package com.chinaex123.kaleidoscope_ranch.data.animal.hunger;


import com.chinaex123.kaleidoscope_ranch.data.animal.mood.AnimalMood;

public class HungerRise {

    public static final long FEED_COOLDOWN_TICKS = 12000L;
    public static final float BASE_FEED_BONUS = 1.0f;
    public static final float FEED_BONUS_DECAY = 0.2f;

    public static float calcFeedBonus(int consecutiveCount) {
        float bonus = BASE_FEED_BONUS - FEED_BONUS_DECAY * consecutiveCount;
        return Math.max(bonus, 0.0f);
    }

    public static float calcFeedGain(float currentHunger, int consecutiveCount) {
        if (currentHunger >= AnimalHunger.MAX_HUNGER) return 0f;
        return calcFeedBonus(consecutiveCount);
    }

    public static int resolveConsecutiveCount(boolean cooldownExpired, int currentCount) {
        return cooldownExpired ? 0 : currentCount;
    }

    public static boolean canBreed(int mood, float hunger) {
        return mood >= AnimalMood.INITIAL_MOOD && hunger >= AnimalHunger.MAX_HUNGER;
    }
}