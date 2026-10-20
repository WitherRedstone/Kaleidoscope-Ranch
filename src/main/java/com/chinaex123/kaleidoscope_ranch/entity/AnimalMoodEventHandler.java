package com.chinaex123.kaleidoscope_ranch.entity;

import com.chinaex123.kaleidoscope_ranch.KaleidoscopeRanch;
import com.chinaex123.kaleidoscope_ranch.data.animal.AnimalMood;
import com.chinaex123.kaleidoscope_ranch.data.animal.AnimalMoodSavedData;
import com.chinaex123.kaleidoscope_ranch.data.animal.mood.MoodDecline;
import com.chinaex123.kaleidoscope_ranch.data.animal.mood.MoodRise;
import com.chinaex123.kaleidoscope_ranch.init.KREntityTags;
import com.chinaex123.kaleidoscope_ranch.network.AnimalMoodSyncPayload;
import com.chinaex123.kaleidoscope_ranch.util.PetScheduler;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.level.border.WorldBorder;
import net.minecraft.world.phys.AABB;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.EntityJoinLevelEvent;
import net.neoforged.neoforge.event.entity.living.LivingDamageEvent;
import net.neoforged.neoforge.event.entity.living.LivingDeathEvent;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;
import net.neoforged.neoforge.event.entity.player.PlayerInteractEvent;
import net.neoforged.neoforge.event.tick.ServerTickEvent;
import net.neoforged.neoforge.network.PacketDistributor;

import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * 动物心情事件处理器。
 * <p>
 * 负责监听动物加入世界、服务端 tick、玩家交互、受伤与玩家开始追踪等事件，
 * 据此维护动物心情数据并同步到客户端。
 */
@EventBusSubscriber(modid = KaleidoscopeRanch.MODID)
public class AnimalMoodEventHandler {

    /**
     * 处理动物加入世界事件。
     * <p>
     * 若该动物没有心情数据则初始化为初始心情，
     * 并向追踪该动物的玩家同步心情。
     *
     * @param event 实体加入世界事件
     */
    @SubscribeEvent
    public static void onAnimalJoinLevel(EntityJoinLevelEvent event) {
        if (event.getLevel().isClientSide()) return;
        if (event.getEntity() instanceof Animal animal && KREntityTags.isManagedAnimal(animal)) {
            ServerLevel level = (ServerLevel) event.getLevel();
            AnimalMoodSavedData data = getAnimalMoodData(level);
            if (!data.hasAnimalMoodData(animal.getUUID())) {
                data.setAnimalMoodScore(animal.getUUID(), AnimalMood.INITIAL_MOOD);
            }
            syncMoodToTrackingPlayers(animal, data);
        }
    }

    /**
     * 处理服务端 tick 事件中的心情更新。
     * <p>
     * 仅在到达任意惩罚、加成或恢复的 tick 间隔时才遍历所有动物，
     * 计算心情变化量并应用，若发生改变则同步到客户端。
     *
     * @param event 服务端 tick 事件
     */
    @SubscribeEvent
    public static void onServerMoodUpdateTick(ServerTickEvent.Post event) {
        MinecraftServer server = event.getServer();
        long tickCount = server.getTickCount();

        boolean anyPenalty = tickCount % MoodDecline.DIVERSE_SPECIES_PENALTY_TICK_INTERVAL == 0
                || tickCount % MoodDecline.FECES_PENALTY_TICK_INTERVAL == 0
                || tickCount % MoodDecline.RAIN_PENALTY_TICK_INTERVAL == 0;
        boolean anyBonus = tickCount % MoodRise.SALT_BLOCK_MOOD_BONUS_TICK_INTERVAL == 0;
        boolean recovery = tickCount % MoodDecline.MOOD_RECOVERY_TICK_INTERVAL == 0;

        if (!anyPenalty && !anyBonus && !recovery) return;

        for (ServerLevel level : server.getAllLevels()) {
            AnimalMoodSavedData data = getAnimalMoodData(level);
            WorldBorder border = level.getWorldBorder();
            AABB worldBounds = new AABB(
                    border.getMinX(), level.getMinBuildHeight(), border.getMinZ(),
                    border.getMaxX(), level.getMaxBuildHeight(), border.getMaxZ()
            );
            List<? extends Animal> allAnimals = level.getEntitiesOfClass(Animal.class, worldBounds);
            List<Animal> animals = allAnimals.stream()
                    .filter(KREntityTags::isManagedAnimal)
                    .map(a -> (Animal) a)
                    .toList();

            for (Animal animal : animals) {
                if (!data.hasAnimalMoodData(animal.getUUID())) continue;

                int currentMood = data.getAnimalMoodScore(animal.getUUID());

                int moodDelta = MoodDecline.calcTickPenalty(level, animal, tickCount);
                moodDelta += MoodRise.calcTickBonus(level, animal, currentMood, tickCount);

                if (recovery && moodDelta == 0) {
                    moodDelta += MoodDecline.calcRecovery(level, animal, currentMood);
                }

                if (data.applyMoodDelta(animal.getUUID(), moodDelta) || (recovery && moodDelta == 0)) {
                    syncMoodToTrackingPlayers(animal, data);
                }
            }
        }
    }

