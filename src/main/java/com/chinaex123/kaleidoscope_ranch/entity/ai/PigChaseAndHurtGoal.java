package com.chinaex123.kaleidoscope_ranch.entity.ai;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.goal.WrappedGoal;
import net.minecraft.world.entity.animal.Pig;
import net.minecraft.world.entity.player.Player;

import java.util.EnumSet;

public class PigChaseAndHurtGoal extends Goal {

    private final Pig pig;
    private final double speed;
    private final float damage;
    private boolean attacked;
    private int attackCooldown;

    public PigChaseAndHurtGoal(Pig pig, double speed, float damage) {
        this.pig = pig;
        this.speed = speed;
        this.damage = damage;
        this.setFlags(EnumSet.of(Flag.MOVE, Flag.LOOK));
    }

    @Override
    public boolean canUse() {
        LivingEntity target = pig.getTarget();
        return target != null && target.isAlive();
    }

    @Override
    public boolean canContinueToUse() {
        LivingEntity target = pig.getTarget();
        if (target == null || !target.isAlive()) return false;
        return !attacked || attackCooldown > 0;
    }

    @Override
    public void start() {
        attacked = false;
        attackCooldown = 0;
        pig.goalSelector.getAvailableGoals().stream()
                .map(WrappedGoal::getGoal)
                .filter(goal -> goal instanceof KRFleeFromPlayerGoal)
                .toList()
                .forEach(pig.goalSelector::removeGoal);
    }

    @Override
    public void stop() {
        pig.getNavigation().stop();
        pig.setTarget(null);
        pig.goalSelector.addGoal(0, new KRFleeFromPlayerGoal(pig));
    }

    @Override
    public void tick() {
        LivingEntity target = pig.getTarget();
        if (target == null) return;

        if (attackCooldown > 0) {
            attackCooldown--;
            return;
        }

        pig.getLookControl().setLookAt(target, 10.0F, 10.0F);

        double dist = pig.distanceTo(target);
        if (dist <= 1.8 && !attacked) {
            if (target instanceof Player player && !player.isCreative()) {
                player.hurt(pig.damageSources().mobAttack(pig), damage);
            }
            attacked = true;
            attackCooldown = 20;
        } else if (dist > 1.8) {
            pig.getNavigation().moveTo(target, speed);
        }
    }
}