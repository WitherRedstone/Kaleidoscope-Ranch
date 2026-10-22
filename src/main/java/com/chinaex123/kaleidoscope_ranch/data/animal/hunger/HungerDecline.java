package com.chinaex123.kaleidoscope_ranch.data.animal.hunger;

import com.chinaex123.kaleidoscope_ranch.config.KRConfig;
import com.chinaex123.kaleidoscope_ranch.init.KRBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.level.block.Block;

public class HungerDecline {

    public static int getFecesDecayTickInterval() {
        return KRConfig.HUNGER_DECLINE_FECES_TICK_INTERVAL.get();
    }

    public static float calcTickPenalty(ServerLevel level, Animal animal, boolean wasFedToday) {
        float delta = 0f;
        if (!wasFedToday) {
            delta -= KRConfig.HUNGER_DECLINE_DAILY_AMOUNT.get().floatValue();
        }
        int fecesCount = countNearbyFeces(level, animal.blockPosition());
        if (fecesCount > 0) {
            delta -= KRConfig.HUNGER_DECLINE_FECES_AMOUNT.get().floatValue() * fecesCount;
        }
        return delta;
    }

    public static int countNearbyFeces(ServerLevel level, BlockPos center) {
        int count = 0;
        Block fecesBlock = KRBlocks.FECES.get();
        int r = KRConfig.HUNGER_DECLINE_FECES_RADIUS.get();
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