    /**
     * 处理玩家与动物交互事件。
     * <p>
     * 玩家空手、未潜行且当天尚未抚摸该动物时，
     * 增加心情、记录抚摸日期、生成爱心粒子并同步到客户端。
     *
     * @param event 实体交互事件
     */
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

        spawnHeartParticles(level, animal);

        player.swing(hand);
        event.setCancellationResult(InteractionResult.SUCCESS);
        event.setCanceled(true);
    }

    /**
     * 处理动物受伤事件。
     * <p>
     * 动物受伤后降低心情，并同步到客户端。
     *
     * @param event 生物受伤事件
     */
    @SubscribeEvent
    public static void onAnimalHurt(LivingDamageEvent.Post event) {
        LivingEntity entity = event.getEntity();
        if (entity.level().isClientSide()) return;
        if (!(entity instanceof Animal animal)) return;
        if (!KREntityTags.isManagedAnimal(animal)) return;

        ServerLevel level = (ServerLevel) entity.level();
        AnimalMoodSavedData data = getAnimalMoodData(level);
        if (!data.hasAnimalMoodData(animal.getUUID())) return;

        data.addAnimalMoodScore(animal.getUUID(), -MoodDecline.calcHurtPenalty());
        syncMoodToTrackingPlayers(animal, data);
    }

    /**
     * 在动物头顶生成爱心粒子。
     *
     * @param level  服务端世界
     * @param animal 动物
     */
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

    /**
     * 处理玩家开始追踪实体事件。
     * <p>
     * 当玩家开始追踪动物时，将当前心情与当天抚摸状态同步给该玩家。
     *
     * @param event 玩家追踪实体事件
     */
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
        Map<UUID, Integer> moodEntry = Map.of(uuid, data.getAnimalMoodScore(uuid));
        Map<UUID, Boolean> petEntry = Map.of(uuid, data.hasBeenPetToday(uuid, gameDay));
        AnimalMoodSyncPayload payload = new AnimalMoodSyncPayload(moodEntry, petEntry);
        PacketDistributor.sendToPlayer(player, payload);
    }

    /**
     * 将动物心情同步给所有追踪该动物的玩家。
     *
     * @param animal 动物
     * @param data   动物心情存档数据
     */
    public static void syncMoodToTrackingPlayers(Animal animal, AnimalMoodSavedData data) {
        UUID uuid = animal.getUUID();
        long gameDay = ((ServerLevel) animal.level()).getDayTime() / 24000L;
        Map<UUID, Integer> moodEntry = Map.of(uuid, data.getAnimalMoodScore(uuid));
        Map<UUID, Boolean> petEntry = Map.of(uuid, data.hasBeenPetToday(uuid, gameDay));
        AnimalMoodSyncPayload payload = new AnimalMoodSyncPayload(moodEntry, petEntry);
        PacketDistributor.sendToPlayersTrackingEntity(animal, payload);
    }

    /**
     * 获取指定世界的动物心情存档数据。
     * <p>
     * 若不存在则创建新的存档数据。
     *
     * @param level 服务端世界
     * @return 动物心情存档数据
     */
    public static AnimalMoodSavedData getAnimalMoodData(ServerLevel level) {
        return level.getDataStorage().computeIfAbsent(
                AnimalMoodSavedData.factory(),
                AnimalMoodSavedData.getDataName()
        );
    }

    /**
     * 处理动物死亡事件。
     * <p>
     * 动物死亡后移除其心情数据与抚摸记录，
     * 避免存档数据随死亡动物无限累积。
     *
     * @param event 生物死亡事件
     */
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