package com.chinaex123.kaleidoscope_ranch.data.animal.mood;

import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.world.level.saveddata.SavedData;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class AnimalMoodSavedData extends SavedData {

    private static final String DATA_NAME = "kaleidoscope_ranch_animal_mood";
    private final Map<UUID, AnimalMood> animalMoodMap = new HashMap<>();
    private final Map<UUID, Long> lastPetDayMap = new HashMap<>();
    private final Map<UUID, Boolean> wildMap = new HashMap<>();

    public AnimalMoodSavedData() {
        super();
    }

    public AnimalMoodSavedData(CompoundTag tag, HolderLookup.Provider registries) {
        super();
        ListTag list = tag.getList("kr_animal_mood_list", 10);
        for (int i = 0; i < list.size(); i++) {
            CompoundTag entry = list.getCompound(i);
            UUID uuid = entry.getUUID("kr_animal_id");
            double mood = entry.getDouble("kr_mood_score");
            long lastPetDay = entry.getLong("kr_last_pet_day");
            this.animalMoodMap.put(uuid, new AnimalMood(mood));
            if (lastPetDay >= 0) {
                this.lastPetDayMap.put(uuid, lastPetDay);
            }
            if (entry.getBoolean("kr_wild")) {
                this.wildMap.put(uuid, true);
            }
        }
    }

    public double getAnimalMoodScore(UUID uuid) {
        AnimalMood mood = animalMoodMap.get(uuid);
        return mood != null ? mood.getMoodScore() : AnimalMood.getInitialMood();
    }

    public void setAnimalMoodScore(UUID uuid, double moodScore) {
        animalMoodMap.computeIfAbsent(uuid, k -> new AnimalMood()).setMoodScore(moodScore);
        setDirty();
    }

    public void addAnimalMoodScore(UUID uuid, double delta) {
        animalMoodMap.computeIfAbsent(uuid, k -> new AnimalMood()).addMoodScore(delta);
        setDirty();
    }

    public boolean applyMoodDelta(UUID uuid, double delta) {
        if (delta == 0) return false;
        addAnimalMoodScore(uuid, delta);
        return true;
    }

    public void removeAnimalMood(UUID uuid) {
        animalMoodMap.remove(uuid);
        lastPetDayMap.remove(uuid);
        wildMap.remove(uuid);
        setDirty();
    }

    public boolean hasAnimalMoodData(UUID uuid) {
        return animalMoodMap.containsKey(uuid);
    }

    public boolean canPet(UUID uuid, long gameDay) {
        Long lastDay = lastPetDayMap.get(uuid);
        return lastDay == null || lastDay != gameDay;
    }

    public void setLastPetDay(UUID uuid, long gameDay) {
        lastPetDayMap.put(uuid, gameDay);
        setDirty();
    }

    public boolean hasBeenPetToday(UUID uuid, long gameDay) {
        Long lastDay = lastPetDayMap.get(uuid);
        return lastDay != null && lastDay == gameDay;
    }

    public Map<UUID, AnimalMood> getAllAnimalMoodData() {
        return animalMoodMap;
    }

    public boolean isWild(UUID uuid) {
        return wildMap.getOrDefault(uuid, false);
    }

    public void setWild(UUID uuid, boolean wild) {
        if (wild) {
            wildMap.put(uuid, true);
        } else {
            wildMap.remove(uuid);
        }
        setDirty();
    }

    @Override
    public @NotNull CompoundTag save(@NotNull CompoundTag tag, HolderLookup.@NotNull Provider registries) {
        ListTag list = new ListTag();
        for (Map.Entry<UUID, AnimalMood> entry : animalMoodMap.entrySet()) {
            CompoundTag entryTag = new CompoundTag();
            entryTag.putUUID("kr_animal_id", entry.getKey());
            entryTag.putDouble("kr_mood_score", entry.getValue().getMoodScore());
            entryTag.putLong("kr_last_pet_day", lastPetDayMap.getOrDefault(entry.getKey(), -1L));
            entryTag.putBoolean("kr_wild", wildMap.getOrDefault(entry.getKey(), false));
            list.add(entryTag);
        }
        tag.put("kr_animal_mood_list", list);
        return tag;
    }

    public static SavedData.Factory<AnimalMoodSavedData> factory() {
        return new SavedData.Factory<>(AnimalMoodSavedData::new, AnimalMoodSavedData::new);
    }

    public static String getDataName() {
        return DATA_NAME;
    }
}