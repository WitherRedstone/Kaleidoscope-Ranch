package com.chinaex123.kaleidoscope_ranch.init;

import com.chinaex123.kaleidoscope_ranch.KaleidoscopeRanch;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;

public interface KREntityTags {

    /** 管理动物类型 */
    TagKey<EntityType<?>> MANAGED_ANIMAL = modTag("managed_animal");
    /** 家禽 */
    TagKey<EntityType<?>> POULTRY = modTag("poultry");
    /** 家畜 */
    TagKey<EntityType<?>> LIVESTOCK = modTag("livestock");

    private static TagKey<EntityType<?>> modTag(String name) {
        return TagKey.create(Registries.ENTITY_TYPE, ResourceLocation.fromNamespaceAndPath(KaleidoscopeRanch.MODID, name));
    }

    static boolean isManagedAnimal(Entity entity) {
        EntityType<?> type = entity.getType();
        return type.is(POULTRY) || type.is(LIVESTOCK);
    }
}
