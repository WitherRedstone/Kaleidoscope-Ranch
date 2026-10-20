package com.chinaex123.kaleidoscope_ranch.compat.jade.animal;

import com.chinaex123.kaleidoscope_ranch.compat.jade.KRJadePlugin;
import com.chinaex123.kaleidoscope_ranch.data.animal.hunger.AnimalHunger;
import com.chinaex123.kaleidoscope_ranch.data.animal.mood.AnimalMood;
import com.chinaex123.kaleidoscope_ranch.init.KREntityTags;
import com.chinaex123.kaleidoscope_ranch.network.ClientAnimalMoodData;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.animal.Animal;
import snownee.jade.api.EntityAccessor;
import snownee.jade.api.IEntityComponentProvider;
import snownee.jade.api.ITooltip;
import snownee.jade.api.config.IPluginConfig;

import java.util.UUID;

public enum AnimalMoodComponentProvider implements IEntityComponentProvider {
    INSTANCE;

    @Override
    public void appendTooltip(ITooltip tooltip, EntityAccessor accessor, IPluginConfig pluginConfig) {
        if (!(accessor.getEntity() instanceof Animal animal)) return;
        if (!KREntityTags.isManagedAnimal(animal)) return;

        UUID uuid = accessor.getEntity().getUUID();
        ClientAnimalMoodData clientData = ClientAnimalMoodData.getInstance();
        int moodScore = clientData.getAnimalMoodScore(uuid);
        int color = AnimalMood.getDisplayColor(moodScore);

        tooltip.add(Component.translatable("jade.plugin_kaleidoscope_ranch.animal_mood", moodScore).withStyle(style -> style.withColor(color)));

        float hungerValue = clientData.getAnimalHungerValue(uuid);
        int hungerColor = AnimalHunger.getDisplayColor(hungerValue);
        String hungerText = String.format("%.1f", hungerValue);
        tooltip.add(Component.translatable("jade.plugin_kaleidoscope_ranch.animal_hunger", hungerText).withStyle(style -> style.withColor(hungerColor)));

        boolean petToday = clientData.hasBeenPetToday(uuid);
        String petKey = petToday
                ? "jade.plugin_kaleidoscope_ranch.pet_status_yes"
                : "jade.plugin_kaleidoscope_ranch.pet_status_no";
        tooltip.add(Component.translatable("jade.plugin_kaleidoscope_ranch.pet_status", Component.translatable(petKey)));
    }

    @Override
    public ResourceLocation getUid() {
        return KRJadePlugin.ANIMAL_MOOD;
    }
}