package com.chinaex123.kaleidoscope_ranch.data.animal.mood;

import com.chinaex123.kaleidoscope_ranch.config.KRConfig;

public class AnimalMood {

    public static double getInitialMood() {
        return KRConfig.MOOD_INITIAL.get();
    }

    public static double getMinMood() {
        return KRConfig.MOOD_MIN.get();
    }

    public static double getMaxMood() {
        return KRConfig.MOOD_MAX.get();
    }

    public static double getAbsoluteMaxMood() {
        return KRConfig.MOOD_ABSOLUTE_MAX.get();
    }

    private double moodScore;

    public AnimalMood() {
        this(getInitialMood());
    }

    public AnimalMood(double moodScore) {
        this.moodScore = clamp(moodScore);
    }

    public double getMoodScore() {
        return moodScore;
    }

    public void setMoodScore(double moodScore) {
        this.moodScore = clamp(moodScore);
    }

    public void addMoodScore(double delta) {
        this.moodScore = clamp(this.moodScore + delta);
    }

    public boolean isAboveDefault() {
        return moodScore > getInitialMood();
    }

    public boolean isBelowThreshold(double threshold) {
        return moodScore < threshold;
    }

    public static double clamp(double value) {
        return Math.clamp(value, getMinMood(), getMaxMood());
    }

    public static int getDisplayColor(double moodScore) {
        if (moodScore > getInitialMood()) {
            return 0xFFAA00;
        } else if (moodScore >= 71.0) {
            return 0xFFFFFF;
        } else if (moodScore >= 30.0) {
            return 0x5599FF;
        } else {
            return 0xFF5555;
        }
    }
}