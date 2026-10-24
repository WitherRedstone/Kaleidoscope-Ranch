package com.chinaex123.kaleidoscope_ranch.init;

import com.chinaex123.kaleidoscope_ranch.KaleidoscopeRanch;
import com.chinaex123.kaleidoscope_ranch.block.FeedingTroughBlock;
import com.chinaex123.kaleidoscope_ranch.block.HaystackHollowBlock;
import com.chinaex123.kaleidoscope_ranch.block.RoseSaltCrystalCoreBlock;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.AmethystClusterBlock;
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

    /** 玫瑰盐岩 */
    DeferredBlock<Block> ROSE_SALT_ROCK = registerBlocks("rose_salt_rock", () -> new Block(BlockBehaviour.Properties.of()
            .strength(1.5F, 6.0F).mapColor(MapColor.COLOR_PURPLE).sound(SoundType.STONE).requiresCorrectToolForDrops()));
    /** 玫瑰盐晶 */
    DeferredBlock<Block> ROSE_SALT_CRYSTAL = registerBlocks("rose_salt_crystal", () -> new Block(BlockBehaviour.Properties.of()
            .strength(1.5F, 6.0F).mapColor(MapColor.COLOR_PURPLE).sound(SoundType.AMETHYST).requiresCorrectToolForDrops()));
    /** 玫瑰盐晶核 */
    DeferredBlock<RoseSaltCrystalCoreBlock> ROSE_SALT_CRYSTAL_CORE = registerBlocks("rose_salt_crystal_core", () -> new RoseSaltCrystalCoreBlock(BlockBehaviour.Properties.of()
            .strength(1.5F, 6.0F).mapColor(MapColor.COLOR_PURPLE).sound(SoundType.AMETHYST).randomTicks().noLootTable()));
    /** 小玫瑰盐晶芽 */
    DeferredBlock<AmethystClusterBlock> SMALL_ROSE_SALT_CRYSTAL_BUD = registerBlocks("small_rose_salt_crystal_bud", () -> new AmethystClusterBlock(2, 1, BlockBehaviour.Properties.of()
            .strength(1.5F, 6.0F).mapColor(MapColor.COLOR_PURPLE).sound(SoundType.AMETHYST).noOcclusion().randomTicks().noLootTable()));
    /** 中玫瑰盐晶芽 */
    DeferredBlock<AmethystClusterBlock> MEDIUM_ROSE_SALT_CRYSTAL_BUD = registerBlocks("medium_rose_salt_crystal_bud", () -> new AmethystClusterBlock(4, 3, BlockBehaviour.Properties.of()
            .strength(1.5F, 6.0F).mapColor(MapColor.COLOR_PURPLE).sound(SoundType.AMETHYST).noOcclusion().randomTicks().noLootTable()));
    /** 大玫瑰盐晶芽 */
    DeferredBlock<AmethystClusterBlock> LARGE_ROSE_SALT_CRYSTAL_BUD = registerBlocks("large_rose_salt_crystal_bud", () -> new AmethystClusterBlock(5, 4, BlockBehaviour.Properties.of()
            .strength(1.5F, 6.0F).mapColor(MapColor.COLOR_PURPLE).sound(SoundType.AMETHYST).noOcclusion().randomTicks().noLootTable()));
    /** 玫瑰盐晶簇 */
    DeferredBlock<AmethystClusterBlock> ROSE_SALT_CLUSTER = registerBlocks("rose_salt_cluster", () -> new AmethystClusterBlock(7, 3, BlockBehaviour.Properties.of()
            .strength(1.5F, 6.0F).mapColor(MapColor.COLOR_PURPLE).sound(SoundType.AMETHYST).noOcclusion().requiresCorrectToolForDrops()));

    /** 玫瑰盐块 */
    DeferredBlock<Block> ROSE_SALT_BLOCK = registerBlocks("rose_salt_block", () -> new Block(BlockBehaviour.Properties.of()
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