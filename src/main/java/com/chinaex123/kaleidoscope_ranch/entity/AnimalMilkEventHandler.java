package com.chinaex123.kaleidoscope_ranch.entity;

import com.chinaex123.kaleidoscope_ranch.KaleidoscopeRanch;
import com.chinaex123.kaleidoscope_ranch.data.animal.milk.AnimalMilkSavedData;
import com.chinaex123.kaleidoscope_ranch.init.tags.KREntityTags;
import com.chinaex123.kaleidoscope_ranch.network.AnimalMilkSyncPayload;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.animal.Cow;
import net.minecraft.world.entity.animal.goat.Goat;
import net.minecraft.world.entity.animal.Rabbit;
import net.minecraft.world.entity.animal.MushroomCow;
import net.minecraft.world.entity.animal.horse.Donkey;
import net.minecraft.world.entity.animal.horse.Horse;
import net.minecraft.world.entity.animal.camel.Camel;
import net.minecraft.world.entity.animal.horse.Llama;
import net.minecraft.world.entity.animal.Sheep;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.EntityJoinLevelEvent;
import net.neoforged.neoforge.event.entity.living.LivingDeathEvent;
import net.neoforged.neoforge.network.PacketDistributor;

import java.util.Map;
import java.util.UUID;

@EventBusSubscriber(modid = KaleidoscopeRanch.MODID)
public class AnimalMilkEventHandler {

    @SubscribeEvent
    public static void onAnimalJoinLevel(EntityJoinLevelEvent event) {
        if (event.getLevel().isClientSide()) return;
        if (event.getEntity() instanceof Animal animal && KREntityTags.isManagedAnimal(animal)) {
            ServerLevel level = (ServerLevel) event.getLevel();
            AnimalMilkSavedData data = getAnimalMilkData(level);
            if (!data.hasMilkData(animal.getUUID())) {
                double maxCapacity;
                if (animal instanceof Cow) {
                    maxCapacity = 20000 + level.random.nextDouble() * 10000;
                } else if (animal instanceof Horse) {
                    maxCapacity = 150000 + level.random.nextDouble() * 40000;
                } else if (animal instanceof Donkey) {
                    maxCapacity = 2300 + level.random.nextDouble() * 3600;
                } else if (animal instanceof Llama) {
                    maxCapacity = 1000;
                } else if (animal instanceof Camel) {
                    maxCapacity = 5000 + level.random.nextDouble() * 25000;
                } else if (animal instanceof Goat) {
                    maxCapacity = 1000 + level.random.nextDouble() * 1000;
                } else if (animal instanceof Rabbit) {
                    maxCapacity = 1000;
                } else if (animal instanceof MushroomCow) {
                    maxCapacity = 20000 + level.random.nextDouble() * 10000;
                } else {
                    maxCapacity = 2000 + level.random.nextDouble() * 7000;
                }
                data.initMilkData(animal.getUUID(), maxCapacity);
            }
            syncMilkToTrackingPlayers(animal, data);
        }
    }

    @SubscribeEvent
    public static void onAnimalDeath(LivingDeathEvent event) {
        if (event.getEntity().level().isClientSide()) return;
        if (!(event.getEntity() instanceof Animal animal)) return;
        if (!KREntityTags.isManagedAnimal(animal)) return;

        ServerLevel level = (ServerLevel) animal.level();
        AnimalMilkSavedData data = getAnimalMilkData(level);
        data.removeMilkData(animal.getUUID());
    }

    public static void syncMilkToTrackingPlayers(Animal animal, AnimalMilkSavedData data) {
        UUID uuid = animal.getUUID();
        Map<UUID, Double> milkEntry = Map.of(uuid, data.getMilkAmount(uuid));
        Map<UUID, Double> maxCapacityEntry = Map.of(uuid, data.getMilkMaxCapacity(uuid));
        AnimalMilkSyncPayload payload = new AnimalMilkSyncPayload(milkEntry, maxCapacityEntry);
        PacketDistributor.sendToPlayersTrackingEntity(animal, payload);
    }

    public static void syncMilkToPlayer(ServerPlayer player, Animal animal, AnimalMilkSavedData data) {
        UUID uuid = animal.getUUID();
        Map<UUID, Double> milkEntry = Map.of(uuid, data.getMilkAmount(uuid));
        Map<UUID, Double> maxCapacityEntry = Map.of(uuid, data.getMilkMaxCapacity(uuid));
        AnimalMilkSyncPayload payload = new AnimalMilkSyncPayload(milkEntry, maxCapacityEntry);
        PacketDistributor.sendToPlayer(player, payload);
    }

    public static AnimalMilkSavedData getAnimalMilkData(ServerLevel level) {
        return level.getDataStorage().computeIfAbsent(
                AnimalMilkSavedData.factory(),
                AnimalMilkSavedData.getDataName()
        );
    }
}