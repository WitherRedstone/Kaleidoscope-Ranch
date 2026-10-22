package com.chinaex123.kaleidoscope_ranch.init;

import com.chinaex123.kaleidoscope_ranch.KaleidoscopeRanch;
import com.chinaex123.kaleidoscope_ranch.block.FeedingTroughBlock;
import com.chinaex123.kaleidoscope_ranch.block.HaystackHollowBlock;
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

    /** 玫瑰盐块 */
    DeferredBlock<Block> SALT_BLOCK = registerBlocks("rose_salt_block", () -> new Block(BlockBehaviour.Properties.of()
            .strength(1.5F, 6.0F).mapColor(MapColor.STONE).sound(SoundType.STONE).requiresCorrectToolForDrops()));
    /** 粪便 */
    DeferredBlock<Block> FECES = BLOCKS.register("feces", () -> new Block(BlockBehaviour.Properties.of()
            .strength(0.5F, 3.0F).mapColor(MapColor.STONE).sound(SoundType.STONE).requiresCorrectToolForDrops()));
    /** 喂食槽 */
    DeferredBlock<FeedingTroughBlock> FEEDING_TROUGH = registerBlocks("feeding_trough", () -> new FeedingTroughBlock(BlockBehaviour.Properties.of()
            .strength(0.5F, 3.0F).mapColor(MapColor.WOOD).sound(SoundType.WOOD).requiresCorrectToolForDrops()));
    /** 干草窝 */
    DeferredBlock<HaystackHollowBlock> HAYSTACK_HOLLOW = registerBlocks("haystack_hollow", () -> new HaystackHollowBlock(BlockBehaviour.Properties.of()
            .strength(0.5F, 3.0F).mapColor(MapColor.WOOD).sound(SoundType.WOOD).requiresCorrectToolForDrops()));


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