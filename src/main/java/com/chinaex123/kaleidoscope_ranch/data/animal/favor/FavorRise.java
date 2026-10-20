package com.chinaex123.kaleidoscope_ranch.data.animal.favor;

public class FavorRise {

    public static final float MOOD_RISE_FAVOR_BONUS = 0.1f;
    public static final float HUNGER_RISE_FAVOR_BONUS = 0.5f;
    public static final float PET_FAVOR_BONUS = 1.5f;

    public static float calcMoodRiseFavor(int moodDelta) {
        if (moodDelta <= 0) return 0f;
        return moodDelta * MOOD_RISE_FAVOR_BONUS;
    }

    public static float calcHungerRiseFavor(float hungerDelta) {
        if (hungerDelta <= 0) return 0f;
        return hungerDelta * HUNGER_RISE_FAVOR_BONUS;
    }

    public static float calcPetFavor() {
        return PET_FAVOR_BONUS;
    }
}