package com.chinaex123.kaleidoscope_ranch.entity;

import com.chinaex123.kaleidoscope_ranch.KaleidoscopeRanch;
import com.chinaex123.kaleidoscope_ranch.config.KRConfig;
import com.chinaex123.kaleidoscope_ranch.data.animal.favor.FavorRise;
import com.chinaex123.kaleidoscope_ranch.data.animal.hunger.AnimalHungerSavedData;
import com.chinaex123.kaleidoscope_ranch.data.animal.mood.AnimalMoodSavedData;
import com.chinaex123.kaleidoscope_ranch.data.animal.mood.MoodDecline;
import com.chinaex123.kaleidoscope_ranch.data.animal.mood.MoodRise;
import com.chinaex123.kaleidoscope_ranch.entity.ai.KREatFromTroughGoal;
import com.chinaex123.kaleidoscope_ranch.entity.ai.KRFleeFromPlayerGoal;
import com.chinaex123.kaleidoscope_ranch.init.tags.KREntityTags;
import com.chinaex123.kaleidoscope_ranch.network.AnimalMoodSyncPayload;
import com.chinaex123.kaleidoscope_ranch.util.PetScheduler;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.animal.Animal;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.EntityJoinLevelEvent;
import net.neoforged.neoforge.event.entity.living.LivingDamageEvent;
import net.neoforged.neoforge.event.entity.living.LivingDeathEvent;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;
import net.neoforged.neoforge.event.entity.player.PlayerInteractEvent;
import net.neoforged.neoforge.network.PacketDistributor;

import java.util.Map;
import java.util.UUID;

@EventBusSubscriber(modid = KaleidoscopeRanch.MODID)
public class AnimalMoodEventHandler {

    @SubscribeEvent
    public static void onAnimalJoinLevel(EntityJoinLevelEvent event) {
        if (event.getLevel().isClientSide()) return;
        if (event.getEntity() instanceof Animal animal && KREntityTags.isManagedAnimal(animal)) {
            ServerLevel level = (ServerLevel) event.getLevel();
            AnimalMoodSavedData data = getAnimalMoodData(level);
            if (!data.hasAnimalMoodData(animal.getUUID())) {
                data.setWild(animal.getUUID(), true);
                double minMood = KRConfig.WILD_INITIAL_MOOD_MIN.get();
                double maxMood = KRConfig.WILD_INITIAL_MOOD_MAX.get();
                double initialMood = minMood + level.random.nextDouble() * (maxMood - minMood);
                data.setAnimalMoodScore(animal.getUUID(), initialMood);
            }
            syncMoodToTrackingPlayers(animal, data);

            AnimalHungerSavedData hungerData = AnimalHungerEventHandler.getAnimalHungerData(level);
            if (data.isWild(animal.getUUID())) {
                animal.goalSelector.getAvailableGoals().removeIf(wrappedGoal ->
                        wrappedGoal.getGoal() instanceof KRFleeFromPlayerGoal
                );
                animal.goalSelector.addGoal(0, new KRFleeFromPlayerGoal(animal));
            } else if (hungerData.hasAnimalHungerData(animal.getUUID())) {
                AnimalHungerEventHandler.replaceTemptGoal(animal);
            }

            animal.goalSelector.getAvailableGoals().removeIf(wrappedGoal ->
                    wrappedGoal.getGoal() instanceof KREatFromTroughGoal
            );
            animal.goalSelector.addGoal(3, new KREatFromTroughGoal(animal));
        }
    }

    @SubscribeEvent
    public static void onAnimalInteract(PlayerInteractEvent.EntityInteract event) {
        if (event.getLevel().isClientSide()) return;
        if (!(event.getTarget() instanceof Animal animal)) return;
        if (!KREntityTags.isManagedAnimal(animal)) return;
        if (!(event.getEntity() instanceof ServerPlayer player)) return;
        if (player.isShiftKeyDown()) return;

        InteractionHand hand = event.getHand();
        if (!player.getItemInHand(hand).isEmpty()) return;

        ServerLevel level = (ServerLevel) event.getLevel();
        AnimalMoodSavedData data = getAnimalMoodData(level);

        if (!PetScheduler.shouldPet(level, data, animal)) return;

        long gameDay = level.getDayTime() / 24000L;
        data.addAnimalMoodScore(animal.getUUID(), MoodRise.calcPetBonus());
        data.setLastPetDay(animal.getUUID(), gameDay);
        syncMoodToTrackingPlayers(animal, data);

        AnimalFavorEventHandler.addFavor(level, animal, FavorRise.calcPetFavor());

        spawnHeartParticles(level, animal);

        player.swing(hand);
        event.setCancellationResult(InteractionResult.SUCCESS);
        event.setCanceled(true);
    }

