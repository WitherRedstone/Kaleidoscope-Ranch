package com.chinaex123.kaleidoscope_ranch.init.tags;

import com.chinaex123.kaleidoscope_ranch.KaleidoscopeRanch;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.material.Fluid;

public interface KRFluidTags {

    /** 奶 */
    TagKey<Fluid> MILKS = neoforgeItemTag("milks");
    /** 羊奶 */
    TagKey<Fluid> MILKS_SHEEP = neoforgeItemTag("milks/sheep");
    /** 牛奶 */
    TagKey<Fluid> MILKS_GOAT = neoforgeItemTag("milks/goat");
    /** 马奶 */
    TagKey<Fluid> MILKS_HORSE = neoforgeItemTag("milks/horse");
    /** 驴奶 */
    TagKey<Fluid> MILKS_DONKEY = neoforgeItemTag("milks/donkey");
    /** 骆驼奶 */
    TagKey<Fluid> MILKS_CAMEL = neoforgeItemTag("milks/camel");
    /** 羊驼奶 */
    TagKey<Fluid> MILKS_ALPACA = neoforgeItemTag("milks/alpaca");
    /** 兔奶 */
    TagKey<Fluid> MILKS_RABBIT = neoforgeItemTag("milks/rabbit");
    /** 猪奶 */
    TagKey<Fluid> MILKS_PIG = neoforgeItemTag("milks/pig");

    private static TagKey<Fluid> modItemTag(String name) {
        return TagKey.create(Registries.FLUID, ResourceLocation.fromNamespaceAndPath(KaleidoscopeRanch.MODID, name));
    }
    private static TagKey<Fluid> neoforgeItemTag(String name) {
        return TagKey.create(Registries.FLUID, ResourceLocation.fromNamespaceAndPath("c", name));
    }
}
