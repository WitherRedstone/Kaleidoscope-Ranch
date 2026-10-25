package com.chinaex123.kaleidoscope_ranch.entity.ai;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.goal.WrappedGoal;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.player.Player;

import java.util.EnumSet;

public class AnimalChaseAndHurtGoal extends Goal {

    private final Animal animal;
    private final double speed;
    private final float damage;
    private boolean attacked;
    private int attackCooldown;

    public AnimalChaseAndHurtGoal(Animal animal, double speed, float damage) {
        this.animal = animal;
        this.speed = speed;
        this.damage = damage;
        this.setFlags(EnumSet.of(Flag.MOVE, Flag.LOOK));
    }

    @Override
    public boolean canUse() {
        LivingEntity target = animal.getTarget();
        return target != null && target.isAlive();
    }

    @Override
    public boolean canContinueToUse() {
        LivingEntity target = animal.getTarget();
        if (target == null || !target.isAlive()) return false;
        return !attacked || attackCooldown > 0;
    }

    @Override
    public void start() {
        attacked = false;
        attackCooldown = 0;
        animal.goalSelector.getAvailableGoals().stream()
                .map(WrappedGoal::getGoal)
                .filter(goal -> goal instanceof KRFleeFromPlayerGoal)
                .toList()
                .forEach(animal.goalSelector::removeGoal);
        LivingEntity target = animal.getTarget();
        if (target != null) {
            animal.getLookControl().setLookAt(target, 10.0F, 10.0F);
            animal.getNavigation().moveTo(target, speed);
        }
    }

    @Override
    public void stop() {
        animal.getNavigation().stop();
        animal.setTarget(null);
        animal.goalSelector.addGoal(0, new KRFleeFromPlayerGoal(animal));
    }

    @Override
    public void tick() {
        LivingEntity target = animal.getTarget();
        if (target == null) return;

        animal.getLookControl().setLookAt(target, 10.0F, 10.0F);

        if (attackCooldown > 0) {
            attackCooldown--;
            animal.getNavigation().moveTo(target, speed);
            return;
        }

        double dist = animal.distanceTo(target);
        if (dist <= 1.8 && !attacked) {
            if (target instanceof Player player && !player.isCreative()) {
                player.hurt(animal.damageSources().mobAttack(animal), damage);
            }
            attacked = true;
            attackCooldown = 20;
        } else if (dist > 1.8) {
            animal.getNavigation().moveTo(target, speed);
        }
    }
}