package com.chinaex123.kaleidoscope_ranch.data;

import com.chinaex123.kaleidoscope_ranch.KaleidoscopeRanch;
import com.chinaex123.kaleidoscope_ranch.init.KRBlocks;
import com.chinaex123.kaleidoscope_ranch.init.KRItems;
import com.chinaex123.kaleidoscope_ranch.util.ModelProviderUtil;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.client.model.generators.ItemModelProvider;
import net.neoforged.neoforge.common.data.ExistingFileHelper;

public class ModItemModelsProvider extends ItemModelProvider {
    public ModItemModelsProvider(PackOutput output, ExistingFileHelper existingFileHelper) {
        super(output, KaleidoscopeRanch.MODID, existingFileHelper);
    }

    @Override
    protected void registerModels() {
        crystalItem(KRBlocks.SMALL_ROSE_SALT_CRYSTAL_BUD.getId());
        crystalItem(KRBlocks.MEDIUM_ROSE_SALT_CRYSTAL_BUD.getId());
        crystalItem(KRBlocks.LARGE_ROSE_SALT_CRYSTAL_BUD.getId());
        crystalItem(KRBlocks.ROSE_SALT_CLUSTER.getId());

        basicItem(KRItems.ROSE_SALT.get());
//        basicItem(KRItems.POULTRY_FEED.get());
//        basicItem(KRItems.LIVESTOCK_FEED.get());
//        basicItem(KRItems.CREATIVE_FEED.get());
        basicItem(KRItems.GOLDEN_EGG.get());

        ModelProviderUtil.bucketFluids(KRItems.SHEEP_MILK_BUCKET, this);
        ModelProviderUtil.bucketFluids(KRItems.GOAT_MILK_BUCKET, this);
        ModelProviderUtil.bucketFluids(KRItems.HORSE_MILK_BUCKET, this);
        ModelProviderUtil.bucketFluids(KRItems.DONKEY_MILK_BUCKET, this);
        ModelProviderUtil.bucketFluids(KRItems.CAMEL_MILK_BUCKET, this);
        ModelProviderUtil.bucketFluids(KRItems.ALPACA_MILK_BUCKET, this);
        ModelProviderUtil.bucketFluids(KRItems.RABBIT_MILK_BUCKET, this);
        ModelProviderUtil.bucketFluids(KRItems.PIG_MILK_BUCKET, this);
    }

    private void crystalItem(ResourceLocation id) {
        withExistingParent(id.getPath(), mcLoc("item/generated"))
                .texture("layer0", modLoc("block/" + id.getPath()));
    }
}