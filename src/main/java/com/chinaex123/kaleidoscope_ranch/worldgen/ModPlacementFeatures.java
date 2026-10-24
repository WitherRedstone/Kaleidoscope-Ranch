package com.chinaex123.kaleidoscope_ranch.worldgen;

import com.chinaex123.kaleidoscope_ranch.KaleidoscopeRanch;
import net.minecraft.core.HolderGetter;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.levelgen.VerticalAnchor;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.placement.*;

public class ModPlacementFeatures {
    public static final ResourceKey<PlacedFeature> ROSE_SALT_GEODE_PLACED = createKey("rose_salt_geode_placed");

    public static void bootstrap(BootstrapContext<PlacedFeature> context) {
        HolderGetter<ConfiguredFeature<?, ?>> configuredFeatures = context.lookup(Registries.CONFIGURED_FEATURE);

        context.register(ROSE_SALT_GEODE_PLACED, new PlacedFeature(
                configuredFeatures.getOrThrow(ModConfiguredFeatures.ROSE_SALT_GEODE),
                java.util.List.of(
//                        CountPlacement.of(1),
                        RarityFilter.onAverageOnceEvery(4),
                        InSquarePlacement.spread(),
                        HeightRangePlacement.uniform(VerticalAnchor.absolute(-50), VerticalAnchor.absolute(-24)),
                        BiomeFilter.biome()
                )
        ));
    }

    public static ResourceKey<PlacedFeature> createKey(String key) {
        return ResourceKey.create(Registries.PLACED_FEATURE, ResourceLocation.fromNamespaceAndPath(KaleidoscopeRanch.MODID, key));
    }
}