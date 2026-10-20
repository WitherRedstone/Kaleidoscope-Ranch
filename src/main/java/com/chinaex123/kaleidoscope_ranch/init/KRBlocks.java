package com.chinaex123.kaleidoscope_ranch.init;

import com.chinaex123.kaleidoscope_ranch.KaleidoscopeRanch;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.MapColor;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredBlock;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

public interface KRBlocks {
    DeferredRegister.Blocks BLOCKS = DeferredRegister.createBlocks(KaleidoscopeRanch.MODID);

    /** 盐块 */
    DeferredBlock<Block> SALT_BLOCK = registerBlocks("salt_block", () -> new Block(BlockBehaviour.Properties.of()
            .strength(1.5F, 6.0F).mapColor(MapColor.STONE).sound(SoundType.STONE).requiresCorrectToolForDrops()));
    /** 粪便 */
    DeferredBlock<Block> FECES = registerBlocks("feces", () -> new Block(BlockBehaviour.Properties.of()
            .strength(1.5F, 6.0F).mapColor(MapColor.STONE).sound(SoundType.STONE).requiresCorrectToolForDrops()));


    static <T extends Block> void registerBlockItems(String name, DeferredBlock<T> block) {
        KRItems.ITEMS.register(name, () -> new BlockItem(block.get(), new Item.Properties()));
    }

    static <T extends Block> DeferredBlock<T> registerBlocks(String name, Supplier<T> block) {
        DeferredBlock<T> blocks = BLOCKS.register(name, block);
        registerBlockItems(name, blocks);
        return blocks;
    }

    static void register(IEventBus eventBus){
        BLOCKS.register(eventBus);
    }
}
