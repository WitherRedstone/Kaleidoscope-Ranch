package com.chinaex123.kaleidoscope_ranch.worldgen;

import com.chinaex123.kaleidoscope_ranch.KaleidoscopeRanch;
import com.chinaex123.kaleidoscope_ranch.init.KRBlocks;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.valueproviders.UniformInt;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.levelgen.GeodeBlockSettings;
import net.minecraft.world.level.levelgen.GeodeCrackSettings;
import net.minecraft.world.level.levelgen.GeodeLayerSettings;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.configurations.GeodeConfiguration;
import net.minecraft.world.level.levelgen.feature.stateproviders.BlockStateProvider;

import java.util.List;

public class ModConfiguredFeatures {
    public static final ResourceKey<ConfiguredFeature<?, ?>> ROSE_SALT_GEODE = createKey("rose_salt_geode");

    public static void bootstrap(BootstrapContext<ConfiguredFeature<?, ?>> context) {
        context.register(ROSE_SALT_GEODE, new ConfiguredFeature<>(Feature.GEODE, new GeodeConfiguration(
                new GeodeBlockSettings(
                        BlockStateProvider.simple(Blocks.AIR),                                    // 填充层提供器
                        BlockStateProvider.simple(KRBlocks.ROSE_SALT_CRYSTAL.get()),              // 内层提供器
                        BlockStateProvider.simple(KRBlocks.ROSE_SALT_CRYSTAL_CORE.get()),         // 交替内层提供器
                        BlockStateProvider.simple(Blocks.CALCITE),                                // 中层提供器
                        BlockStateProvider.simple(Blocks.SMOOTH_BASALT),                          // 外层提供器
                        List.of(                                                                  // 内部放置物列表
                                KRBlocks.SMALL_ROSE_SALT_CRYSTAL_BUD.get().defaultBlockState(),
                                KRBlocks.MEDIUM_ROSE_SALT_CRYSTAL_BUD.get().defaultBlockState(),
                                KRBlocks.LARGE_ROSE_SALT_CRYSTAL_BUD.get().defaultBlockState(),
                                KRBlocks.ROSE_SALT_CLUSTER.get().defaultBlockState()
                        ),
                        BlockTags.FEATURES_CANNOT_REPLACE,                                        // 不可替换标签
                        BlockTags.GEODE_INVALID_BLOCKS                                            // 无效方块标签
                ),
//                new GeodeLayerSettings(1.7, 2.2, 3.2, 4.2),
                new GeodeLayerSettings(0.85, 1.1, 1.6, 2.1), // 四层球壳的相对半径
                new GeodeCrackSettings(0.95, 2.0, 2), // 裂纹概率/大小/数量
                0.35,   // 内壁"潜在附着点"使用概率
                0.12,  // 第 0 层被"交替层"替换的概率
                true,   // 附着物是否必须长在交替层上
                UniformInt.of(3, 4),    // 外墙额外厚度
                UniformInt.of(2, 3),    // 内部晶体分布点数
                UniformInt.of(1, 2),    // 内部附着物放置次数
                -12,    // 生成范围最小偏移
                12, // 生成范围最大偏移
                0.03,   // 噪声扰动强度
                1   // 噪声偏移
        )));
    }

    public static ResourceKey<ConfiguredFeature<?, ?>> createKey(String name) {
        return ResourceKey.create(Registries.CONFIGURED_FEATURE,
                ResourceLocation.fromNamespaceAndPath(KaleidoscopeRanch.MODID, name));
    }
}