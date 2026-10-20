package com.chinaex123.kaleidoscope_ranch.data.animal.hunger;

public class AnimalHunger {

    public static final float INITIAL_HUNGER = 0.0f;
    public static final float MIN_HUNGER = 0.0f;
    public static final float MAX_HUNGER = 10.0f;

    private float hungerValue;

    public AnimalHunger() {
        this(INITIAL_HUNGER);
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
        return hungerValue >= MAX_HUNGER;
    }

    public static float clamp(float value) {
        return Math.clamp(value, MIN_HUNGER, MAX_HUNGER);
    }

    public static int getDisplayColor(float hungerValue) {
        if (hungerValue >= MAX_HUNGER) {
            return 0xFFAA00;
        } else if (hungerValue >= 7.0f) {
            return 0xFFFFFF;
        } else if (hungerValue >= 4.0f) {
            return 0x5599FF;
        } else {
            return 0xFF5555;
        }
    }
}
