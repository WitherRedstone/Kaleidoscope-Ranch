package com.chinaex123.kaleidoscope_ranch.init.tags;

import com.chinaex123.kaleidoscope_ranch.KaleidoscopeRanch;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;

public interface KRItemTags {

    /** 动物饲料 */
    TagKey<Item> ANIMAL_FEED = modItemTag("animal_feed");
    /** 干草窝蛋 */
    TagKey<Item> HAYSTACK_HOLLOW_EGG = modItemTag("haystack_hollow_egg");
    /** 羊奶 */
    TagKey<Item> BUCKETS_MILK_SHEEP = modItemTag("buckets/milk_sheep");
    /** 牛奶 */
    TagKey<Item> BUCKETS_MILK_GOAT = modItemTag("buckets/milk_goat");
    /** 马奶 */
    TagKey<Item> BUCKETS_MILK_HORSE = modItemTag("buckets/milk_horse");
    /** 驴奶 */
    TagKey<Item> BUCKETS_MILK_DONKEY = modItemTag("buckets/milk_donkey");
    /** 骆驼奶 */
    TagKey<Item> BUCKETS_MILK_CAMEL = modItemTag("buckets/milk_camel");
    /** 羊驼奶 */
    TagKey<Item> BUCKETS_MILK_ALPACA = modItemTag("buckets/milk_alpaca");
    /** 兔奶 */
    TagKey<Item> BUCKETS_MILK_RABBIT = modItemTag("buckets/milk_rabbit");
    /** 猪奶 */
    TagKey<Item> BUCKETS_MILK_PIG = modItemTag("buckets/milk_pig");

    /** 盐 */
    TagKey<Item> SALTS = neoforgeItemTag("salts");

    private static TagKey<Item> modItemTag(String name) {
        return TagKey.create(Registries.ITEM, ResourceLocation.fromNamespaceAndPath(KaleidoscopeRanch.MODID, name));
    }
    private static TagKey<Item> neoforgeItemTag(String name) {
        return TagKey.create(Registries.ITEM, ResourceLocation.fromNamespaceAndPath("c", name));
    }
}
