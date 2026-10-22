package com.chinaex123.kaleidoscope_ranch.util;

import com.chinaex123.kaleidoscope_ranch.data.animal.mood.AnimalMoodSavedData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.animal.Animal;

public class PetScheduler {

    public static boolean shouldPet(ServerLevel level, AnimalMoodSavedData data, Animal animal) {
        if (level == null || level.isClientSide()) return false;
        if (!data.hasAnimalMoodData(animal.getUUID())) return false;

        long gameDay = level.getDayTime() / 24000L;
        return data.canPet(animal.getUUID(), gameDay);
    }
}