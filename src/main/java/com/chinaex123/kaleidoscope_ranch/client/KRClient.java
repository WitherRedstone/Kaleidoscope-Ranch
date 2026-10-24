package com.chinaex123.kaleidoscope_ranch.client;

import com.chinaex123.kaleidoscope_ranch.KaleidoscopeRanch;
import com.chinaex123.kaleidoscope_ranch.blockentity.HaystackHollowBlockEntity;
import com.chinaex123.kaleidoscope_ranch.client.renderer.HaystackHollowRenderer;
import com.chinaex123.kaleidoscope_ranch.init.KRBlockEntityTypes;
import com.chinaex123.kaleidoscope_ranch.init.KRFluids;
import com.chinaex123.kaleidoscope_ranch.init.KRItems;
import net.minecraft.client.color.item.ItemColor;
import net.minecraft.world.item.BucketItem;
import net.minecraft.world.item.Item;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;
import net.neoforged.neoforge.client.event.RegisterColorHandlersEvent;
import net.neoforged.neoforge.client.extensions.common.RegisterClientExtensionsEvent;
import net.neoforged.neoforge.client.model.DynamicFluidContainerModel;
import net.neoforged.neoforge.event.entity.EntityJoinLevelEvent;

@Mod(value = KaleidoscopeRanch.MODID, dist = Dist.CLIENT)
public class KRClient {

    public KRClient(IEventBus modEventBus) {
        modEventBus.addListener(RegisterClientExtensionsEvent.class, KRFluids::onRegisterFluidType);
        modEventBus.addListener(RegisterColorHandlersEvent.Item.class, KRClient::registerItemColors);
        modEventBus.addListener(EntityRenderersEvent.RegisterRenderers.class, KRClient::registerRenderers);
    }

    private static void registerRenderers(EntityRenderersEvent.RegisterRenderers event) {
        event.registerBlockEntityRenderer(KRBlockEntityTypes.HAYSTACK_HOLLOW.get(), HaystackHollowRenderer::new);
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