    @SubscribeEvent
    public static void onAnimalHurt(LivingDamageEvent.Post event) {
        LivingEntity entity = event.getEntity();
        if (entity.level().isClientSide()) return;
        if (!(entity instanceof Animal animal)) return;
        if (!KREntityTags.isManagedAnimal(animal)) return;

        ServerLevel level = (ServerLevel) entity.level();
        AnimalMoodSavedData data = getAnimalMoodData(level);
        if (!data.hasAnimalMoodData(animal.getUUID())) return;

        if (data.applyMoodDelta(animal.getUUID(), -MoodDecline.calcHurtPenalty())) {
            syncMoodToTrackingPlayers(animal, data);
        }
    }

    public static void spawnHeartParticles(ServerLevel level, Animal animal) {
        double x = animal.getX();
        double y = animal.getY() + animal.getBbHeight() + 0.3;
        double z = animal.getZ();
        for (int i = 0; i < 7; i++) {
            double offsetX = (level.random.nextDouble() - 0.5) * 0.5;
            double offsetY = level.random.nextDouble() * 0.3;
            double offsetZ = (level.random.nextDouble() - 0.5) * 0.5;
            level.sendParticles(ParticleTypes.HEART,
                    x + offsetX, y + offsetY, z + offsetZ,
                    1, 0, 0.02, 0, 0);
        }
    }

    @SubscribeEvent
    public static void onStartTracking(PlayerEvent.StartTracking event) {
        if (event.getEntity().level().isClientSide()) return;
        if (!(event.getTarget() instanceof Animal animal)) return;
        if (!KREntityTags.isManagedAnimal(animal)) return;
        if (!(event.getEntity() instanceof ServerPlayer player)) return;

        ServerLevel level = (ServerLevel) animal.level();
        AnimalMoodSavedData data = getAnimalMoodData(level);
        if (!data.hasAnimalMoodData(animal.getUUID())) return;

        UUID uuid = animal.getUUID();
        long gameDay = level.getDayTime() / 24000L;
        Map<UUID, Double> moodEntry = Map.of(uuid, data.getAnimalMoodScore(uuid));
        Map<UUID, Boolean> petEntry = Map.of(uuid, data.hasBeenPetToday(uuid, gameDay));
        Map<UUID, Boolean> wildEntry = Map.of(uuid, data.isWild(uuid));
        AnimalMoodSyncPayload payload = new AnimalMoodSyncPayload(moodEntry, petEntry, wildEntry);
        PacketDistributor.sendToPlayer(player, payload);

        AnimalHungerEventHandler.syncHungerToPlayer(player, animal, AnimalHungerEventHandler.getAnimalHungerData(level));
        AnimalFavorEventHandler.syncFavorToPlayer(player, animal, AnimalFavorEventHandler.getAnimalFavorData(level));
        AnimalMilkEventHandler.syncMilkToPlayer(player, animal, AnimalMilkEventHandler.getAnimalMilkData(level));
    }

    public static void syncMoodToTrackingPlayers(Animal animal, AnimalMoodSavedData data) {
        UUID uuid = animal.getUUID();
        long gameDay = ((ServerLevel) animal.level()).getDayTime() / 24000L;
        Map<UUID, Double> moodEntry = Map.of(uuid, data.getAnimalMoodScore(uuid));
        Map<UUID, Boolean> petEntry = Map.of(uuid, data.hasBeenPetToday(uuid, gameDay));
        Map<UUID, Boolean> wildEntry = Map.of(uuid, data.isWild(uuid));
        AnimalMoodSyncPayload payload = new AnimalMoodSyncPayload(moodEntry, petEntry, wildEntry);
        PacketDistributor.sendToPlayersTrackingEntity(animal, payload);
    }

    public static AnimalMoodSavedData getAnimalMoodData(ServerLevel level) {
        return level.getDataStorage().computeIfAbsent(
                AnimalMoodSavedData.factory(),
                AnimalMoodSavedData.getDataName()
        );
    }

    @SubscribeEvent
    public static void onAnimalDeath(LivingDeathEvent event) {
        if (event.getEntity().level().isClientSide()) return;
        if (!(event.getEntity() instanceof Animal animal)) return;
        if (!KREntityTags.isManagedAnimal(animal)) return;

        ServerLevel level = (ServerLevel) animal.level();
        AnimalMoodSavedData data = getAnimalMoodData(level);
        if (!data.hasAnimalMoodData(animal.getUUID())) return;

        data.removeAnimalMood(animal.getUUID());
    }
}