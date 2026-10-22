package com.chinaex123.kaleidoscope_ranch.data.animal.mood;

import com.chinaex123.kaleidoscope_ranch.config.KRConfig;
import com.chinaex123.kaleidoscope_ranch.init.KRBlocks;
import com.chinaex123.kaleidoscope_ranch.init.KREntityTags;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.level.block.Block;

public class MoodRise {

    public static int getSaltBlockMoodBonusTickInterval() {
        return KRConfig.MOOD_RISE_SALT_BLOCK_TICK_INTERVAL.get();
    }

    public static int getMudMoodBonusTickInterval() {
        return KRConfig.MOOD_RISE_MUD_TICK_INTERVAL.get();
    }

    public static int calcTickBonus(ServerLevel level, Animal animal, int currentMood, long tickCount) {
        if (tickCount % KRConfig.MOOD_RISE_SALT_BLOCK_TICK_INTERVAL.get() == 0) {
            return calcSaltBlockBonus(level, animal, currentMood);
        }
        return 0;
    }

    public static int calcPetBonus() {
        return KRConfig.MOOD_RISE_PET_BONUS.get();
    }

    public static int calcSaltBlockBonus(ServerLevel level, Animal animal, int currentMood) {
        if (currentMood >= AnimalMood.getInitialMood()) return 0;

        Block saltBlock = KRBlocks.SALT_BLOCK.get();
        BlockPos center = animal.blockPosition();
        int r = KRConfig.MOOD_RISE_SALT_BLOCK_RADIUS.get();
        for (BlockPos pos : BlockPos.betweenClosed(
                center.getX() - r, center.getY() - r, center.getZ() - r,
                center.getX() + r, center.getY() + r, center.getZ() + r)) {
            if (level.getBlockState(pos).getBlock() == saltBlock) {
                return KRConfig.MOOD_RISE_SALT_BLOCK_BONUS.get();
            }
        }
        return 0;
    }

    public static int calcMudBonus(ServerLevel level, Animal animal, int currentMood) {
        if (!animal.getType().is(KREntityTags.FAVOR_MUD)) return 0;
        if (currentMood >= AnimalMood.getInitialMood()) return 0;
        if (level.getBlockState(animal.blockPosition()).getBlock() == net.minecraft.world.level.block.Blocks.MUD
                || level.getBlockState(animal.blockPosition().below()).getBlock() == net.minecraft.world.level.block.Blocks.MUD) {
            return KRConfig.MOOD_RISE_MUD_BONUS.get();
        }
        return 0;
    }
}