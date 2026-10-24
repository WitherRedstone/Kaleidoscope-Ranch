package com.chinaex123.kaleidoscope_ranch.data.tags;

import com.chinaex123.kaleidoscope_ranch.KaleidoscopeRanch;
import com.chinaex123.kaleidoscope_ranch.init.KRBlocks;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.tags.BlockTags;
import net.neoforged.neoforge.common.Tags;
import net.neoforged.neoforge.common.data.BlockTagsProvider;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.concurrent.CompletableFuture;

public class ModBlockTagsProvider extends BlockTagsProvider {
    public ModBlockTagsProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider, @Nullable ExistingFileHelper existingFileHelper) {
        super(output, lookupProvider, KaleidoscopeRanch.MODID, existingFileHelper);
    }

    @Override
    protected void addTags(HolderLookup.@NotNull Provider provider) {

        tag(Tags.Blocks.NEEDS_WOOD_TOOL)
                .add(KRBlocks.FEEDING_TROUGH.get())
                .add(KRBlocks.HAYSTACK_HOLLOW.get());
        tag(BlockTags.NEEDS_IRON_TOOL)
                .add(KRBlocks.ROSE_SALT_ROCK.get())
                .add(KRBlocks.ROSE_SALT_CRYSTAL.get())
                .add(KRBlocks.ROSE_SALT_CRYSTAL_CORE.get())
                .add(KRBlocks.SMALL_ROSE_SALT_CRYSTAL_BUD.get())
                .add(KRBlocks.MEDIUM_ROSE_SALT_CRYSTAL_BUD.get())
                .add(KRBlocks.LARGE_ROSE_SALT_CRYSTAL_BUD.get())
                .add(KRBlocks.ROSE_SALT_CLUSTER.get())
                .add(KRBlocks.ROSE_SALT_BLOCK.get());

        tag(BlockTags.MINEABLE_WITH_PICKAXE)
                .add(KRBlocks.ROSE_SALT_ROCK.get())
                .add(KRBlocks.ROSE_SALT_CRYSTAL.get())
                .add(KRBlocks.ROSE_SALT_CRYSTAL_CORE.get())
                .add(KRBlocks.SMALL_ROSE_SALT_CRYSTAL_BUD.get())
                .add(KRBlocks.MEDIUM_ROSE_SALT_CRYSTAL_BUD.get())
                .add(KRBlocks.LARGE_ROSE_SALT_CRYSTAL_BUD.get())
                .add(KRBlocks.ROSE_SALT_CLUSTER.get())
                .add(KRBlocks.ROSE_SALT_BLOCK.get());
        tag(BlockTags.MINEABLE_WITH_AXE)
                .add(KRBlocks.FEEDING_TROUGH.get())
                .add(KRBlocks.HAYSTACK_HOLLOW.get());

    }
}
