package com.chinaex123.kaleidoscope_ranch.client;

import com.chinaex123.kaleidoscope_ranch.KaleidoscopeRanch;
import com.chinaex123.kaleidoscope_ranch.init.KRFluids;
import com.chinaex123.kaleidoscope_ranch.init.KRItems;
import net.minecraft.client.color.item.ItemColor;
import net.minecraft.world.item.BucketItem;
import net.minecraft.world.item.Item;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.client.event.RegisterColorHandlersEvent;
import net.neoforged.neoforge.client.extensions.common.RegisterClientExtensionsEvent;
import net.neoforged.neoforge.client.model.DynamicFluidContainerModel;

@Mod(value = KaleidoscopeRanch.MODID, dist = Dist.CLIENT)
public class KRClient {

    public KRClient(IEventBus modEventBus) {
        modEventBus.addListener(RegisterClientExtensionsEvent.class, KRFluids::onRegisterFluidType);
        modEventBus.addListener(RegisterColorHandlersEvent.Item.class, KRClient::registerItemColors);
    }

    private static void registerItemColors(RegisterColorHandlersEvent.Item event) {
        ItemColor fluidColor = new DynamicFluidContainerModel.Colors();

        for (var entry : KRItems.ITEMS.getEntries()) {
            Item item = entry.get();
            if (item instanceof BucketItem) {
                event.register(fluidColor, item);
            }
        }
    }
}
