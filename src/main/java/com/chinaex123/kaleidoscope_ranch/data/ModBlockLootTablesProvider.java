package com.chinaex123.kaleidoscope_ranch.data;

import com.chinaex123.kaleidoscope_ranch.init.KRBlocks;
import com.chinaex123.kaleidoscope_ranch.init.KRItems;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.loot.BlockLootSubProvider;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.storage.loot.providers.number.UniformGenerator;
import org.jetbrains.annotations.NotNull;

import java.util.Set;

public class ModBlockLootTablesProvider extends BlockLootSubProvider {

    public ModBlockLootTablesProvider(HolderLookup.Provider registries) {
        super(Set.of(), FeatureFlags.REGISTRY.allFlags(), registries);
    }

    @Override
    protected void generate() {
        dropSelf(KRBlocks.ROSE_SALT_ROCK.get());
        dropSelf(KRBlocks.ROSE_SALT_CRYSTAL.get());
        add(KRBlocks.ROSE_SALT_CLUSTER.get(), block -> createSingleItemTableWithSilkTouch(
                block, KRItems.ROSE_SALT.get(), UniformGenerator.between(1.0F, 3.0F))
        );
        add(KRBlocks.ROSE_SALT_BLOCK.get(), block -> createSingleItemTableWithSilkTouch(
                block, KRItems.ROSE_SALT.get(), UniformGenerator.between(2.0F, 6.0F))
        );
        dropSelf(KRBlocks.FEEDING_TROUGH.get());
        dropSelf(KRBlocks.HAYSTACK_HOLLOW.get());
    }

    @Override
    protected @NotNull Iterable<Block> getKnownBlocks() {
        return KRBlocks.BLOCKS.getEntries().stream()
                .map(Holder::value)
                .filter(block -> block != KRBlocks.FECES.get())
                .toList();
    }

}