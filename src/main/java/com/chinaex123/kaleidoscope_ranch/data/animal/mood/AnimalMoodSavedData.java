package com.chinaex123.kaleidoscope_ranch.data.animal.mood;

import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.world.level.saveddata.SavedData;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * 动物心情存档数据类。
 * <p>
 * 负责将每只动物的心情分数与最近抚摸日期持久化到世界存档，
 * 并提供查询、修改、抚摸次数判断等操作。
 */
public class AnimalMoodSavedData extends SavedData {

    /** 存档数据名称 */
    private static final String DATA_NAME = "kaleidoscope_ranch_animal_mood";

    /** 动物 UUID 到心情数据的映射 */
    private final Map<UUID, AnimalMood> animalMoodMap = new HashMap<>();

    /** 动物 UUID 到最近抚摸日期的映射 */
    private final Map<UUID, Long> lastPetDayMap = new HashMap<>();

    /**
     * 构造空的动物心情存档数据。
     */
    public AnimalMoodSavedData() {
        super();
    }

    /**
     * 从 NBT 加载动物心情存档数据。
     * <p>
     * 读取每只动物的 UUID、心情分数与最近抚摸日期，
     * 抚摸日期不小于 0 时才会记录。
     *
     * @param tag        NBT 数据
     * @param registries 注册表提供者
     */
    public AnimalMoodSavedData(CompoundTag tag, HolderLookup.Provider registries) {
        super();
        ListTag list = tag.getList("kr_animal_mood_list", 10);
        for (int i = 0; i < list.size(); i++) {
            CompoundTag entry = list.getCompound(i);
            UUID uuid = entry.getUUID("kr_animal_id");
            int mood = entry.getInt("kr_mood_score");
            long lastPetDay = entry.getLong("kr_last_pet_day");
            this.animalMoodMap.put(uuid, new AnimalMood(mood));
            if (lastPetDay >= 0) {
                this.lastPetDayMap.put(uuid, lastPetDay);
            }
        }
    }

    /**
     * 获取指定动物的心情分数。
     * <p>
     * 若没有记录则返回初始心情分数。
     *
     * @param uuid 动物 UUID
     * @return 心情分数
     */
    public int getAnimalMoodScore(UUID uuid) {
        AnimalMood mood = animalMoodMap.get(uuid);
        return mood != null ? mood.getMoodScore() : AnimalMood.INITIAL_MOOD;
    }

    /**
     * 设置指定动物的心情分数。
     *
     * @param uuid      动物 UUID
     * @param moodScore 心情分数
     */
    public void setAnimalMoodScore(UUID uuid, int moodScore) {
        animalMoodMap.computeIfAbsent(uuid, k -> new AnimalMood()).setMoodScore(moodScore);
        setDirty();
    }

    /**
     * 增加指定动物的心情分数。
     *
     * @param uuid  动物 UUID
     * @param delta 变化量（可为负数）
     */
    public void addAnimalMoodScore(UUID uuid, int delta) {
        animalMoodMap.computeIfAbsent(uuid, k -> new AnimalMood()).addMoodScore(delta);
        setDirty();
    }

    /**
     * 应用心情变化量。
     * <p>
     * 变化量为 0 时不做任何操作并返回 false。
     *
     * @param uuid  动物 UUID
     * @param delta 变化量
     * @return 是否实际应用了变化
     */
    public boolean applyMoodDelta(UUID uuid, int delta) {
        if (delta == 0) return false;
        addAnimalMoodScore(uuid, delta);
        return true;
    }

    /**
     * 移除指定动物的心情与抚摸记录。
     *
     * @param uuid 动物 UUID
     */
    public void removeAnimalMood(UUID uuid) {
        animalMoodMap.remove(uuid);
        lastPetDayMap.remove(uuid);
        setDirty();
    }

    /**
     * 判断指定动物是否有心情数据。
     *
     * @param uuid 动物 UUID
     * @return 是否存在心情数据
     */
    public boolean hasAnimalMoodData(UUID uuid) {
        return animalMoodMap.containsKey(uuid);
    }

    /**
     * 判断指定动物在当天是否可以抚摸。
     * <p>
     * 没有抚摸记录或上次抚摸日期早于当天时允许抚摸。
     *
     * @param uuid    动物 UUID
     * @param gameDay 当前游戏日
     * @return 是否允许抚摸
     */
    public boolean canPet(UUID uuid, long gameDay) {
        Long lastDay = lastPetDayMap.get(uuid);
        return lastDay == null || lastDay != gameDay;
    }

    /**
     * 记录指定动物的最近抚摸日期。
     *
     * @param uuid    动物 UUID
     * @param gameDay 游戏日
     */
    public void setLastPetDay(UUID uuid, long gameDay) {
        lastPetDayMap.put(uuid, gameDay);
        setDirty();
    }

    /**
     * 判断指定动物当天是否已被抚摸。
     *
     * @param uuid    动物 UUID
     * @param gameDay 当前游戏日
     * @return 当天是否已被抚摸
     */
    public boolean hasBeenPetToday(UUID uuid, long gameDay) {
        Long lastDay = lastPetDayMap.get(uuid);
        return lastDay != null && lastDay == gameDay;
    }

    /**
     * 获取所有动物的心情数据映射。
     *
     * @return 动物 UUID 到心情数据的映射
     */
    public Map<UUID, AnimalMood> getAllAnimalMoodData() {
        return animalMoodMap;
    }

    /**
     * 将动物心情数据保存到 NBT。
     * <p>
     * 每只动物写入 UUID、心情分数与最近抚摸日期，
     * 没有抚摸记录时写入 -1。
     *
     * @param tag        NBT 数据
     * @param registries 注册表提供者
     * @return 保存后的 NBT 数据
     */
    @Override
    public @NotNull CompoundTag save(CompoundTag tag, HolderLookup.Provider registries) {
        ListTag list = new ListTag();
        for (Map.Entry<UUID, AnimalMood> entry : animalMoodMap.entrySet()) {
            CompoundTag entryTag = new CompoundTag();
            entryTag.putUUID("kr_animal_id", entry.getKey());
            entryTag.putInt("kr_mood_score", entry.getValue().getMoodScore());
            entryTag.putLong("kr_last_pet_day", lastPetDayMap.getOrDefault(entry.getKey(), -1L));
            list.add(entryTag);
        }
        tag.put("kr_animal_mood_list", list);
        return tag;
    }

    /**
     * 创建动物心情存档数据的工厂。
     *
     * @return 存档数据工厂
     */
    public static SavedData.Factory<AnimalMoodSavedData> factory() {
        return new SavedData.Factory<>(AnimalMoodSavedData::new, AnimalMoodSavedData::new);
    }

    /**
     * 获取存档数据名称。
     *
     * @return 存档数据名称
     */
    public static String getDataName() {
        return DATA_NAME;
    }
}