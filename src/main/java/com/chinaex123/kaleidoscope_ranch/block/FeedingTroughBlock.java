package com.chinaex123.kaleidoscope_ranch.block;

import com.chinaex123.kaleidoscope_ranch.blockentity.FeedingTroughBlockEntity;
import com.chinaex123.kaleidoscope_ranch.init.KRBlockEntityTypes;
import com.chinaex123.kaleidoscope_ranch.init.KRItems;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import com.mojang.serialization.MapCodec;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class FeedingTroughBlock extends BaseEntityBlock {

    public static final DirectionProperty FACING = BlockStateProperties.HORIZONTAL_FACING;

    private static final VoxelShape NORTH_SHAPE = Block.box(0, 0, 2, 16, 10, 16);
    private static final VoxelShape SOUTH_SHAPE = Block.box(0, 0, 0, 16, 10, 14);
    private static final VoxelShape EAST_SHAPE = Block.box(0, 0, 0, 14, 10, 16);
    private static final VoxelShape WEST_SHAPE = Block.box(2, 0, 0, 16, 10, 16);

    public FeedingTroughBlock(Properties properties) {
        super(properties);
        this.registerDefaultState(this.stateDefinition.any().setValue(FACING, Direction.NORTH));
    }

    @Override
    protected MapCodec<? extends BaseEntityBlock> codec() {
        return simpleCodec(FeedingTroughBlock::new);
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(FACING);
    }

    @Override
    public @NotNull VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        return switch (state.getValue(FACING)) {
            case NORTH -> NORTH_SHAPE;
            case SOUTH -> SOUTH_SHAPE;
            case EAST -> EAST_SHAPE;
            case WEST -> WEST_SHAPE;
            default -> NORTH_SHAPE;
        };
    }

    @Override
    public @NotNull RenderShape getRenderShape(BlockState state) {
        return RenderShape.MODEL;
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new FeedingTroughBlockEntity(pos, state);
    }

    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> type) {
        if (!level.isClientSide()) {
            return createTickerHelper(type, KRBlockEntityTypes.FEEDING_TROUGH.get(), FeedingTroughBlockEntity::serverTick);
        }
        return null;
    }

    @Override
    protected @NotNull InteractionResult useWithoutItem(BlockState state, Level level, BlockPos pos, Player player, BlockHitResult hitResult) {
        if (level.isClientSide()) return InteractionResult.SUCCESS;
        if (!(level.getBlockEntity(pos) instanceof FeedingTroughBlockEntity be)) return InteractionResult.PASS;

        if (player.isShiftKeyDown()) {
            ItemStack extracted = be.extractOneGroup();
            if (!extracted.isEmpty()) {
                player.getInventory().placeItemBackInInventory(extracted);
                level.playSound(null, pos, SoundEvents.ITEM_PICKUP, SoundSource.BLOCKS, 0.5f, 1.0f);
                be.setChanged();
                return InteractionResult.sidedSuccess(level.isClientSide());
            }
            return InteractionResult.PASS;
        }

        ItemStack mainHand = player.getMainHandItem();
        ItemStack offHand = player.getOffhandItem();
        ItemStack feedStack = null;

        if (mainHand.is(KRItems.LIVESTOCK_FEED.get())) {
            feedStack = mainHand;
        } else if (offHand.is(KRItems.LIVESTOCK_FEED.get())) {
            feedStack = offHand;
        }

        if (feedStack == null) return InteractionResult.PASS;

        int inserted = be.insertItem(feedStack);
        if (inserted > 0) {
            level.playSound(null, pos, SoundEvents.BUNDLE_INSERT, SoundSource.BLOCKS, 0.5f, 1.0f);
            be.setChanged();
            return InteractionResult.sidedSuccess(level.isClientSide());
        }

        return InteractionResult.PASS;
    }
}