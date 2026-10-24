package com.chinaex123.kaleidoscope_ranch.data.tags;

import com.chinaex123.kaleidoscope_ranch.KaleidoscopeRanch;
import com.chinaex123.kaleidoscope_ranch.init.KRItems;
import com.chinaex123.kaleidoscope_ranch.init.tags.KRFluidTags;
import com.chinaex123.kaleidoscope_ranch.init.KRFluids;
import com.chinaex123.kaleidoscope_ranch.init.tags.KRItemTags;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.FluidTagsProvider;
import net.neoforged.neoforge.common.data.ExistingFileHelper;

import java.util.concurrent.CompletableFuture;

public class ModFluidTagsProvider extends FluidTagsProvider {
    public ModFluidTagsProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider,
                                ExistingFileHelper existingFileHelper) {
        super(output, lookupProvider, KaleidoscopeRanch.MODID, existingFileHelper);
    }

    @Override
    protected void addTags(HolderLookup.Provider provider) {

        tag(KRFluidTags.MILKS)
                .add(KRFluids.SHEEP_MILK.source.getKey())
                .add(KRFluids.GOAT_MILK.source.getKey())
                .add(KRFluids.HORSE_MILK.source.getKey())
                .add(KRFluids.DONKEY_MILK.source.getKey())
                .add(KRFluids.CAMEL_MILK.source.getKey())
                .add(KRFluids.ALPACA_MILK.source.getKey())
                .add(KRFluids.RABBIT_MILK.source.getKey())
                .add(KRFluids.PIG_MILK.source.getKey());
        tag(KRFluidTags.MILKS_SHEEP).add(KRFluids.SHEEP_MILK.source.getKey());
        tag(KRFluidTags.MILKS_GOAT).add(KRFluids.GOAT_MILK.source.getKey());
        tag(KRFluidTags.MILKS_HORSE).add(KRFluids.HORSE_MILK.source.getKey());
        tag(KRFluidTags.MILKS_DONKEY).add(KRFluids.DONKEY_MILK.source.getKey());
        tag(KRFluidTags.MILKS_CAMEL).add(KRFluids.CAMEL_MILK.source.getKey());
        tag(KRFluidTags.MILKS_ALPACA).add(KRFluids.ALPACA_MILK.source.getKey());
        tag(KRFluidTags.MILKS_RABBIT).add(KRFluids.RABBIT_MILK.source.getKey());
        tag(KRFluidTags.MILKS_PIG).add(KRFluids.PIG_MILK.source.getKey());

    }
}