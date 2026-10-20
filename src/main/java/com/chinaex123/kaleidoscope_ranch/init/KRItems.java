package com.chinaex123.kaleidoscope_ranch.init;

import com.chinaex123.kaleidoscope_ranch.KaleidoscopeRanch;
import com.chinaex123.kaleidoscope_ranch.item.FecesItem;
import net.minecraft.world.item.Item;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;

public interface KRItems {
    DeferredRegister.Items ITEMS = DeferredRegister.createItems(KaleidoscopeRanch.MODID);

    /** 粪便 */
    DeferredItem<Item> FECES = ITEMS.register("feces", () -> new FecesItem(new Item.Properties()));
    /** 盐 */
    DeferredItem<Item> SALT = ITEMS.register("salt", () -> new Item(new Item.Properties()));
    /** 家禽饲料 */
    DeferredItem<Item> POULTRY_FEED = ITEMS.register("poultry_feed", () -> new Item(new Item.Properties()));
    /** 家畜饲料 */
    DeferredItem<Item> LIVESTOCK_FEED = ITEMS.register("livestock_feed", () -> new Item(new Item.Properties()));

    static void register(IEventBus eventBus) {
        ITEMS.register(eventBus);
    }
}
