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
            this.animalHungerMap.put(uuid, new AnimalHunger(hunger));
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

    @Override
    public @NotNull CompoundTag save(CompoundTag tag, HolderLookup.Provider registries) {
        ListTag list = new ListTag();
        for (Map.Entry<UUID, AnimalHunger> entry : animalHungerMap.entrySet()) {
            CompoundTag entryTag = new CompoundTag();
            entryTag.putUUID("kr_animal_id", entry.getKey());
            entryTag.putFloat("kr_hunger_value", entry.getValue().getHungerValue());
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