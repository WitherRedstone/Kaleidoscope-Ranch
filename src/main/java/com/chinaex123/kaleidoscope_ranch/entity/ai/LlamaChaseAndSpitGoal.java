package com.chinaex123.kaleidoscope_ranch.entity.ai;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.goal.WrappedGoal;
import net.minecraft.world.entity.animal.horse.Llama;
import net.minecraft.world.entity.projectile.LlamaSpit;

import java.util.EnumSet;

public class LlamaChaseAndSpitGoal extends Goal {

    private final Llama llama;
    private final double speed;
    private boolean hasSpit;
    private int spitCooldown;

    public LlamaChaseAndSpitGoal(Llama llama, double speed) {
        this.llama = llama;
        this.speed = speed;
        this.setFlags(EnumSet.of(Flag.MOVE, Flag.LOOK));
    }

    @Override
    public boolean canUse() {
        LivingEntity target = llama.getTarget();
        return target != null && target.isAlive();
    }

    @Override
    public boolean canContinueToUse() {
        LivingEntity target = llama.getTarget();
        if (target == null || !target.isAlive()) return false;
        return !hasSpit || spitCooldown > 0;
    }

    @Override
    public void start() {
        hasSpit = false;
        spitCooldown = 0;
        llama.goalSelector.getAvailableGoals().stream()
                .map(WrappedGoal::getGoal)
                .filter(goal -> goal instanceof KRFleeFromPlayerGoal)
                .toList()
                .forEach(llama.goalSelector::removeGoal);
        LivingEntity target = llama.getTarget();
        if (target != null) {
            llama.getLookControl().setLookAt(target, 10.0F, 10.0F);
            llama.getNavigation().moveTo(target, speed);
        }
    }

    @Override
    public void stop() {
        llama.getNavigation().stop();
        llama.setTarget(null);
        llama.goalSelector.addGoal(0, new KRFleeFromPlayerGoal(llama));
    }

    @Override
    public void tick() {
        LivingEntity target = llama.getTarget();
        if (target == null) return;

        llama.getLookControl().setLookAt(target, 10.0F, 10.0F);

        if (spitCooldown > 0) {
            spitCooldown--;
            llama.getNavigation().moveTo(target, speed);
            return;
        }

        double dist = llama.distanceTo(target);
        if (dist <= 4.0 && !hasSpit) {
            LlamaSpit spit = new LlamaSpit(llama.level(), llama);
            double dx = target.getX() - llama.getX();
            double dy = target.getY(0.3333333333333333) - spit.getY();
            double dz = target.getZ() - llama.getZ();
            double d = Math.sqrt(dx * dx + dy * dy + dz * dz);
            spit.shoot(dx, dy, dz, (float) (d * 0.2F + 1.0F), 1.0F);
            llama.level().addFreshEntity(spit);
            hasSpit = true;
            spitCooldown = 20;
        } else {
            llama.getNavigation().moveTo(target, speed);
        }
    }
}