package com.chinaex123.kaleidoscope_ranch.data.tags;

import com.chinaex123.kaleidoscope_ranch.KaleidoscopeRanch;
import com.chinaex123.kaleidoscope_ranch.init.KREntityTags;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.EntityTypeTagsProvider;
import net.minecraft.world.entity.EntityType;
import net.neoforged.neoforge.common.data.ExistingFileHelper;

import java.util.concurrent.CompletableFuture;

public class ModEntityTagsProvider extends EntityTypeTagsProvider {

    public ModEntityTagsProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider, ExistingFileHelper existingFileHelper) {
        super(output, lookupProvider, KaleidoscopeRanch.MODID, existingFileHelper);
    }

    @Override
    protected void addTags(HolderLookup.Provider provider) {

        tag(KREntityTags.POULTRY)
                .add(EntityType.CHICKEN);
//                .addOptional(ResourceLocation.fromNamespaceAndPath("twilightforest", "raspberry"));
        tag(KREntityTags.LIVESTOCK)
                .add(EntityType.PIG)
                .add(EntityType.COW)
                .add(EntityType.SHEEP)
                .add(EntityType.HORSE)
                .add(EntityType.DONKEY)
//                .add(EntityType.MULE)
                .add(EntityType.LLAMA)
//                .add(EntityType.TRADER_LLAMA)
                .add(EntityType.CAMEL)
                .add(EntityType.GOAT)
                .add(EntityType.RABBIT)
                .add(EntityType.MOOSHROOM);
        tag(KREntityTags.MANAGED_ANIMAL)
                .add(EntityType.CHICKEN)
                .add(EntityType.PIG)
                .add(EntityType.COW)
                .add(EntityType.SHEEP)
                .add(EntityType.HORSE)
                .add(EntityType.DONKEY)
//                .add(EntityType.MULE)
                .add(EntityType.LLAMA)
//                .add(EntityType.TRADER_LLAMA)
                .add(EntityType.CAMEL)
                .add(EntityType.GOAT)
                .add(EntityType.RABBIT)
                .add(EntityType.MOOSHROOM);
    }
}
