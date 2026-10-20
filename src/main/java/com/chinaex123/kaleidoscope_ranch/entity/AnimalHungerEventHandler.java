package com.chinaex123.kaleidoscope_ranch.entity;

import com.chinaex123.kaleidoscope_ranch.KaleidoscopeRanch;
import com.chinaex123.kaleidoscope_ranch.data.animal.hunger.AnimalHunger;
import com.chinaex123.kaleidoscope_ranch.data.animal.hunger.AnimalHungerSavedData;
import com.chinaex123.kaleidoscope_ranch.data.animal.mood.AnimalMoodSavedData;
import com.chinaex123.kaleidoscope_ranch.init.KREntityTags;
import com.chinaex123.kaleidoscope_ranch.init.KRItems;
import com.chinaex123.kaleidoscope_ranch.network.AnimalHungerSyncPayload;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.ai.goal.TemptGoal;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.border.WorldBorder;
import net.minecraft.world.phys.AABB;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.EntityJoinLevelEvent;
import net.neoforged.neoforge.event.entity.living.LivingDeathEvent;
import net.neoforged.neoforge.event.entity.player.PlayerInteractEvent;
import net.neoforged.neoforge.event.tick.ServerTickEvent;
import net.neoforged.neoforge.network.PacketDistributor;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@EventBusSubscriber(modid = KaleidoscopeRanch.MODID)
public class AnimalHungerEventHandler {

    public static final float FEED_HUNGER_BONUS = 2.0f;
    public static final int HUNGER_DECAY_TICK_INTERVAL = 12000;
    public static final float HUNGER_DECAY_AMOUNT = 0.5f;

    @SubscribeEvent
    public static void onAnimalJoinLevel(EntityJoinLevelEvent event) {
        if (event.getLevel().isClientSide()) return;
        if (event.getEntity() instanceof Animal animal && KREntityTags.isManagedAnimal(animal)) {
            ServerLevel level = (ServerLevel) event.getLevel();
            AnimalHungerSavedData data = getAnimalHungerData(level);
            if (!data.hasAnimalHungerData(animal.getUUID())) {
                data.setAnimalHungerValue(animal.getUUID(), AnimalHunger.INITIAL_HUNGER);
            }
            syncHungerToTrackingPlayers(animal, data);
            replaceTemptGoal(animal);
        }
    }

    private static void replaceTemptGoal(Animal animal) {
        Item feedItem = getFeedForAnimal(animal);
        if (feedItem == null) return;

        Ingredient feedIngredient = Ingredient.of(feedItem);

        animal.goalSelector.getAvailableGoals().removeIf(wrappedGoal ->
                wrappedGoal.getGoal() instanceof TemptGoal
        );

        animal.goalSelector.addGoal(3, new TemptGoal(animal, 1.0, feedIngredient, false));
    }

