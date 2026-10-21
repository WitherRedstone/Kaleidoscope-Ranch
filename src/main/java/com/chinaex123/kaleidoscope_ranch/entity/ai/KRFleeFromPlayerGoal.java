package com.chinaex123.kaleidoscope_ranch.entity.ai;

import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.player.Player;

import java.util.EnumSet;

public class KRFleeFromPlayerGoal extends Goal {

    public static final float DETECT_RADIUS = 4.0f;
    public static final double FLEE_SPEED = 1.1;
    public static final double FLEE_DISTANCE = 4.0;
    public static final double FLEE_DISTANCE_VARIANCE = 4.0;

    private final PathfinderMob mob;
    private Player fleeTarget;

    public KRFleeFromPlayerGoal(PathfinderMob mob) {
        this.mob = mob;
        this.setFlags(EnumSet.of(Flag.MOVE));
    }

    @Override
    public boolean canUse() {
        fleeTarget = null;
        for (Player player : mob.level().getEntitiesOfClass(Player.class, mob.getBoundingBox().inflate(DETECT_RADIUS))) {
            if (!player.isShiftKeyDown() && player.distanceTo(mob) <= DETECT_RADIUS) {
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

        double fleeDist = FLEE_DISTANCE + mob.getRandom().nextDouble() * FLEE_DISTANCE_VARIANCE;
        double fleeX = mob.getX() + (dx / dist) * fleeDist;
        double fleeY = mob.getY();
        double fleeZ = mob.getZ() + (dz / dist) * fleeDist;
        mob.getNavigation().moveTo(fleeX, fleeY, fleeZ, FLEE_SPEED);
    }

    @Override
    public void stop() {
        mob.getNavigation().stop();
    }
}