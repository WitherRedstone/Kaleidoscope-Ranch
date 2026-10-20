package com.chinaex123.kaleidoscope_ranch;

import com.chinaex123.kaleidoscope_ranch.init.KRBlocks;
import com.chinaex123.kaleidoscope_ranch.init.KRCreativeTabs;
import com.chinaex123.kaleidoscope_ranch.init.KRItems;
import com.chinaex123.kaleidoscope_ranch.network.KRPackets;
import org.slf4j.Logger;

import com.mojang.logging.LogUtils;

import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.ModContainer;

@Mod(KaleidoscopeRanch.MODID)
public class KaleidoscopeRanch {
    public static final String MODID = "kaleidoscope_ranch";
    public static final Logger LOGGER = LogUtils.getLogger();

    public KaleidoscopeRanch(IEventBus modEventBus, ModContainer modContainer) {
        KRBlocks.register(modEventBus);
        KRItems.register(modEventBus);
        KRCreativeTabs.register(modEventBus);
        modEventBus.register(KRPackets.class);
    }
}