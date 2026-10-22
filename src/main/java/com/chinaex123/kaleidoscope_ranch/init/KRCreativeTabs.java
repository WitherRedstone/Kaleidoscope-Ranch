package com.chinaex123.kaleidoscope_ranch.init;

import com.chinaex123.kaleidoscope_ranch.KaleidoscopeRanch;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

public class KRCreativeTabs {
    public static final DeferredRegister<CreativeModeTab> CREATIVE_MODE_TAB =
            DeferredRegister.create(Registries.CREATIVE_MODE_TAB, KaleidoscopeRanch.MODID);

    public static final Supplier<CreativeModeTab> KALEIDOSCOPE_RANCH_TAB =
            CREATIVE_MODE_TAB.register("kaleidoscope_ranch_tab", () -> CreativeModeTab.builder()
                    .icon(() -> new ItemStack(KRBlocks.SALT_BLOCK.get()))
                    .title(Component.translatable("itemGroup.kaleidoscope_ranch"))
                    .displayItems((parameters, output) -> {

                        output.accept(KRBlocks.SALT_BLOCK.get());
                        output.accept(KRBlocks.FEEDING_TROUGH.get());
                        output.accept(KRBlocks.HAYSTACK_HOLLOW.get());

                        output.accept(KRItems.SALT.get());
                        output.accept(KRItems.FECES.get());
                        output.accept(KRItems.POULTRY_FEED.get());
                        output.accept(KRItems.LIVESTOCK_FEED.get());
                        output.accept(KRItems.CREATIVE_FEED.get());

                    })
                    .build());

    public static void register(IEventBus eventBus) {
        CREATIVE_MODE_TAB.register(eventBus);
    }
}
