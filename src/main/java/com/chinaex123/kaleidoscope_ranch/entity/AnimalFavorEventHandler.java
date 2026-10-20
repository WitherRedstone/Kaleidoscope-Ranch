package com.chinaex123.kaleidoscope_ranch.entity;

import com.chinaex123.kaleidoscope_ranch.KaleidoscopeRanch;
import com.chinaex123.kaleidoscope_ranch.data.animal.favor.AnimalFavor;
import com.chinaex123.kaleidoscope_ranch.data.animal.favor.AnimalFavorSavedData;
import com.chinaex123.kaleidoscope_ranch.data.animal.favor.FavorDecline;
import com.chinaex123.kaleidoscope_ranch.init.KREntityTags;
import com.chinaex123.kaleidoscope_ranch.network.AnimalFavorSyncPayload;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.LivingEntity;
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
                data.setAnimalFavorValue(animal.getUUID(), AnimalFavor.INITIAL_FAVOR);
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
        AnimalFavorSavedData data = getAnimalFavorData(level);
        if (!data.hasAnimalFavorData(animal.getUUID())) return;

        if (data.applyFavorDelta(animal.getUUID(), -FavorDecline.calcHurtPenalty())) {
            syncFavorToTrackingPlayers(animal, data);
        }
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

    /**
     * 统一的好感变更入口，供其他系统（抚摸、喂食等）调用。
     * 会做数据存在性检查、0 值短路、脏标记与同步。
     */
    public static void addFavor(ServerLevel level, Animal animal, float delta) {
        if (delta == 0) return;
        AnimalFavorSavedData data = getAnimalFavorData(level);
        if (!data.hasAnimalFavorData(animal.getUUID())) return;
        if (data.applyFavorDelta(animal.getUUID(), delta)) {
            syncFavorToTrackingPlayers(animal, data);
        }
    }

    public static void syncFavorToTrackingPlayers(Animal animal, AnimalFavorSavedData data) {
        UUID uuid = animal.getUUID();
        Map<UUID, Float> favorEntry = Map.of(uuid, data.getAnimalFavorValue(uuid));
        AnimalFavorSyncPayload payload = new AnimalFavorSyncPayload(favorEntry);
        PacketDistributor.sendToPlayersTrackingEntity(animal, payload);
    }

    public static void syncFavorToPlayer(ServerPlayer player, Animal animal, AnimalFavorSavedData data) {
        UUID uuid = animal.getUUID();
        Map<UUID, Float> favorEntry = Map.of(uuid, data.getAnimalFavorValue(uuid));
        AnimalFavorSyncPayload payload = new AnimalFavorSyncPayload(favorEntry);
        PacketDistributor.sendToPlayer(player, payload);
    }

    public static AnimalFavorSavedData getAnimalFavorData(ServerLevel level) {
        return level.getDataStorage().computeIfAbsent(
                AnimalFavorSavedData.factory(),
                AnimalFavorSavedData.getDataName()
        );
    }
}