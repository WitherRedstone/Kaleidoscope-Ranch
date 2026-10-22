package com.chinaex123.kaleidoscope_ranch.network;

import com.chinaex123.kaleidoscope_ranch.data.animal.favor.AnimalFavor;
import com.chinaex123.kaleidoscope_ranch.data.animal.hunger.AnimalHunger;
import com.chinaex123.kaleidoscope_ranch.data.animal.mood.AnimalMood;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class ClientAnimalData {

    private static final ClientAnimalData INSTANCE = new ClientAnimalData();
    private final Map<UUID, Integer> clientMoodMap = new HashMap<>();
    private final Map<UUID, Boolean> clientPetTodayMap = new HashMap<>();
    private final Map<UUID, Float> clientHungerMap = new HashMap<>();
    private final Map<UUID, Long> clientFeedCooldownExpireMap = new HashMap<>();
    private final Map<UUID, Long> clientTroughCooldownExpireMap = new HashMap<>();
    private final Map<UUID, Float> clientFavorMap = new HashMap<>();
    private final Map<UUID, Boolean> clientWildMap = new HashMap<>();

    public static ClientAnimalData getInstance() {
        return INSTANCE;
    }

    public void updateMoodData(Map<UUID, Integer> moodMap, Map<UUID, Boolean> petTodayMap, Map<UUID, Boolean> wildMap) {
        clientMoodMap.putAll(moodMap);
        clientPetTodayMap.putAll(petTodayMap);
        clientWildMap.putAll(wildMap);
    }

    public void updateHungerData(Map<UUID, Float> hungerMap, Map<UUID, Long> feedCooldownExpireMap, Map<UUID, Long> troughCooldownExpireMap) {
        clientHungerMap.putAll(hungerMap);
        clientFeedCooldownExpireMap.putAll(feedCooldownExpireMap);
        clientTroughCooldownExpireMap.putAll(troughCooldownExpireMap);
    }

    public int getAnimalMoodScore(UUID uuid) {
        return clientMoodMap.getOrDefault(uuid, AnimalMood.getInitialMood());
    }

    public boolean hasAnimalMoodData(UUID uuid) {
        return clientMoodMap.containsKey(uuid);
    }

    public boolean hasBeenPetToday(UUID uuid) {
        return clientPetTodayMap.getOrDefault(uuid, false);
    }

    public float getAnimalHungerValue(UUID uuid) {
        return clientHungerMap.getOrDefault(uuid, AnimalHunger.getInitialHunger());
    }

    public boolean hasAnimalHungerData(UUID uuid) {
        return clientHungerMap.containsKey(uuid);
    }

    public long getFeedCooldownRemaining(UUID uuid, long currentGameTime) {
        long expire = clientFeedCooldownExpireMap.getOrDefault(uuid, 0L);
        return Math.max(expire - currentGameTime, 0L);
    }

    public boolean hasFeedCooldown(UUID uuid, long currentGameTime) {
        return getFeedCooldownRemaining(uuid, currentGameTime) > 0;
    }

    public long getTroughFeedCooldownRemaining(UUID uuid, long currentGameTime) {
        long expire = clientTroughCooldownExpireMap.getOrDefault(uuid, 0L);
        return Math.max(expire - currentGameTime, 0L);
    }

    public boolean hasTroughFeedCooldown(UUID uuid, long currentGameTime) {
        return getTroughFeedCooldownRemaining(uuid, currentGameTime) > 0;
    }

    public void updateFavorData(Map<UUID, Float> favorMap) {
        clientFavorMap.putAll(favorMap);
    }

    public float getAnimalFavorValue(UUID uuid) {
        return clientFavorMap.getOrDefault(uuid, AnimalFavor.getInitialFavor());
    }

    public boolean hasAnimalFavorData(UUID uuid) {
        return clientFavorMap.containsKey(uuid);
    }

    public boolean isWild(UUID uuid) {
        return clientWildMap.getOrDefault(uuid, false);
    }
}