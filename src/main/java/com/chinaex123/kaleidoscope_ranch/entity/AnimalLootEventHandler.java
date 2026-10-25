package com.chinaex123.kaleidoscope_ranch.entity;

import com.chinaex123.kaleidoscope_ranch.KaleidoscopeRanch;
import com.chinaex123.kaleidoscope_ranch.config.KRConfig;
import com.chinaex123.kaleidoscope_ranch.data.animal.favor.AnimalFavorSavedData;
import com.chinaex123.kaleidoscope_ranch.data.animal.mood.AnimalMoodSavedData;
import com.chinaex123.kaleidoscope_ranch.init.tags.KREntityTags;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.living.LivingDropsEvent;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@EventBusSubscriber(modid = KaleidoscopeRanch.MODID)
public class AnimalLootEventHandler {

    @SubscribeEvent
    public static void onLivingDrops(LivingDropsEvent event) {
        if (!(event.getEntity() instanceof Animal animal)) return;
        if (!KREntityTags.isManagedAnimal(animal)) return;
        if (animal.level().isClientSide()) return;

        ServerLevel level = (ServerLevel) animal.level();
        UUID uuid = animal.getUUID();

        AnimalMoodSavedData moodData = AnimalMoodEventHandler.getAnimalMoodData(level);
        AnimalFavorSavedData favorData = AnimalFavorEventHandler.getAnimalFavorData(level);

        double mood = moodData.getAnimalMoodScore(uuid);
        int favorLevel = favorData.getAnimalFavorLevel(uuid);

        double multiplier = resolveMultiplier(mood, favorLevel);
        if (multiplier <= 0) return;

        int fullCopies = (int) multiplier;
        double fraction = multiplier - fullCopies;

        List<ItemStack> originalDrops = new ArrayList<>();
        for (ItemEntity itemEntity : event.getDrops()) {
            originalDrops.add(itemEntity.getItem().copy());
        }

        for (int i = 0; i < fullCopies; i++) {
            for (ItemStack stack : originalDrops) {
                event.getDrops().add(new ItemEntity(
                        level, animal.getX(), animal.getY(), animal.getZ(), stack.copy()));
            }
        }

        if (fraction > 0 && level.random.nextDouble() < fraction) {
            for (ItemStack stack : originalDrops) {
                event.getDrops().add(new ItemEntity(
                        level, animal.getX(), animal.getY(), animal.getZ(), stack.copy()));
            }
        }
    }

    private static double resolveMultiplier(double mood, int favorLevel) {
        if (mood >= KRConfig.LOOT_TIER3_MOOD_THRESHOLD.get() && favorLevel >= KRConfig.LOOT_TIER3_FAVOR_THRESHOLD.get()) {
            return KRConfig.LOOT_TIER3_MULTIPLIER.get();
        }
        if (mood >= KRConfig.LOOT_TIER2_MOOD_THRESHOLD.get() && favorLevel >= KRConfig.LOOT_TIER2_FAVOR_THRESHOLD.get()) {
            return KRConfig.LOOT_TIER2_MULTIPLIER.get();
        }
        if (mood >= KRConfig.LOOT_TIER1_MOOD_THRESHOLD.get() && favorLevel >= KRConfig.LOOT_TIER1_FAVOR_THRESHOLD.get()) {
            return KRConfig.LOOT_TIER1_MULTIPLIER.get();
        }
        return 0;
    }
}