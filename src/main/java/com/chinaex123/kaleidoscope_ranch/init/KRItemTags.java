package com.chinaex123.kaleidoscope_ranch.init;

import com.chinaex123.kaleidoscope_ranch.KaleidoscopeRanch;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;

public interface KRItemTags {

    /** 动物饲料 */
    TagKey<Item> ANIMAL_FEED = modItemTag("animal_feed");
    /** 盐 */
    TagKey<Item> SALTS = neoforgeItemTag("salts");

    private static TagKey<Item> modItemTag(String name) {
        return TagKey.create(Registries.ITEM, ResourceLocation.fromNamespaceAndPath(KaleidoscopeRanch.MODID, name));
    }
    private static TagKey<Item> neoforgeItemTag(String name) {
        return TagKey.create(Registries.ITEM, ResourceLocation.fromNamespaceAndPath("c", name));
    }
}
