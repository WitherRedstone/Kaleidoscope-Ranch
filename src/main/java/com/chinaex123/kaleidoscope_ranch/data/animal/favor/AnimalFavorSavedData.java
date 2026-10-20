package com.chinaex123.kaleidoscope_ranch.data.animal.favor;

import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.world.level.saveddata.SavedData;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class AnimalFavorSavedData extends SavedData {

    private static final String DATA_NAME = "kaleidoscope_ranch_animal_favor";

    private final Map<UUID, AnimalFavor> animalFavorMap = new HashMap<>();

    public AnimalFavorSavedData() {
        super();
    }

    public AnimalFavorSavedData(CompoundTag tag, HolderLookup.Provider registries) {
        super();
        ListTag list = tag.getList("kr_animal_favor_list", 10);
        for (int i = 0; i < list.size(); i++) {
            CompoundTag entry = list.getCompound(i);
            UUID uuid = entry.getUUID("kr_animal_id");
            float favor = entry.getFloat("kr_favor_value");
            this.animalFavorMap.put(uuid, new AnimalFavor(favor));
        }
    }

    public float getAnimalFavorValue(UUID uuid) {
        AnimalFavor favor = animalFavorMap.get(uuid);
        return favor != null ? favor.getFavorValue() : AnimalFavor.INITIAL_FAVOR;
    }

    public void setAnimalFavorValue(UUID uuid, float favorValue) {
        animalFavorMap.computeIfAbsent(uuid, k -> new AnimalFavor()).setFavorValue(favorValue);
        setDirty();
    }

    public void addAnimalFavorValue(UUID uuid, float delta) {
        animalFavorMap.computeIfAbsent(uuid, k -> new AnimalFavor()).addFavorValue(delta);
        setDirty();
    }

    public boolean applyFavorDelta(UUID uuid, float delta) {
        if (delta == 0) return false;
        addAnimalFavorValue(uuid, delta);
        return true;
    }

    public void removeAnimalFavor(UUID uuid) {
        animalFavorMap.remove(uuid);
        setDirty();
    }

    public boolean hasAnimalFavorData(UUID uuid) {
        return animalFavorMap.containsKey(uuid);
    }

    public Map<UUID, AnimalFavor> getAllAnimalFavorData() {
        return animalFavorMap;
    }

    @Override
    public @NotNull CompoundTag save(CompoundTag tag, HolderLookup.Provider registries) {
        ListTag list = new ListTag();
        for (Map.Entry<UUID, AnimalFavor> entry : animalFavorMap.entrySet()) {
            CompoundTag entryTag = new CompoundTag();
            entryTag.putUUID("kr_animal_id", entry.getKey());
            entryTag.putFloat("kr_favor_value", entry.getValue().getFavorValue());
            list.add(entryTag);
        }
        tag.put("kr_animal_favor_list", list);
        return tag;
    }

    public static SavedData.Factory<AnimalFavorSavedData> factory() {
        return new SavedData.Factory<>(AnimalFavorSavedData::new, AnimalFavorSavedData::new);
    }

    public static String getDataName() {
        return DATA_NAME;
    }
}