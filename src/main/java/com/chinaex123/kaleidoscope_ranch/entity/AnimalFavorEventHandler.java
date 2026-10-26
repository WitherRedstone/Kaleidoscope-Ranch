package com.chinaex123.kaleidoscope_ranch.entity;

import com.chinaex123.kaleidoscope_ranch.KaleidoscopeRanch;
import com.chinaex123.kaleidoscope_ranch.config.KRConfig;
import com.chinaex123.kaleidoscope_ranch.data.animal.favor.AnimalFavorSavedData;
import com.chinaex123.kaleidoscope_ranch.data.animal.favor.FavorDecline;
import com.chinaex123.kaleidoscope_ranch.data.animal.mood.AnimalMoodSavedData;
import com.chinaex123.kaleidoscope_ranch.entity.ai.KRFleeFromPlayerGoal;
import com.chinaex123.kaleidoscope_ranch.init.tags.KREntityTags;
import com.chinaex123.kaleidoscope_ranch.network.AnimalFavorSyncPayload;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.goal.WrappedGoal;
import net.minecraft.world.entity.animal.Animal;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.EntityJoinLevelEvent;
import net.neoforged.neoforge.event.entity.living.LivingDamageEvent;
import net.neoforged.neoforge.event.entity.living.LivingDeathEvent;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;
import net.neoforged.neoforge.network.PacketDistributor;

import java.util.Map;
import java.util.UUID;

@EventBusSubscriber(modid = KaleidoscopeRanch.MODID)
public class AnimalFavorEventHandler {

    @SubscribeEvent
    public static void onAnimalJoinLevel(EntityJoinLevelEvent event) {
        if (event.getLevel().isClientSide()) return;
        if (event.getEntity() instanceof Animal animal && KREntityTags.isManagedAnimal(animal)) {
            ServerLevel level = (ServerLevel) event.getLevel();
            AnimalFavorSavedData data = getAnimalFavorData(level);
            if (!data.hasAnimalFavorData(animal.getUUID())) {
                data.setAnimalFavorExp(animal.getUUID(), 0);
            }
            syncFavorToTrackingPlayers(animal, data);
        }
    }

    @SubscribeEvent
    public static void onAnimalHurt(LivingDamageEvent.Post event) {
        LivingEntity entity = event.getEntity();
        if (entity.level().isClientSide()) return;
        if (!(entity instanceof Animal animal)) return;
        if (!KREntityTags.isManagedAnimal(animal)) return;

        ServerLevel level = (ServerLevel) entity.level();
        addFavorExp(level, animal, -FavorDecline.calcHurtPenaltyExp(level.random));
    }

    @SubscribeEvent
    public static void onAnimalDeath(LivingDeathEvent event) {
        if (event.getEntity().level().isClientSide()) return;
        if (!(event.getEntity() instanceof Animal animal)) return;
        if (!KREntityTags.isManagedAnimal(animal)) return;

        ServerLevel level = (ServerLevel) animal.level();
        AnimalFavorSavedData data = getAnimalFavorData(level);
        if (!data.hasAnimalFavorData(animal.getUUID())) return;

        data.removeAnimalFavor(animal.getUUID());
    }

    @SubscribeEvent
    public static void onStartTracking(PlayerEvent.StartTracking event) {
        if (event.getEntity().level().isClientSide()) return;
        if (!(event.getTarget() instanceof Animal animal)) return;
        if (!KREntityTags.isManagedAnimal(animal)) return;
        if (!(event.getEntity() instanceof ServerPlayer player)) return;

        ServerLevel level = (ServerLevel) animal.level();
        AnimalFavorSavedData data = getAnimalFavorData(level);
        if (!data.hasAnimalFavorData(animal.getUUID())) return;

        syncFavorToPlayer(player, animal, data);
    }

    public static void addFavorExp(ServerLevel level, Animal animal, int delta) {
        if (delta == 0) return;
        AnimalFavorSavedData data = getAnimalFavorData(level);
        if (!data.hasAnimalFavorData(animal.getUUID())) return;
        if (data.applyFavorExpDelta(animal.getUUID(), delta)) {
            syncFavorToTrackingPlayers(animal, data);
            checkTameFromWild(level, animal, data);
        }
    }

    public static void checkTameFromWild(ServerLevel level, Animal animal, AnimalFavorSavedData favorData) {
        UUID uuid = animal.getUUID();
        int favorLevel = favorData.getAnimalFavorLevel(uuid);
        if (favorLevel < KRConfig.WILD_TAME_FAVOR_THRESHOLD.get()) return;

        AnimalMoodSavedData moodData = AnimalMoodEventHandler.getAnimalMoodData(level);
        if (!moodData.isWild(uuid)) return;

        moodData.setWild(uuid, false);
        AnimalMoodEventHandler.syncMoodToTrackingPlayers(animal, moodData);

        animal.goalSelector.getAvailableGoals().stream()
                .map(WrappedGoal::getGoal)
                .filter(goal -> goal instanceof KRFleeFromPlayerGoal)
                .toList()
                .forEach(animal.goalSelector::removeGoal);
        AnimalHungerEventHandler.replaceTemptGoal(animal);
    }

    public static void syncFavorToTrackingPlayers(Animal animal, AnimalFavorSavedData data) {
        UUID uuid = animal.getUUID();
        Map<UUID, Integer> levelEntry = Map.of(uuid, data.getAnimalFavorLevel(uuid));
        Map<UUID, Integer> expEntry = Map.of(uuid, data.getAnimalFavorExp(uuid));
        Map<UUID, Integer> breedEntry = Map.of(uuid, data.getBreedCount(uuid));
        AnimalFavorSyncPayload payload = new AnimalFavorSyncPayload(levelEntry, expEntry, breedEntry);
        PacketDistributor.sendToPlayersTrackingEntity(animal, payload);
    }

    public static void syncFavorToPlayer(ServerPlayer player, Animal animal, AnimalFavorSavedData data) {
        UUID uuid = animal.getUUID();
        Map<UUID, Integer> levelEntry = Map.of(uuid, data.getAnimalFavorLevel(uuid));
        Map<UUID, Integer> expEntry = Map.of(uuid, data.getAnimalFavorExp(uuid));
        Map<UUID, Integer> breedEntry = Map.of(uuid, data.getBreedCount(uuid));
        AnimalFavorSyncPayload payload = new AnimalFavorSyncPayload(levelEntry, expEntry, breedEntry);
        PacketDistributor.sendToPlayer(player, payload);
    }

    public static AnimalFavorSavedData getAnimalFavorData(ServerLevel level) {
        return level.getDataStorage().computeIfAbsent(
                AnimalFavorSavedData.factory(),
                AnimalFavorSavedData.getDataName()
        );
    }
}