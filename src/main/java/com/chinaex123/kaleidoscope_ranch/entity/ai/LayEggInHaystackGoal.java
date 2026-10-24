package com.chinaex123.kaleidoscope_ranch.entity.ai;

import com.chinaex123.kaleidoscope_ranch.config.KRConfig;
import com.chinaex123.kaleidoscope_ranch.init.KRBlocks;
import com.chinaex123.kaleidoscope_ranch.init.tags.KREntityTags;
import com.chinaex123.kaleidoscope_ranch.util.KRHoldEgg;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.animal.Chicken;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.pathfinder.Path;

public class LayEggInHaystackGoal extends Goal {

    private final Chicken chicken;
    private BlockPos targetPos;
    private int cooldown;

    public LayEggInHaystackGoal(Chicken chicken) {
        this.chicken = chicken;
    }

    @Override
    public boolean canUse() {
        if (chicken.level().isClientSide) return false;
        if (!chicken.isAlive() || chicken.isBaby() || chicken.isChickenJockey()) return false;
        if (!KREntityTags.isManagedAnimal(chicken)) return false;

        if (!((KRHoldEgg) chicken).KR$isHoldingEgg()) return false;

        if (isNearHaystack(chicken.blockPosition())) return false;

        if (cooldown > 0) {
            cooldown--;
            return false;
        }

        BlockPos found = findNearestHaystack(chicken, KRConfig.HAYSTACK_DETECT_RADIUS.get());
        if (found == null) return false;
        targetPos = found;
        return true;
    }

    @Override
    public boolean canContinueToUse() {
        if (targetPos == null) return false;
        if (!((KRHoldEgg) chicken).KR$isHoldingEgg()) return false;
        if (isNearHaystack(chicken.blockPosition())) return false;
        return isHaystack(targetPos);
    }

    @Override
    public void start() {
        Path path = chicken.getNavigation().createPath(targetPos, 2);
        if (path != null) {
            chicken.getNavigation().moveTo(path, 1.0D);
        } else {
            cooldown = 40;
            targetPos = null;
        }
    }

    @Override
    public void stop() {
        targetPos = null;
        chicken.getNavigation().stop();
        cooldown = 20;
    }

    @Override
    public void tick() {
        if (targetPos == null) return;

        if (isNearHaystack(chicken.blockPosition())) {
            chicken.getNavigation().stop();
            return;
        }

        if (chicken.getNavigation().isDone()) {
            Path path = chicken.getNavigation().createPath(targetPos, 2);
            if (path != null) {
                chicken.getNavigation().moveTo(path, 1.0D);
            } else {
                cooldown = 40;
                targetPos = null;
            }
        }
    }

    private boolean isNearHaystack(BlockPos chickenPos) {
        for (BlockPos pos : BlockPos.betweenClosed(chickenPos.offset(-1, -1, -1), chickenPos.offset(1, 1, 1))) {
            if (isHaystack(pos)) return true;
        }
        return false;
    }

    private boolean isHaystack(BlockPos pos) {
        BlockState state = chicken.level().getBlockState(pos);
        return state.getBlock() == KRBlocks.HAYSTACK_HOLLOW.get();
    }

    private BlockPos findNearestHaystack(Chicken chicken, int radius) {
        BlockPos origin = chicken.blockPosition();
        BlockPos best = null;
        double bestDist = Double.MAX_VALUE;
        for (BlockPos pos : BlockPos.betweenClosed(origin.offset(-radius, -2, -radius), origin.offset(radius, 2, radius))) {
            if (isHaystack(pos)) {
                double d = pos.distSqr(origin);
                if (d < bestDist) {
                    bestDist = d;
                    best = pos.immutable();
                }
            }
        }
        return best;
    }
}