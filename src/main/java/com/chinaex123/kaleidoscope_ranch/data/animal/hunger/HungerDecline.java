package com.chinaex123.kaleidoscope_ranch.data.animal.hunger;

import com.chinaex123.kaleidoscope_ranch.init.KRBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.level.block.Block;

public class HungerDecline {

    public static final float DAILY_DECAY_AMOUNT = 3.0f;
    public static final float FECES_DECAY_AMOUNT = 0.1f;
    public static final int FECES_DECAY_TICK_INTERVAL = 3600;
    public static final int FECES_CHECK_RADIUS = 8;

    public static float calcTickPenalty(ServerLevel level, Animal animal, boolean wasFedToday) {
        float delta = 0f;
        if (!wasFedToday) {
            delta -= DAILY_DECAY_AMOUNT;
        }
        int fecesCount = countNearbyFeces(level, animal.blockPosition());
        if (fecesCount > 0) {
            delta -= FECES_DECAY_AMOUNT * fecesCount;
        }
        return delta;
    }

    public static int countNearbyFeces(ServerLevel level, BlockPos center) {
        int count = 0;
        Block fecesBlock = KRBlocks.FECES.get();
        int r = FECES_CHECK_RADIUS;
        for (BlockPos pos : BlockPos.betweenClosed(
                center.getX() - r, center.getY() - r, center.getZ() - r,
                center.getX() + r, center.getY() + r, center.getZ() + r)) {
            if (level.getBlockState(pos).getBlock() == fecesBlock) {
                count++;
            }
        }
        return count;
    }
}