package com.chinaex123.kaleidoscope_ranch.entity.ai;

import com.chinaex123.kaleidoscope_ranch.config.KRConfig;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.player.Player;

import java.util.EnumSet;

public class KRFleeFromPlayerGoal extends Goal {

    public static float getDetectRadius() {
        return (float) KRConfig.FLEE_DETECT_RADIUS.get().doubleValue();
    }

    public static double getFleeSpeed() {
        return KRConfig.FLEE_SPEED.get();
    }

    public static double getFleeDistance() {
        return KRConfig.FLEE_DISTANCE.get();
    }

    public static double getFleeDistanceVariance() {
        return KRConfig.FLEE_DISTANCE_VARIANCE.get();
    }

    private final PathfinderMob mob;
    private Player fleeTarget;

    public KRFleeFromPlayerGoal(PathfinderMob mob) {
        this.mob = mob;
        this.setFlags(EnumSet.of(Flag.MOVE));
    }

    @Override
    public boolean canUse() {
        fleeTarget = null;
        float radius = getDetectRadius();
        for (Player player : mob.level().getEntitiesOfClass(Player.class, mob.getBoundingBox().inflate(radius))) {
            if (!player.isShiftKeyDown() && player.distanceTo(mob) <= radius) {
                fleeTarget = player;
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean canContinueToUse() {
        return !mob.getNavigation().isDone() && mob.getNavigation().getPath() != null;
    }

    @Override
    public void start() {
        if (fleeTarget == null) return;

        double dx = mob.getX() - fleeTarget.getX();
        double dz = mob.getZ() - fleeTarget.getZ();
        double dist = Math.sqrt(dx * dx + dz * dz);
        if (dist < 0.001) {
            double angle = mob.getRandom().nextDouble() * Math.PI * 2.0;
            dx = Math.sin(angle);
            dz = Math.cos(angle);
            dist = 1.0;
        }

        double fleeDist = getFleeDistance() + mob.getRandom().nextDouble() * getFleeDistanceVariance();
        double fleeX = mob.getX() + (dx / dist) * fleeDist;
        double fleeY = mob.getY();
        double fleeZ = mob.getZ() + (dz / dist) * fleeDist;
        mob.getNavigation().moveTo(fleeX, fleeY, fleeZ, getFleeSpeed());
    }

    @Override
    public void stop() {
        mob.getNavigation().stop();
    }
}