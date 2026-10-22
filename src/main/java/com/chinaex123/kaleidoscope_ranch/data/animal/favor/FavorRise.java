package com.chinaex123.kaleidoscope_ranch.data.animal.favor;

import com.chinaex123.kaleidoscope_ranch.config.KRConfig;

public class FavorRise {

    public static float calcMoodRiseFavor(double moodDelta) {
        if (moodDelta <= 0) return 0f;
        return (float) moodDelta * KRConfig.FAVOR_RISE_MOOD_FACTOR.get().floatValue();
    }

    public static float calcHungerRiseFavor(float hungerDelta) {
        if (hungerDelta <= 0) return 0f;
        return hungerDelta * KRConfig.FAVOR_RISE_HUNGER_FACTOR.get().floatValue();
    }

    public static float calcPetFavor() {
        return KRConfig.FAVOR_RISE_PET_BONUS.get().floatValue();
    }
}