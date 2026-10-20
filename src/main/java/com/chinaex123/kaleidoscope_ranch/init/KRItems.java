package com.chinaex123.kaleidoscope_ranch.init;

import com.chinaex123.kaleidoscope_ranch.KaleidoscopeRanch;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;

public interface KRItems {
    DeferredRegister.Items ITEMS = DeferredRegister.createItems(KaleidoscopeRanch.MODID);

    static void register(IEventBus eventBus) {
        ITEMS.register(eventBus);
    }
}
