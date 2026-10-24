package com.chinaex123.kaleidoscope_ranch.init.tags;

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

    /** 好感度_泥巴 */
    TagKey<EntityType<?>> FAVOR_MUD = modTag("favor_mud");

    /** 舔舐盐块的动物 */
    TagKey<EntityType<?>> ROSE_SALT_ANIMAL = modTag("rose_salt_animal");

    private static TagKey<EntityType<?>> modTag(String name) {
        return TagKey.create(Registries.ENTITY_TYPE, ResourceLocation.fromNamespaceAndPath(KaleidoscopeRanch.MODID, name));
    }

    static boolean isManagedAnimal(Entity entity) {
        EntityType<?> type = entity.getType();
        return type.is(POULTRY) || type.is(LIVESTOCK);
    }
}
