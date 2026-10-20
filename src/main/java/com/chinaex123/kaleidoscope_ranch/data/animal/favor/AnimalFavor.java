package com.chinaex123.kaleidoscope_ranch.data.animal.favor;

public class AnimalFavor {

    public static final float INITIAL_FAVOR = 0.0f;
    public static final float MIN_FAVOR = 0.0f;
    public static final float MAX_FAVOR = 10.0f;
    public static final float ABSOLUTE_MAX_FAVOR = 12.0f;

    private float favorValue;

    public AnimalFavor() {
        this(INITIAL_FAVOR);
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

    /**
     * 普通好感增长：上限为常规上限 MAX_FAVOR。
     */
    public void addFavorValue(float delta) {
        if (delta <= 0) {
            this.favorValue = clamp(this.favorValue + delta);
            return;
        }
        if (this.favorValue >= MAX_FAVOR) {
            // 已到/超过常规上限，普通正增长不再增加
            return;
        }
        this.favorValue = clamp(Math.min(this.favorValue + delta, MAX_FAVOR));
    }

    /**
     * 特殊好感增长：上限为绝对上限 ABSOLUTE_MAX_FAVOR。
     */
    public void addFavorValueSpecial(float delta) {
        this.favorValue = clamp(this.favorValue + delta);
    }

    /** 是否达到常规上限。 */
    public boolean isMaxed() {
        return favorValue >= MAX_FAVOR;
    }

    /** 是否达到绝对上限。 */
    public boolean isAbsolutelyMaxed() {
        return favorValue >= ABSOLUTE_MAX_FAVOR;
    }

    public static float clamp(float value) {
        return Math.clamp(value, MIN_FAVOR, ABSOLUTE_MAX_FAVOR);
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