package com.chinaex123.kaleidoscope_ranch.util;

import com.chinaex123.kaleidoscope_ranch.data.animal.mood.AnimalMoodSavedData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.animal.Animal;

/**
 * 抚摸调度工具类。
 * <p>
 * 负责判断指定动物当前是否允许被抚摸。
 */
public class PetScheduler {

    /**
     * 判断指定动物当前是否应该被抚摸。
     * <p>
     * 仅在服务端、动物有心情数据、且当天尚未被抚摸时返回 true。
     *
     * @param level  服务端世界
     * @param data   动物心情存档数据
     * @param animal 动物
     * @return 是否应该被抚摸
     */
    public static boolean shouldPet(ServerLevel level, AnimalMoodSavedData data, Animal animal) {
        if (level == null || level.isClientSide()) return false;
        if (!data.hasAnimalMoodData(animal.getUUID())) return false;

        long gameDay = level.getDayTime() / 24000L;
        return data.canPet(animal.getUUID(), gameDay);
    }
}