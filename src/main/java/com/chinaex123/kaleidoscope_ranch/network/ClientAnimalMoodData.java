package com.chinaex123.kaleidoscope_ranch.network;

import com.chinaex123.kaleidoscope_ranch.data.animal.mood.MoodDecline;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class ClientAnimalMoodData {

    private static final ClientAnimalMoodData INSTANCE = new ClientAnimalMoodData();
    private final Map<UUID, Integer> clientMoodMap = new HashMap<>();
    private final Map<UUID, Boolean> clientPetTodayMap = new HashMap<>();

    public static ClientAnimalMoodData getInstance() {
        return INSTANCE;
    }

    public void updateMoodData(Map<UUID, Integer> moodMap, Map<UUID, Boolean> petTodayMap) {
        clientMoodMap.putAll(moodMap);
        clientPetTodayMap.putAll(petTodayMap);
    }

    public int getAnimalMoodScore(UUID uuid) {
        return clientMoodMap.getOrDefault(uuid, MoodDecline.INITIAL_MOOD);
    }

    public boolean hasAnimalMoodData(UUID uuid) {
        return clientMoodMap.containsKey(uuid);
    }

    public boolean hasBeenPetToday(UUID uuid) {
        return clientPetTodayMap.getOrDefault(uuid, false);
    }
}