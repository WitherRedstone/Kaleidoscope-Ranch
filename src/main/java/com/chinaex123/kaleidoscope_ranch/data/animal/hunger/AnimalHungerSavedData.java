package com.chinaex123.kaleidoscope_ranch.data.animal.hunger;

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
            this.animalHungerMap.put(uuid, new AnimalHunger(hunger));
            this.consecutiveFeedCountMap.put(uuid, feedCount);
            if (lastFeedGameTime >= 0) {
                this.lastFeedGameTimeMap.put(uuid, lastFeedGameTime);
            }
        }
    }

    public float getAnimalHungerValue(UUID uuid) {
        AnimalHunger hunger = animalHungerMap.get(uuid);
        return hunger != null ? hunger.getHungerValue() : AnimalHunger.INITIAL_HUNGER;
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
        return lastFeedTime + HungerRise.FEED_COOLDOWN_TICKS;
    }

    public boolean wasFedToday(UUID uuid, long currentGameTime) {
        Long lastFeedTime = lastFeedGameTimeMap.get(uuid);
        if (lastFeedTime == null) return false;
        return (lastFeedTime / 24000L) == (currentGameTime / 24000L);
    }

    public boolean isFeedCooldownExpired(UUID uuid, long currentGameTime) {
        Long lastFeedTime = lastFeedGameTimeMap.get(uuid);
        if (lastFeedTime == null) return true;
        return (currentGameTime - lastFeedTime) >= HungerRise.FEED_COOLDOWN_TICKS;
    }

    public long getFeedCooldownRemaining(UUID uuid, long currentGameTime) {
        Long lastFeedTime = lastFeedGameTimeMap.get(uuid);
        if (lastFeedTime == null) return 0;
        long elapsed = currentGameTime - lastFeedTime;
        long remaining = HungerRise.FEED_COOLDOWN_TICKS - elapsed;
        return Math.max(remaining, 0);
    }

    @Override
    public @NotNull CompoundTag save(CompoundTag tag, HolderLookup.Provider registries) {
        ListTag list = new ListTag();
        for (Map.Entry<UUID, AnimalHunger> entry : animalHungerMap.entrySet()) {
            UUID uuid = entry.getKey();
            CompoundTag entryTag = new CompoundTag();
            entryTag.putUUID("kr_animal_id", uuid);
            entryTag.putFloat("kr_hunger_value", entry.getValue().getHungerValue());
            entryTag.putInt("kr_consecutive_feed_count", consecutiveFeedCountMap.getOrDefault(uuid, 0));
            entryTag.putLong("kr_last_feed_game_time", lastFeedGameTimeMap.getOrDefault(uuid, -1L));
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