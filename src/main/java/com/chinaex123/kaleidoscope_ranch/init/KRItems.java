package com.chinaex123.kaleidoscope_ranch.init;

import com.chinaex123.kaleidoscope_ranch.KaleidoscopeRanch;
import com.chinaex123.kaleidoscope_ranch.item.CreativeFeed;
import com.chinaex123.kaleidoscope_ranch.item.FecesItem;
import net.minecraft.world.item.BucketItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;

public interface KRItems {
    DeferredRegister.Items ITEMS = DeferredRegister.createItems(KaleidoscopeRanch.MODID);
    Item.Properties BUCKET = new Item.Properties().stacksTo(1).craftRemainder(Items.BUCKET);

    /** 粪便 */
    DeferredItem<Item> FECES = ITEMS.register("feces", () -> new FecesItem(new Item.Properties()));
    /** 玫瑰盐 */
    DeferredItem<Item> ROSE_SALT = ITEMS.register("rose_salt", () -> new Item(new Item.Properties()));
    /** 家禽饲料 */
    DeferredItem<Item> POULTRY_FEED = ITEMS.register("poultry_feed", () -> new Item(new Item.Properties()));
    /** 家畜饲料 */
    DeferredItem<Item> LIVESTOCK_FEED = ITEMS.register("livestock_feed", () -> new Item(new Item.Properties()));
    /** 创造饲料 */
    DeferredItem<Item> CREATIVE_FEED = ITEMS.register("creative_feed", () -> new CreativeFeed(new Item.Properties()));
    /** 金蛋 */
    DeferredItem<Item> GOLDEN_EGG = ITEMS.register("golden_egg", () -> new Item(new Item.Properties()));

    /** 羊奶桶 */
    DeferredItem<BucketItem> SHEEP_MILK_BUCKET = ITEMS.register("sheep_milk_bucket", () -> new BucketItem(KRFluids.SHEEP_MILK.source.get(), BUCKET));
    /** 山羊奶桶 */
    DeferredItem<BucketItem> GOAT_MILK_BUCKET = ITEMS.register("goat_milk_bucket", () -> new BucketItem(KRFluids.GOAT_MILK.source.get(), BUCKET));
    /** 马奶桶 */
    DeferredItem<BucketItem> HORSE_MILK_BUCKET = ITEMS.register("horse_milk_bucket", () -> new BucketItem(KRFluids.HORSE_MILK.source.get(), BUCKET));
    /** 驴奶桶 */
    DeferredItem<BucketItem> DONKEY_MILK_BUCKET = ITEMS.register("donkey_milk_bucket", () -> new BucketItem(KRFluids.DONKEY_MILK.source.get(), BUCKET));
    /** 骆驼奶桶 */
    DeferredItem<BucketItem> CAMEL_MILK_BUCKET = ITEMS.register("camel_milk_bucket", () -> new BucketItem(KRFluids.CAMEL_MILK.source.get(), BUCKET));
    /** 羊驼奶桶 */
    DeferredItem<BucketItem> ALPACA_MILK_BUCKET = ITEMS.register("alpaca_milk_bucket", () -> new BucketItem(KRFluids.ALPACA_MILK.source.get(), BUCKET));
    /** 兔奶桶 */
    DeferredItem<BucketItem> RABBIT_MILK_BUCKET = ITEMS.register("rabbit_milk_bucket", () -> new BucketItem(KRFluids.RABBIT_MILK.source.get(), BUCKET));
    /** 猪奶桶 */
    DeferredItem<BucketItem> PIG_MILK_BUCKET = ITEMS.register("pig_milk_bucket", () -> new BucketItem(KRFluids.PIG_MILK.source.get(), BUCKET));

    static void register(IEventBus eventBus) {
        ITEMS.register(eventBus);
    }
}
