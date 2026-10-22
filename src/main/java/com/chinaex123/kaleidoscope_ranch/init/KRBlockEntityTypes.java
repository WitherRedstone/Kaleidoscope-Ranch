package com.chinaex123.kaleidoscope_ranch.init;

import com.chinaex123.kaleidoscope_ranch.KaleidoscopeRanch;
import com.chinaex123.kaleidoscope_ranch.blockentity.FeedingTroughBlockEntity;
import com.chinaex123.kaleidoscope_ranch.blockentity.HaystackHollowBlockEntity;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public interface KRBlockEntityTypes {

    DeferredRegister<BlockEntityType<?>> BLOCK_ENTITY_TYPES =
            DeferredRegister.create(Registries.BLOCK_ENTITY_TYPE, KaleidoscopeRanch.MODID);

    DeferredHolder<BlockEntityType<?>, BlockEntityType<FeedingTroughBlockEntity>> FEEDING_TROUGH = BLOCK_ENTITY_TYPES.register("feeding_trough",
            () -> BlockEntityType.Builder.of(FeedingTroughBlockEntity::new, KRBlocks.FEEDING_TROUGH.get()).build(null));
    DeferredHolder<BlockEntityType<?>, BlockEntityType<HaystackHollowBlockEntity>> HAYSTACK_HOLLOW = BLOCK_ENTITY_TYPES.register("haystack_hollow",
            () -> BlockEntityType.Builder.of(HaystackHollowBlockEntity::new, KRBlocks.HAYSTACK_HOLLOW.get()).build(null));

    static void register(IEventBus eventBus) {
        BLOCK_ENTITY_TYPES.register(eventBus);
    }
}