    @SubscribeEvent
    public static void onServerHungerUpdateTick(ServerTickEvent.Post event) {
        MinecraftServer server = event.getServer();
        long tickCount = server.getTickCount();

        if (tickCount % HUNGER_DECAY_TICK_INTERVAL != 0) return;

        for (ServerLevel level : server.getAllLevels()) {
            AnimalHungerSavedData data = getAnimalHungerData(level);
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
                if (!data.hasAnimalHungerData(animal.getUUID())) continue;

                float currentHunger = data.getAnimalHungerValue(animal.getUUID());
                if (currentHunger > AnimalHunger.MIN_HUNGER) {
                    data.addAnimalHungerValue(animal.getUUID(), -HUNGER_DECAY_AMOUNT);
                    syncHungerToTrackingPlayers(animal, data);
                }
            }
        }
    }

    @SubscribeEvent
    public static void onAnimalInteract(PlayerInteractEvent.EntityInteract event) {
        if (event.getLevel().isClientSide()) return;
        if (!(event.getTarget() instanceof Animal animal)) return;
        if (!KREntityTags.isManagedAnimal(animal)) return;
        if (!(event.getEntity() instanceof ServerPlayer player)) return;

        InteractionHand hand = event.getHand();
        ItemStack heldItem = player.getItemInHand(hand);
        if (heldItem.isEmpty()) return;

        Item feedItem = getFeedForAnimal(animal);
        if (feedItem == null || heldItem.getItem() != feedItem) return;

        ServerLevel level = (ServerLevel) event.getLevel();
        AnimalHungerSavedData hungerData = getAnimalHungerData(level);
        if (!hungerData.hasAnimalHungerData(animal.getUUID())) return;

        if (player.isShiftKeyDown()) {
            AnimalMoodSavedData moodData = AnimalMoodEventHandler.getAnimalMoodData(level);
            int mood = moodData.getAnimalMoodScore(animal.getUUID());
            float hunger = hungerData.getAnimalHungerValue(animal.getUUID());

            if (mood >= 100 && hunger >= AnimalHunger.MAX_HUNGER) {
                if (!player.hasInfiniteMaterials()) {
                    heldItem.shrink(1);
                }
                animal.setInLove(player);
                spawnHeartParticles(level, animal);
                player.swing(hand);
                event.setCancellationResult(InteractionResult.SUCCESS);
                event.setCanceled(true);
            }
        } else {
            float currentHunger = hungerData.getAnimalHungerValue(animal.getUUID());
            if (currentHunger >= AnimalHunger.MAX_HUNGER) return;

            hungerData.addAnimalHungerValue(animal.getUUID(), FEED_HUNGER_BONUS);
            if (!player.hasInfiniteMaterials()) {
                heldItem.shrink(1);
            }
            syncHungerToTrackingPlayers(animal, hungerData);
            spawnHappyParticles(level, animal);

            player.swing(hand);
            event.setCancellationResult(InteractionResult.SUCCESS);
            event.setCanceled(true);
        }
    }

    @SubscribeEvent
    public static void onAnimalDeath(LivingDeathEvent event) {
        if (event.getEntity().level().isClientSide()) return;
        if (!(event.getEntity() instanceof Animal animal)) return;
        if (!KREntityTags.isManagedAnimal(animal)) return;

        ServerLevel level = (ServerLevel) animal.level();
        AnimalHungerSavedData data = getAnimalHungerData(level);
        if (!data.hasAnimalHungerData(animal.getUUID())) return;

        data.removeAnimalHunger(animal.getUUID());
    }

    public static Item getFeedForAnimal(Animal animal) {
        if (animal.getType().is(KREntityTags.POULTRY)) {
            return KRItems.POULTRY_FEED.get();
        }
        if (animal.getType().is(KREntityTags.LIVESTOCK)) {
            return KRItems.LIVESTOCK_FEED.get();
        }
        return null;
    }

    public static void syncHungerToTrackingPlayers(Animal animal, AnimalHungerSavedData data) {
        UUID uuid = animal.getUUID();
        Map<UUID, Float> hungerEntry = Map.of(uuid, data.getAnimalHungerValue(uuid));
        AnimalHungerSyncPayload payload = new AnimalHungerSyncPayload(hungerEntry);
        PacketDistributor.sendToPlayersTrackingEntity(animal, payload);
    }

    public static void syncHungerToPlayer(ServerPlayer player, Animal animal, AnimalHungerSavedData data) {
        UUID uuid = animal.getUUID();
        Map<UUID, Float> hungerEntry = Map.of(uuid, data.getAnimalHungerValue(uuid));
        AnimalHungerSyncPayload payload = new AnimalHungerSyncPayload(hungerEntry);
        PacketDistributor.sendToPlayer(player, payload);
    }

    public static AnimalHungerSavedData getAnimalHungerData(ServerLevel level) {
        return level.getDataStorage().computeIfAbsent(
                AnimalHungerSavedData.factory(),
                AnimalHungerSavedData.getDataName()
        );
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

    public static void spawnHappyParticles(ServerLevel level, Animal animal) {
        double x = animal.getX();
        double y = animal.getY() + animal.getBbHeight() + 0.3;
        double z = animal.getZ();
        for (int i = 0; i < 5; i++) {
            double offsetX = (level.random.nextDouble() - 0.5) * 0.5;
            double offsetY = level.random.nextDouble() * 0.3;
            double offsetZ = (level.random.nextDouble() - 0.5) * 0.5;
            level.sendParticles(ParticleTypes.HAPPY_VILLAGER,
                    x + offsetX, y + offsetY, z + offsetZ,
                    1, 0, 0.02, 0, 0);
        }
    }
}