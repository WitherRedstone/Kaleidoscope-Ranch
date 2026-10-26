package com.chinaex123.kaleidoscope_ranch.compat.jade.animal;

import com.chinaex123.kaleidoscope_ranch.compat.jade.KRJadePlugin;
import com.chinaex123.kaleidoscope_ranch.data.animal.favor.AnimalFavor;
import com.chinaex123.kaleidoscope_ranch.data.animal.hunger.AnimalHunger;
import com.chinaex123.kaleidoscope_ranch.data.animal.mood.AnimalMood;
import com.chinaex123.kaleidoscope_ranch.init.tags.KREntityTags;
import com.chinaex123.kaleidoscope_ranch.network.ClientAnimalData;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.animal.Cow;
import net.minecraft.world.entity.animal.Pig;
import net.minecraft.world.entity.animal.Sheep;
import net.minecraft.world.entity.animal.camel.Camel;
import net.minecraft.world.entity.animal.horse.Donkey;
import net.minecraft.world.entity.animal.horse.Horse;
import net.minecraft.world.entity.animal.horse.Llama;
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
        ClientAnimalData clientData = ClientAnimalData.getInstance();
        double moodScore = clientData.getAnimalMoodScore(uuid);
        int color = AnimalMood.getDisplayColor(moodScore);

        if (clientData.isWild(uuid)) {
            tooltip.add(Component.translatable("jade.plugin_kaleidoscope_ranch.wild_status")
                    .withStyle(style -> style.withColor(0xFF5555)));
        }
        String moodText = String.format("%.1f", moodScore);
        tooltip.add(Component.translatable("jade.plugin_kaleidoscope_ranch.animal_mood", Component.literal(moodText).withStyle(style -> style.withColor(color)))
                .withStyle(style -> style.withColor(0xAAAAAA)));

        float hungerValue = clientData.getAnimalHungerValue(uuid);
        int hungerColor = AnimalHunger.getDisplayColor(hungerValue);
        String hungerText = String.format("%.1f", hungerValue);
        tooltip.add(Component.translatable("jade.plugin_kaleidoscope_ranch.animal_hunger", Component.literal(hungerText).withStyle(style -> style.withColor(hungerColor)))
                .withStyle(style -> style.withColor(0xAAAAAA)));

        int favorExp = clientData.getAnimalFavorExp(uuid);
        int favorLevel = AnimalFavor.expToLevel(favorExp);
        int favorColor = AnimalFavor.getDisplayColor(favorLevel);
        String favorText = "Lv." + favorLevel;
        tooltip.add(Component.translatable("jade.plugin_kaleidoscope_ranch.animal_favor", Component.literal(favorText).withStyle(style -> style.withColor(favorColor)))
                .withStyle(style -> style.withColor(0xAAAAAA)));

        if (favorLevel < AnimalFavor.MAX_LEVEL) {
            int expNeeded = AnimalFavor.getLevelUpExp(favorLevel);
            int expInCurrentLevel = favorExp - AnimalFavor.levelToExp(favorLevel);
            String expText = expInCurrentLevel + "/" + expNeeded;
            tooltip.add(Component.translatable("jade.plugin_kaleidoscope_ranch.animal_favor_exp", Component.literal(expText).withStyle(style -> style.withColor(favorColor)))
                    .withStyle(style -> style.withColor(0xAAAAAA)));
        }

        boolean petToday = clientData.hasBeenPetToday(uuid);
        String petKey = petToday
                ? "jade.plugin_kaleidoscope_ranch.pet_status_yes"
                : "jade.plugin_kaleidoscope_ranch.pet_status_no";
        int petColor = petToday ? 0x55FF55 : 0xAAAAAA;
        tooltip.add(Component.translatable("jade.plugin_kaleidoscope_ranch.pet_status", Component.translatable(petKey)
                .withStyle(style -> style.withColor(petColor))));

        if ((animal instanceof Pig || animal instanceof Cow || animal instanceof Sheep || animal instanceof Horse || animal instanceof Donkey || animal instanceof Llama || animal instanceof Camel) && clientData.hasMilkData(uuid)) {
            double milkAmount = clientData.getAnimalMilkAmount(uuid);
            double milkMaxCapacity = clientData.getAnimalMilkDailyMax(uuid);
            int milkColor = milkAmount >= milkMaxCapacity ? 0x55FF55 : 0xAAAAAA;
            String milkStr = String.format("%.1f/%.1fmb", milkAmount, milkMaxCapacity);
            tooltip.add(Component.translatable("jade.plugin_kaleidoscope_ranch.animal_milk", Component.literal(milkStr).withStyle(style -> style.withColor(milkColor)))
                    .withStyle(style -> style.withColor(0xAAAAAA)));
        }

        long currentGameTime = getClientGameTime();
        if (clientData.hasFeedCooldown(uuid, currentGameTime)) {
            long remainingTicks = clientData.getFeedCooldownRemaining(uuid, currentGameTime);
            String timeStr = formatTickTime(remainingTicks);
            tooltip.add(Component.translatable("jade.plugin_kaleidoscope_ranch.feed_cooldown", Component.literal(timeStr).withStyle(style -> style.withColor(0xFFFFFF)))
                    .withStyle(style -> style.withColor(0xAAAAAA)));
        }
        if (clientData.hasTroughFeedCooldown(uuid, currentGameTime)) {
            long remainingTicks = clientData.getTroughFeedCooldownRemaining(uuid, currentGameTime);
            String timeStr = formatTickTime(remainingTicks);
            tooltip.add(Component.translatable("jade.plugin_kaleidoscope_ranch.trough_feed_cooldown", Component.literal(timeStr).withStyle(style -> style.withColor(0xFFFFFF)))
                    .withStyle(style -> style.withColor(0xAAAAAA)));
        }

        if (animal instanceof Sheep && clientData.hasShearCooldown(uuid, currentGameTime)) {
            long remainingTicks = clientData.getShearCooldownRemaining(uuid, currentGameTime);
            String timeStr = formatTickTime(remainingTicks);
            tooltip.add(Component.translatable("jade.plugin_kaleidoscope_ranch.shear_cooldown", Component.literal(timeStr).withStyle(style -> style.withColor(0xFFFFFF)))
                    .withStyle(style -> style.withColor(0xAAAAAA)));
        }
    }

    private static String formatTickTime(long ticks) {
        long totalSeconds = ticks / 20;
        long hours = totalSeconds / 3600;
        long minutes = (totalSeconds % 3600) / 60;
        long seconds = totalSeconds % 60;
        StringBuilder sb = new StringBuilder();
        if (hours > 0) sb.append(Component.translatable("jade.plugin_kaleidoscope_ranch.time_hour", hours).getString());
        if (minutes > 0 || hours > 0) sb.append(Component.translatable("jade.plugin_kaleidoscope_ranch.time_minute", minutes).getString());
        sb.append(Component.translatable("jade.plugin_kaleidoscope_ranch.time_second", seconds).getString());
        return sb.toString();
    }

    private static long getClientGameTime() {
        Minecraft mc = Minecraft.getInstance();
        return mc.level != null ? mc.level.getGameTime() : 0L;
    }

    @Override
    public ResourceLocation getUid() {
        return KRJadePlugin.ANIMAL_MOOD;
    }
}