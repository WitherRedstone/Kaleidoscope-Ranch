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
            int level = entry.getInt("kr_favor_level");
            int exp = entry.getInt("kr_favor_exp");
            this.animalFavorMap.put(uuid, new AnimalFavor(level, exp));
        }
    }

    public int getAnimalFavorExp(UUID uuid) {
        AnimalFavor favor = animalFavorMap.get(uuid);
        return favor != null ? favor.getFavorExp() : 0;
    }

    public int getAnimalFavorLevel(UUID uuid) {
        AnimalFavor favor = animalFavorMap.get(uuid);
        return favor != null ? favor.getFavorLevel() : 0;
    }

    public void setAnimalFavorExp(UUID uuid, int exp) {
        animalFavorMap.computeIfAbsent(uuid, k -> new AnimalFavor()).setFavorExp(exp);
        setDirty();
    }

    public void setAnimalFavorLevel(UUID uuid, int level) {
        animalFavorMap.computeIfAbsent(uuid, k -> new AnimalFavor()).setFavorLevel(level);
        setDirty();
    }

    public void addAnimalFavorExp(UUID uuid, int delta) {
        animalFavorMap.computeIfAbsent(uuid, k -> new AnimalFavor()).addFavorExp(delta);
        setDirty();
    }

    public boolean applyFavorExpDelta(UUID uuid, int delta) {
        if (delta == 0) return false;
        addAnimalFavorExp(uuid, delta);
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
    public @NotNull CompoundTag save(@NotNull CompoundTag tag, HolderLookup.@NotNull Provider registries) {
        ListTag list = new ListTag();
        for (Map.Entry<UUID, AnimalFavor> entry : animalFavorMap.entrySet()) {
            CompoundTag entryTag = new CompoundTag();
            entryTag.putUUID("kr_animal_id", entry.getKey());
            entryTag.putInt("kr_favor_level", entry.getValue().getFavorLevel());
            entryTag.putInt("kr_favor_exp", entry.getValue().getFavorExp());
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