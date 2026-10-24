package com.chinaex123.kaleidoscope_ranch.block;

import com.chinaex123.kaleidoscope_ranch.init.KRBlocks;
import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.block.AmethystBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.level.material.Fluids;

import org.jetbrains.annotations.Nullable;

public class RoseSaltCrystalCoreBlock extends AmethystBlock {
    public static final DirectionProperty FACING = BlockStateProperties.FACING;

    private static final Direction[] DIRECTIONS = Direction.values();

    public RoseSaltCrystalCoreBlock(Properties properties) {
        super(properties);
    }

    @Override
    public MapCodec<? extends AmethystBlock> codec() {
        return simpleCodec(RoseSaltCrystalCoreBlock::new);
    }

    @Override
    protected void randomTick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
        Direction direction = DIRECTIONS[random.nextInt(DIRECTIONS.length)];
        BlockPos targetPos = pos.relative(direction);
        BlockState targetState = level.getBlockState(targetPos);
        Block targetBlock = targetState.getBlock();

        if (!canBudGrowAtPos(level, targetPos)) {
            return;
        }

        if (targetState.isAir() || targetState.getFluidState().is(Fluids.WATER)) {
            BlockState budState = KRBlocks.SMALL_ROSE_SALT_CRYSTAL_BUD.get().defaultBlockState()
                    .setValue(FACING, direction);
            if (targetState.getFluidState().is(Fluids.WATER)) {
                budState = budState.setValue(BlockStateProperties.WATERLOGGED, true);
            }
            if (budState.canSurvive(level, targetPos)) {
                level.setBlockAndUpdate(targetPos, budState);
                playGrowthSound(level, targetPos, random);
            }
            return;
        }

        Block nextBlock = getNextGrowthStage(targetBlock);
        if (nextBlock != null) {
            Direction budFacing = targetState.getValue(FACING);
            if (!isAttachedToCore(level, targetPos, budFacing)) {
                return;
            }

            if (direction != budFacing) {
                return;
            }
            BlockState nextState = nextBlock.defaultBlockState()
                    .setValue(FACING, budFacing)
                    .setValue(BlockStateProperties.WATERLOGGED,
                            targetState.getValue(BlockStateProperties.WATERLOGGED));

            if (nextState.canSurvive(level, targetPos)) {
                level.setBlockAndUpdate(targetPos, nextState);
                playGrowthSound(level, targetPos, random);
            }
        }
    }

    private static boolean isAttachedToCore(ServerLevel level, BlockPos budPos, Direction budFacing) {
        BlockPos behind = budPos.relative(budFacing.getOpposite());
        return level.getBlockState(behind).is(KRBlocks.ROSE_SALT_CRYSTAL_CORE.get());
    }

    private static void playGrowthSound(ServerLevel level, BlockPos pos, RandomSource random) {
        level.playSound(
                null,
                pos,
                SoundEvents.AMETHYST_BLOCK_RESONATE,
                SoundSource.BLOCKS,
                0.6F,
                0.9F + random.nextFloat() * 0.2F
        );
    }

    public static boolean canBudGrowAtPos(ServerLevel level, BlockPos pos) {
        BlockState state = level.getBlockState(pos);
        return state.isAir() || state.getFluidState().is(Fluids.WATER) || isBud(state.getBlock());
    }

    @Nullable
    private static Block getNextGrowthStage(Block block) {
        if (block == KRBlocks.SMALL_ROSE_SALT_CRYSTAL_BUD.get()) return KRBlocks.MEDIUM_ROSE_SALT_CRYSTAL_BUD.get();
        if (block == KRBlocks.MEDIUM_ROSE_SALT_CRYSTAL_BUD.get()) return KRBlocks.LARGE_ROSE_SALT_CRYSTAL_BUD.get();
        if (block == KRBlocks.LARGE_ROSE_SALT_CRYSTAL_BUD.get()) return KRBlocks.ROSE_SALT_CLUSTER.get();
        return null;
    }

    private static boolean isBud(Block block) {
        return block == KRBlocks.SMALL_ROSE_SALT_CRYSTAL_BUD.get()
                || block == KRBlocks.MEDIUM_ROSE_SALT_CRYSTAL_BUD.get()
                || block == KRBlocks.LARGE_ROSE_SALT_CRYSTAL_BUD.get()
                || block == KRBlocks.ROSE_SALT_CLUSTER.get();
    }
}