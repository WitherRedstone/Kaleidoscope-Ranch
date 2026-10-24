package com.chinaex123.kaleidoscope_ranch.data.tags;

import com.chinaex123.kaleidoscope_ranch.KaleidoscopeRanch;
import com.chinaex123.kaleidoscope_ranch.init.tags.KRItemTags;
import com.chinaex123.kaleidoscope_ranch.init.KRItems;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.ItemTagsProvider;
import net.minecraft.world.level.block.Block;
import net.neoforged.neoforge.common.Tags;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.concurrent.CompletableFuture;

public class ModItemTagsProvider extends ItemTagsProvider {
    public ModItemTagsProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider,
                               CompletableFuture<TagLookup<Block>> blockTags, @Nullable ExistingFileHelper existingFileHelper) {
        super(output, lookupProvider, blockTags, KaleidoscopeRanch.MODID, existingFileHelper);
    }

    @Override
    protected void addTags(HolderLookup.@NotNull Provider provider) {

        tag(KRItemTags.ANIMAL_FEED)
                .add(KRItems.POULTRY_FEED.get())
                .add(KRItems.LIVESTOCK_FEED.get());
        tag(KRItemTags.HAYSTACK_HOLLOW_EGG).addTag(Tags.Items.EGGS);
        tag(KRItemTags.SALTS).add(KRItems.ROSE_SALT.get());

        tag(KRItemTags.BUCKETS_MILK_SHEEP).add(KRItems.SHEEP_MILK_BUCKET.get());
        tag(KRItemTags.BUCKETS_MILK_GOAT).add(KRItems.GOAT_MILK_BUCKET.get());
        tag(KRItemTags.BUCKETS_MILK_HORSE).add(KRItems.HORSE_MILK_BUCKET.get());
        tag(KRItemTags.BUCKETS_MILK_DONKEY).add(KRItems.DONKEY_MILK_BUCKET.get());
        tag(KRItemTags.BUCKETS_MILK_CAMEL).add(KRItems.CAMEL_MILK_BUCKET.get());
        tag(KRItemTags.BUCKETS_MILK_ALPACA).add(KRItems.ALPACA_MILK_BUCKET.get());
        tag(KRItemTags.BUCKETS_MILK_RABBIT).add(KRItems.RABBIT_MILK_BUCKET.get());
        tag(KRItemTags.BUCKETS_MILK_PIG).add(KRItems.PIG_MILK_BUCKET.get());

        tag(Tags.Items.BUCKETS_MILK)
                .add(KRItems.SHEEP_MILK_BUCKET.get())
                .add(KRItems.GOAT_MILK_BUCKET.get())
                .add(KRItems.HORSE_MILK_BUCKET.get())
                .add(KRItems.DONKEY_MILK_BUCKET.get())
                .add(KRItems.CAMEL_MILK_BUCKET.get())
                .add(KRItems.ALPACA_MILK_BUCKET.get())
                .add(KRItems.RABBIT_MILK_BUCKET.get())
                .add(KRItems.PIG_MILK_BUCKET.get());
        tag(Tags.Items.DRINKS_MILK)
                .add(KRItems.SHEEP_MILK_BUCKET.get())
                .add(KRItems.GOAT_MILK_BUCKET.get())
                .add(KRItems.HORSE_MILK_BUCKET.get())
                .add(KRItems.DONKEY_MILK_BUCKET.get())
                .add(KRItems.CAMEL_MILK_BUCKET.get())
                .add(KRItems.ALPACA_MILK_BUCKET.get())
                .add(KRItems.RABBIT_MILK_BUCKET.get())
                .add(KRItems.PIG_MILK_BUCKET.get());
        tag(Tags.Items.BUCKETS)
                .add(KRItems.SHEEP_MILK_BUCKET.get())
                .add(KRItems.GOAT_MILK_BUCKET.get())
                .add(KRItems.HORSE_MILK_BUCKET.get())
                .add(KRItems.DONKEY_MILK_BUCKET.get())
                .add(KRItems.CAMEL_MILK_BUCKET.get())
                .add(KRItems.ALPACA_MILK_BUCKET.get())
                .add(KRItems.RABBIT_MILK_BUCKET.get())
                .add(KRItems.PIG_MILK_BUCKET.get());
        tag(Tags.Items.DRINKS)
                .add(KRItems.SHEEP_MILK_BUCKET.get())
                .add(KRItems.GOAT_MILK_BUCKET.get())
                .add(KRItems.HORSE_MILK_BUCKET.get())
                .add(KRItems.DONKEY_MILK_BUCKET.get())
                .add(KRItems.CAMEL_MILK_BUCKET.get())
                .add(KRItems.ALPACA_MILK_BUCKET.get())
                .add(KRItems.RABBIT_MILK_BUCKET.get())
                .add(KRItems.PIG_MILK_BUCKET.get());
        tag(Tags.Items.EGGS).add(KRItems.GOLDEN_EGG.get());
    }
}
