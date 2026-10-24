package com.chinaex123.kaleidoscope_ranch.entity.ai;

import com.chinaex123.kaleidoscope_ranch.data.animal.mood.AnimalMoodSavedData;
import com.chinaex123.kaleidoscope_ranch.entity.AnimalMoodEventHandler;
import com.chinaex123.kaleidoscope_ranch.init.tags.KREntityTags;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.animal.Pig;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.CropBlock;
import net.minecraft.world.level.block.state.BlockState;

public class PigMudBoostGoal extends Goal {

    private static final int BOOST_INTERVAL = 100;
    private static final int BOOST_RADIUS = 3;
    private static final double MOOD_THRESHOLD = 100.0;

    private final Pig pig;
    private int cooldown;

    public PigMudBoostGoal(Pig pig) {
        this.pig = pig;
    }

    @Override
    public boolean canUse() {
        if (pig.level().isClientSide()) return false;
        if (pig.isBaby()) return false;
        if (!KREntityTags.isManagedAnimal(pig)) return false;

        if (cooldown > 0) {
            cooldown--;
            return false;
        }

        ServerLevel level = (ServerLevel) pig.level();

        AnimalMoodSavedData moodData = AnimalMoodEventHandler.getAnimalMoodData(level);
        double mood = moodData.getAnimalMoodScore(pig.getUUID());
        if (mood < MOOD_THRESHOLD) return false;

        BlockPos pigPos = pig.blockPosition();
        BlockState groundState = level.getBlockState(pigPos.below());
        return groundState.is(Blocks.MUD) || groundState.is(Blocks.MUD_BRICKS);
    }

    @Override
    public void start() {
        if (!(pig.level() instanceof ServerLevel level)) return;

        BlockPos center = pig.blockPosition();
        for (int dx = -BOOST_RADIUS; dx <= BOOST_RADIUS; dx++) {
            for (int dz = -BOOST_RADIUS; dz <= BOOST_RADIUS; dz++) {
                for (int dy = 0; dy <= 2; dy++) {
                    BlockPos pos = center.offset(dx, dy, dz);
                    BlockState state = level.getBlockState(pos);
                    if (state.getBlock() instanceof CropBlock crop) {
                        if (!crop.isMaxAge(state)) {
                            BlockState newState = crop.getStateForAge(crop.getAge(state) + 1);
                            level.setBlockAndUpdate(pos, newState);
                        }
                    }
                }
            }
        }

        cooldown = BOOST_INTERVAL;
    }
}