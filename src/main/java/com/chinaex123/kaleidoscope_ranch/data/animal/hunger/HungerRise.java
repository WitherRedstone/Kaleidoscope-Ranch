package com.chinaex123.kaleidoscope_ranch.data.animal.hunger;


import com.chinaex123.kaleidoscope_ranch.config.KRConfig;
import com.chinaex123.kaleidoscope_ranch.data.animal.mood.AnimalMood;

public class HungerRise {

    public static long getFeedCooldownTicks() {
        return (long) KRConfig.HUNGER_RISE_FEED_COOLDOWN_TICKS.get();
    }

    public static float calcFeedBonus(int consecutiveCount) {
        float bonus = KRConfig.HUNGER_RISE_BASE_BONUS.get().floatValue() - KRConfig.HUNGER_RISE_BONUS_DECAY.get().floatValue() * consecutiveCount;
        return Math.max(bonus, 0.0f);
    }

    public static float calcFeedGain(float currentHunger, int consecutiveCount) {
        if (currentHunger >= AnimalHunger.getMaxHunger()) return 0f;
        return calcFeedBonus(consecutiveCount);
    }

    public static int resolveConsecutiveCount(boolean cooldownExpired, int currentCount) {
        return cooldownExpired ? 0 : currentCount;
    }

    public static boolean canBreed(double mood, float hunger) {
        return mood >= AnimalMood.getInitialMood() && hunger >= AnimalHunger.getMaxHunger();
    }
}