package com.chinaex123.kaleidoscope_ranch.data.animal.mood;

import com.chinaex123.kaleidoscope_ranch.config.KRConfig;

public class AnimalMood {

    public static int getInitialMood() {
        return KRConfig.MOOD_INITIAL.get();
    }

    public static int getMinMood() {
        return KRConfig.MOOD_MIN.get();
    }

    public static int getMaxMood() {
        return KRConfig.MOOD_MAX.get();
    }

    public static int getAbsoluteMaxMood() {
        return KRConfig.MOOD_ABSOLUTE_MAX.get();
    }

    private int moodScore;

    public AnimalMood() {
        this(getInitialMood());
    }

    public AnimalMood(int moodScore) {
        this.moodScore = clamp(moodScore);
    }

    public int getMoodScore() {
        return moodScore;
    }

    public void setMoodScore(int moodScore) {
        this.moodScore = clamp(moodScore);
    }

    public void addMoodScore(int delta) {
        this.moodScore = clamp(this.moodScore + delta);
    }

    public boolean isAboveDefault() {
        return moodScore > getInitialMood();
    }

    public boolean isBelowThreshold(int threshold) {
        return moodScore < threshold;
    }

    public static int clamp(int value) {
        return Math.clamp(value, getMinMood(), getMaxMood());
    }

    public static int getDisplayColor(int moodScore) {
        if (moodScore > getInitialMood()) {
            return 0xFFAA00;
        } else if (moodScore >= 61) {
            return 0xFFFFFF;
        } else if (moodScore >= 30) {
            return 0x5599FF;
        } else {
            return 0xFF5555;
        }
    }
}