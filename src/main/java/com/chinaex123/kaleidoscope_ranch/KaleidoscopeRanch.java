package com.chinaex123.kaleidoscope_ranch;

import com.chinaex123.kaleidoscope_ranch.config.KRConfig;
import com.chinaex123.kaleidoscope_ranch.init.KRBlockEntityTypes;
import com.chinaex123.kaleidoscope_ranch.init.KRBlocks;
import com.chinaex123.kaleidoscope_ranch.init.KRCreativeTabs;
import com.chinaex123.kaleidoscope_ranch.init.KRItems;
import com.chinaex123.kaleidoscope_ranch.network.KRPackets;
import com.mojang.logging.LogUtils;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig;
import org.slf4j.Logger;

@Mod(KaleidoscopeRanch.MODID)
public class KaleidoscopeRanch {
    public static final String MODID = "kaleidoscope_ranch";
    public static final Logger LOGGER = LogUtils.getLogger();

    public KaleidoscopeRanch(IEventBus modEventBus, ModContainer modContainer) {
        modContainer.registerConfig(ModConfig.Type.COMMON, KRConfig.SPEC);
        KRBlocks.register(modEventBus);
        KRItems.register(modEventBus);
        KRCreativeTabs.register(modEventBus);
        KRBlockEntityTypes.register(modEventBus);
        modEventBus.register(KRPackets.class);
    }

    public static ResourceLocation id(String name) {
        return ResourceLocation.fromNamespaceAndPath(MODID, name);
    }
}