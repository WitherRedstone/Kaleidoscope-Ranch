package com.chinaex123.kaleidoscope_ranch.data.animal.hunger;

import com.chinaex123.kaleidoscope_ranch.entity.ai.KREatFromTroughGoal;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.world.level.saveddata.SavedData;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class AnimalHungerSavedData extends SavedData {

    private static final String DATA_NAME = "kaleidoscope_ranch_animal_hunger";

    private final Map<UUID, AnimalHunger> animalHungerMap = new HashMap<>();
    private final Map<UUID, Integer> consecutiveFeedCountMap = new HashMap<>();
    private final Map<UUID, Long> lastFeedGameTimeMap = new HashMap<>();
    private final Map<UUID, Long> lastTroughFeedGameTimeMap = new HashMap<>();
    private final Map<UUID, Long> shearCooldownExpireMap = new HashMap<>();

    public AnimalHungerSavedData() {
        super();
    }

    public AnimalHungerSavedData(CompoundTag tag, HolderLookup.Provider registries) {
        super();
        ListTag list = tag.getList("kr_animal_hunger_list", 10);
        for (int i = 0; i < list.size(); i++) {
            CompoundTag entry = list.getCompound(i);
            UUID uuid = entry.getUUID("kr_animal_id");
            float hunger = entry.getFloat("kr_hunger_value");
            int feedCount = entry.getInt("kr_consecutive_feed_count");
            long lastFeedGameTime = entry.getLong("kr_last_feed_game_time");
            long lastTroughFeedGameTime = entry.getLong("kr_last_trough_feed_game_time");
            this.animalHungerMap.put(uuid, new AnimalHunger(hunger));
            this.consecutiveFeedCountMap.put(uuid, feedCount);
            if (lastFeedGameTime >= 0) {
                this.lastFeedGameTimeMap.put(uuid, lastFeedGameTime);
            }
            if (lastTroughFeedGameTime >= 0) {
                this.lastTroughFeedGameTimeMap.put(uuid, lastTroughFeedGameTime);
            }
            long shearCooldownExpire = entry.getLong("kr_shear_cooldown_expire");
            if (shearCooldownExpire > 0) {
                this.shearCooldownExpireMap.put(uuid, shearCooldownExpire);
            }
        }
    }

    public float getAnimalHungerValue(UUID uuid) {
        AnimalHunger hunger = animalHungerMap.get(uuid);
        return hunger != null ? hunger.getHungerValue() : AnimalHunger.getInitialHunger();
    }

    public void setAnimalHungerValue(UUID uuid, float hungerValue) {
        animalHungerMap.computeIfAbsent(uuid, k -> new AnimalHunger()).setHungerValue(hungerValue);
        setDirty();
    }

    public void addAnimalHungerValue(UUID uuid, float delta) {
        animalHungerMap.computeIfAbsent(uuid, k -> new AnimalHunger()).addHungerValue(delta);
        setDirty();
    }

    public boolean applyHungerDelta(UUID uuid, float delta) {
        if (delta == 0) return false;
        addAnimalHungerValue(uuid, delta);
        return true;
    }

    public void removeAnimalHunger(UUID uuid) {
        animalHungerMap.remove(uuid);
        consecutiveFeedCountMap.remove(uuid);
        lastFeedGameTimeMap.remove(uuid);
        lastTroughFeedGameTimeMap.remove(uuid);
        shearCooldownExpireMap.remove(uuid);
        setDirty();
    }

    public boolean hasAnimalHungerData(UUID uuid) {
        return animalHungerMap.containsKey(uuid);
    }

    public boolean isAnimalFull(UUID uuid) {
        AnimalHunger hunger = animalHungerMap.get(uuid);
        return hunger != null && hunger.isFull();
    }

    public Map<UUID, AnimalHunger> getAllAnimalHungerData() {
        return animalHungerMap;
    }

    public int getConsecutiveFeedCount(UUID uuid) {
        return consecutiveFeedCountMap.getOrDefault(uuid, 0);
    }

    public void setConsecutiveFeedCount(UUID uuid, int count) {
        consecutiveFeedCountMap.put(uuid, count);
        setDirty();
    }

    public void incrementConsecutiveFeedCount(UUID uuid) {
        consecutiveFeedCountMap.merge(uuid, 1, Integer::sum);
        setDirty();
    }

    public void resetConsecutiveFeedCount(UUID uuid) {
        consecutiveFeedCountMap.put(uuid, 0);
        setDirty();
    }

    public long getLastFeedGameTime(UUID uuid) {
        return lastFeedGameTimeMap.getOrDefault(uuid, -1L);
    }

    public void setLastFeedGameTime(UUID uuid, long gameTime) {
        lastFeedGameTimeMap.put(uuid, gameTime);
        setDirty();
    }

    public long getFeedCooldownExpire(UUID uuid) {
        Long lastFeedTime = lastFeedGameTimeMap.get(uuid);
        if (lastFeedTime == null) return 0L;
        return lastFeedTime + HungerRise.getFeedCooldownTicks();
    }

    public boolean wasFedToday(UUID uuid, long currentGameTime) {
        Long lastFeedTime = lastFeedGameTimeMap.get(uuid);
        if (lastFeedTime == null) return false;
        return (lastFeedTime / 24000L) == (currentGameTime / 24000L);
    }

    public boolean isFeedCooldownExpired(UUID uuid, long currentGameTime) {
        Long lastFeedTime = lastFeedGameTimeMap.get(uuid);
        if (lastFeedTime == null) return true;
        return (currentGameTime - lastFeedTime) >= HungerRise.getFeedCooldownTicks();
    }

    public long getFeedCooldownRemaining(UUID uuid, long currentGameTime) {
        Long lastFeedTime = lastFeedGameTimeMap.get(uuid);
        if (lastFeedTime == null) return 0;
        long elapsed = currentGameTime - lastFeedTime;
        long remaining = HungerRise.getFeedCooldownTicks() - elapsed;
        return Math.max(remaining, 0);
    }

    public long getLastTroughFeedGameTime(UUID uuid) {
        return lastTroughFeedGameTimeMap.getOrDefault(uuid, -1L);
    }

    public void setLastTroughFeedGameTime(UUID uuid, long gameTime) {
        lastTroughFeedGameTimeMap.put(uuid, gameTime);
        setDirty();
    }

    public long getTroughFeedCooldownExpire(UUID uuid) {
        Long lastTroughFeedTime = lastTroughFeedGameTimeMap.get(uuid);
        if (lastTroughFeedTime == null) return 0L;
        return lastTroughFeedTime + KREatFromTroughGoal.getTroughFeedCooldownTicks();
    }

    public boolean isTroughFeedCooldownExpired(UUID uuid, long currentGameTime) {
        Long lastTroughFeedTime = lastTroughFeedGameTimeMap.get(uuid);
        if (lastTroughFeedTime == null) return true;
        return (currentGameTime - lastTroughFeedTime) >= KREatFromTroughGoal.getTroughFeedCooldownTicks();
    }

    public long getTroughFeedCooldownRemaining(UUID uuid, long currentGameTime) {
        Long lastTroughFeedTime = lastTroughFeedGameTimeMap.get(uuid);
        if (lastTroughFeedTime == null) return 0;
        long elapsed = currentGameTime - lastTroughFeedTime;
        long remaining = KREatFromTroughGoal.getTroughFeedCooldownTicks() - elapsed;
        return Math.max(remaining, 0);
    }

    public long getShearCooldownExpire(UUID uuid) {
        return shearCooldownExpireMap.getOrDefault(uuid, 0L);
    }

    public void setShearCooldownExpire(UUID uuid, long expireGameTime) {
        shearCooldownExpireMap.put(uuid, expireGameTime);
        setDirty();
    }

    public boolean isShearCooldownExpired(UUID uuid, long currentGameTime) {
        Long expire = shearCooldownExpireMap.get(uuid);
        if (expire == null) return true;
        return currentGameTime >= expire;
    }

    public long getShearCooldownRemaining(UUID uuid, long currentGameTime) {
        Long expire = shearCooldownExpireMap.get(uuid);
        if (expire == null) return 0;
        return Math.max(expire - currentGameTime, 0);
    }

    @Override
    public @NotNull CompoundTag save(@NotNull CompoundTag tag, HolderLookup.@NotNull Provider registries) {
        ListTag list = new ListTag();
        for (Map.Entry<UUID, AnimalHunger> entry : animalHungerMap.entrySet()) {
            UUID uuid = entry.getKey();
            CompoundTag entryTag = new CompoundTag();
            entryTag.putUUID("kr_animal_id", uuid);
            entryTag.putFloat("kr_hunger_value", entry.getValue().getHungerValue());
            entryTag.putInt("kr_consecutive_feed_count", consecutiveFeedCountMap.getOrDefault(uuid, 0));
            entryTag.putLong("kr_last_feed_game_time", lastFeedGameTimeMap.getOrDefault(uuid, -1L));
            entryTag.putLong("kr_last_trough_feed_game_time", lastTroughFeedGameTimeMap.getOrDefault(uuid, -1L));
            entryTag.putLong("kr_shear_cooldown_expire", shearCooldownExpireMap.getOrDefault(uuid, 0L));
            list.add(entryTag);
        }
        tag.put("kr_animal_hunger_list", list);
        return tag;
    }

    public static SavedData.Factory<AnimalHungerSavedData> factory() {
        return new SavedData.Factory<>(AnimalHungerSavedData::new, AnimalHungerSavedData::new);
    }

    public static String getDataName() {
        return DATA_NAME;
    }
}