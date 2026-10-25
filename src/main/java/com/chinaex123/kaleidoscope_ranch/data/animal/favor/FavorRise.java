package com.chinaex123.kaleidoscope_ranch.data.animal.favor;

import com.chinaex123.kaleidoscope_ranch.config.KRConfig;
import net.minecraft.util.RandomSource;

public class FavorRise {

    public static int calcMoodRiseFavorExp(double moodDelta, RandomSource random) {
        if (moodDelta <= 0) return 0;
        int total = 0;
        int wholePoints = (int) moodDelta;
        for (int i = 0; i < wholePoints; i++) {
            total += random.nextIntBetweenInclusive(
                    KRConfig.FAVOR_RISE_MOOD_EXP_MIN.get(),
                    KRConfig.FAVOR_RISE_MOOD_EXP_MAX.get());
        }
        double fraction = moodDelta - wholePoints;
        if (fraction > 0 && random.nextDouble() < fraction) {
            total += random.nextIntBetweenInclusive(
                    KRConfig.FAVOR_RISE_MOOD_EXP_MIN.get(),
                    KRConfig.FAVOR_RISE_MOOD_EXP_MAX.get());
        }
        return total;
    }

    public static int calcHungerRiseFavorExp(float hungerDelta, RandomSource random) {
        if (hungerDelta <= 0) return 0;
        int total = 0;
        int wholePoints = (int) hungerDelta;
        for (int i = 0; i < wholePoints; i++) {
            total += random.nextIntBetweenInclusive(
                    KRConfig.FAVOR_RISE_HUNGER_EXP_MIN.get(),
                    KRConfig.FAVOR_RISE_HUNGER_EXP_MAX.get());
        }
        double fraction = hungerDelta - wholePoints;
        if (fraction > 0 && random.nextDouble() < fraction) {
            total += random.nextIntBetweenInclusive(
                    KRConfig.FAVOR_RISE_HUNGER_EXP_MIN.get(),
                    KRConfig.FAVOR_RISE_HUNGER_EXP_MAX.get());
        }
        return total;
    }

    public static int calcPetFavorExp(RandomSource random) {
        return random.nextIntBetweenInclusive(
                KRConfig.FAVOR_RISE_PET_EXP_MIN.get(),
                KRConfig.FAVOR_RISE_PET_EXP_MAX.get());
    }
}