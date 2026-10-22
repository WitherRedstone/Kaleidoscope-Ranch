package com.chinaex123.kaleidoscope_ranch.data.animal.favor;

import com.chinaex123.kaleidoscope_ranch.config.KRConfig;

public class AnimalFavor {

    public static float getInitialFavor() {
        return KRConfig.FAVOR_INITIAL.get().floatValue();
    }

    public static float getMinFavor() {
        return KRConfig.FAVOR_MIN.get().floatValue();
    }

    public static float getMaxFavor() {
        return KRConfig.FAVOR_MAX.get().floatValue();
    }

    public static float getAbsoluteMaxFavor() {
        return KRConfig.FAVOR_ABSOLUTE_MAX.get().floatValue();
    }

    private float favorValue;

    public AnimalFavor() {
        this(getInitialFavor());
    }

    public AnimalFavor(float favorValue) {
        this.favorValue = clamp(favorValue);
    }

    public float getFavorValue() {
        return favorValue;
    }

    public void setFavorValue(float favorValue) {
        this.favorValue = clamp(favorValue);
    }

    public void addFavorValue(float delta) {
        if (delta <= 0) {
            this.favorValue = clamp(this.favorValue + delta);
            return;
        }
        if (this.favorValue >= getMaxFavor()) {
            return;
        }
        this.favorValue = clamp(Math.min(this.favorValue + delta, getMaxFavor()));
    }

    public void addFavorValueSpecial(float delta) {
        this.favorValue = clamp(this.favorValue + delta);
    }

    public boolean isMaxed() {
        return favorValue >= getMaxFavor();
    }

    public boolean isAbsolutelyMaxed() {
        return favorValue >= getAbsoluteMaxFavor();
    }

    public static float clamp(float value) {
        return Math.clamp(value, getMinFavor(), getAbsoluteMaxFavor());
    }

    public static int getDisplayColor(float favorValue) {
        if (favorValue >= 11.0f) {
            return 0xFFAA00;
        } else if (favorValue >= 7.0f) {
            return 0xFFFFFF;
        } else if (favorValue >= 4.0f) {
            return 0x5599FF;
        } else {
            return 0xFF5555;
        }
    }
}