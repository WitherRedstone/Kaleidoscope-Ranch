package com.chinaex123.kaleidoscope_ranch.data.animal.milk;

import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.world.level.saveddata.SavedData;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class AnimalMilkSavedData extends SavedData {

    private static final String DATA_NAME = "kaleidoscope_ranch_animal_milk";

    private final Map<UUID, AnimalMilk> milkMap = new HashMap<>();

    public AnimalMilkSavedData() {
        super();
    }

    public AnimalMilkSavedData(CompoundTag tag, HolderLookup.Provider registries) {
        super();
        ListTag list = tag.getList("kr_animal_milk_list", 10);
        for (int i = 0; i < list.size(); i++) {
            CompoundTag entry = list.getCompound(i);
            UUID uuid = entry.getUUID("kr_animal_id");
            double milkAmount = entry.getDouble("kr_milk_amount");
            double maxCapacity = entry.getDouble("kr_max_capacity");
            AnimalMilk milk = new AnimalMilk(milkAmount, maxCapacity);
            milkMap.put(uuid, milk);
        }
    }

    @Override
    public @NotNull CompoundTag save(CompoundTag tag, HolderLookup.Provider registries) {
        ListTag list = new ListTag();
        for (Map.Entry<UUID, AnimalMilk> entry : milkMap.entrySet()) {
            CompoundTag entryTag = new CompoundTag();
            entryTag.putUUID("kr_animal_id", entry.getKey());
            entryTag.putDouble("kr_milk_amount", entry.getValue().getMilkAmount());
            entryTag.putDouble("kr_max_capacity", entry.getValue().getMaxCapacity());
            list.add(entryTag);
        }
        tag.put("kr_animal_milk_list", list);
        return tag;
    }

    public static SavedData.Factory<AnimalMilkSavedData> factory() {
        return new SavedData.Factory<>(AnimalMilkSavedData::new, AnimalMilkSavedData::new);
    }

    public static String getDataName() {
        return DATA_NAME;
    }

    public double getMilkAmount(UUID uuid) {
        AnimalMilk milk = milkMap.get(uuid);
        return milk != null ? milk.getMilkAmount() : 0;
    }

    public double getMilkMaxCapacity(UUID uuid) {
        AnimalMilk milk = milkMap.get(uuid);
        return milk != null ? milk.getMaxCapacity() : 0;
    }

    public void setMilkAmount(UUID uuid, double amount) {
        milkMap.computeIfAbsent(uuid, k -> new AnimalMilk()).setMilkAmount(amount);
        setDirty();
    }

    public void initMilkData(UUID uuid, double maxCapacity) {
        AnimalMilk milk = new AnimalMilk();
        milk.setMaxCapacity(maxCapacity);
        milkMap.put(uuid, milk);
        setDirty();
    }

    public void addMilk(UUID uuid, double amount) {
        AnimalMilk milk = milkMap.get(uuid);
        if (milk == null) return;
        milk.addMilk(amount);
        setDirty();
    }

    public double drainMilk(UUID uuid, double amount) {
        AnimalMilk milk = milkMap.get(uuid);
        if (milk == null) return 0;
        double drained = milk.drainMilk(amount);
        if (drained > 0) setDirty();
        return drained;
    }

    public boolean hasMilkData(UUID uuid) {
        return milkMap.containsKey(uuid);
    }

    public AnimalMilk getMilkEntry(UUID uuid) {
        return milkMap.get(uuid);
    }

    public void removeMilkData(UUID uuid) {
        milkMap.remove(uuid);
        setDirty();
    }
}