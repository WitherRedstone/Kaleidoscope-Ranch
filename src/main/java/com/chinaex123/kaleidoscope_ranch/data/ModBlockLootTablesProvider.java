//package com.chinaex123.kaleidoscope_ranch.data;
//
//import com.chinaex123.kaleidoscope_ranch.init.KRBlocks;
//import net.minecraft.core.Holder;
//import net.minecraft.core.HolderLookup;
//import net.minecraft.data.loot.BlockLootSubProvider;
//import net.minecraft.world.flag.FeatureFlags;
//import net.minecraft.world.level.block.Block;
//import org.jetbrains.annotations.NotNull;
//
//import java.util.Set;
//
//public class ModBlockLootTablesProvider extends BlockLootSubProvider {
//
//    public ModBlockLootTablesProvider(HolderLookup.Provider registries) {
//        super(Set.of(), FeatureFlags.REGISTRY.allFlags(), registries);
//    }
//
//    @Override
//    protected void generate() {
//        dropSelf(KRBlocks.SALT_BLOCK.get());
//        dropSelf(KRBlocks.FEEDING_TROUGH.get());
//        dropSelf(KRBlocks.HAYSTACK_HOLLOW.get());
//    }
//
//    @Override
//    protected @NotNull Iterable<Block> getKnownBlocks() {
//        return KRBlocks.BLOCKS.getEntries().stream().map(Holder::value).toList();
//    }
//}
