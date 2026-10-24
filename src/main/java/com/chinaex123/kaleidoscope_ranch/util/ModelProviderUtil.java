package com.chinaex123.kaleidoscope_ranch.util;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.BucketItem;
import net.neoforged.neoforge.client.model.generators.ItemModelProvider;
import net.neoforged.neoforge.client.model.generators.loaders.DynamicFluidContainerModelBuilder;
import net.neoforged.neoforge.registries.DeferredItem;

public class ModelProviderUtil {

    public static void bucketFluids(
            DeferredItem<? extends BucketItem> item,
            ItemModelProvider provider
    ) {
        provider.withExistingParent(
                item.getId().getPath(),
                ResourceLocation.parse("neoforge:item/bucket")
        ).customLoader((builder, helper) -> DynamicFluidContainerModelBuilder.begin(builder, helper)
                .fluid(item.get().content)
                .flipGas(false));
    }

    public static void bucketGas(
            DeferredItem<? extends BucketItem> item,
            ItemModelProvider provider
    ) {
        provider.withExistingParent(
                item.getId().getPath(),
                ResourceLocation.parse("neoforge:item/bucket_drip")
        ).customLoader((builder, helper) -> DynamicFluidContainerModelBuilder.begin(builder, helper)
                .fluid(item.get().content)
                .flipGas(true));
    }
}
