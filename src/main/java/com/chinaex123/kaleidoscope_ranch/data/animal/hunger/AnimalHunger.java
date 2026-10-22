package com.chinaex123.kaleidoscope_ranch.data.animal.hunger;

import com.chinaex123.kaleidoscope_ranch.config.KRConfig;

public class AnimalHunger {

    public static float getInitialHunger() {
        return KRConfig.HUNGER_INITIAL.get().floatValue();
    }

    public static float getMinHunger() {
        return KRConfig.HUNGER_MIN.get().floatValue();
    }

    public static float getMaxHunger() {
        return KRConfig.HUNGER_MAX.get().floatValue();
    }

    private float hungerValue;

    public AnimalHunger() {
        this(getInitialHunger());
    }

    public AnimalHunger(float hungerValue) {
        this.hungerValue = clamp(hungerValue);
    }

    public float getHungerValue() {
        return hungerValue;
    }

    public void setHungerValue(float hungerValue) {
        this.hungerValue = clamp(hungerValue);
    }

    public void addHungerValue(float delta) {
        this.hungerValue = clamp(this.hungerValue + delta);
    }

    public boolean isFull() {
        return hungerValue >= getMaxHunger();
    }

    public static float clamp(float value) {
        return Math.clamp(value, getMinHunger(), getMaxHunger());
    }

    public static int getDisplayColor(float hungerValue) {
        if (hungerValue >= 7.0f) {
            return 0xFFFFFF;
        } else if (hungerValue >= 3.0f) {
            return 0x5599FF;
        } else {
            return 0xFF5555;
        }
    }
}
