package com.chinaex123.kaleidoscope_ranch.item;

import com.chinaex123.kaleidoscope_ranch.init.KRBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gameevent.GameEvent;
import org.jetbrains.annotations.NotNull;

public class FecesItem extends Item {

    public FecesItem(Properties properties) {
        super(properties);
    }

    @Override
    public @NotNull InteractionResult useOn(UseOnContext context) {
        if (context.getPlayer() != null && !context.getPlayer().isShiftKeyDown()) return InteractionResult.PASS;

        Level level = context.getLevel();
        BlockPos placePos = context.getClickedPos().relative(context.getClickedFace());
        BlockState placeState = KRBlocks.FECES.get().defaultBlockState();

        if (!level.getBlockState(placePos).canBeReplaced()) return InteractionResult.FAIL;

        level.setBlockAndUpdate(placePos, placeState);
        level.gameEvent(GameEvent.BLOCK_PLACE, placePos, GameEvent.Context.of(context.getPlayer(), placeState));
        level.playSound(null, placePos, SoundEvents.MUD_PLACE, SoundSource.BLOCKS, 1.0F, 1.0F);

        if (!context.getPlayer().hasInfiniteMaterials()) {
            context.getItemInHand().shrink(1);
        }

        return InteractionResult.sidedSuccess(level.isClientSide());
    }
}