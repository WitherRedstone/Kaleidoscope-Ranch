package com.chinaex123.kaleidoscope_ranch.data.animal.mood;

/**
 * 动物心情类。
 * <p>
 * 负责存储与调整动物的心情分数，
 * 提供分数上下限约束、阈值判断与显示颜色计算。
 */
public class AnimalMood {

    /** 初始心情分数 */
    public static final int INITIAL_MOOD = 100;
    /** 心情分数最小值 */
    public static final int MIN_MOOD = 0;
    /** 心情分数最大值 */
    public static final int MAX_MOOD = 120;

    /** 当前心情分数 */
    private int moodScore;

    /**
     * 使用初始心情分数构造动物心情。
     */
    public AnimalMood() {
        this(INITIAL_MOOD);
    }

    /**
     * 使用指定心情分数构造动物心情。
     *
     * @param moodScore 心情分数
     */
    public AnimalMood(int moodScore) {
        this.moodScore = clamp(moodScore);
    }

    /**
     * 获取当前心情分数。
     *
     * @return 心情分数
     */
    public int getMoodScore() {
        return moodScore;
    }

    /**
     * 设置心情分数。
     *
     * @param moodScore 心情分数
     */
    public void setMoodScore(int moodScore) {
        this.moodScore = clamp(moodScore);
    }

    /**
     * 增加心情分数。
     *
     * @param delta 变化量
     */
    public void addMoodScore(int delta) {
        this.moodScore = clamp(this.moodScore + delta);
    }

    /**
     * 判断当前心情是否高于初始值。
     *
     * @return 是否高于初始心情
     */
    public boolean isAboveDefault() {
        return moodScore > INITIAL_MOOD;
    }

    /**
     * 判断当前心情是否低于指定阈值。
     *
     * @param threshold 阈值
     * @return 是否低于阈值
     */
    public boolean isBelowThreshold(int threshold) {
        return moodScore < threshold;
    }

    /**
     * 将心情分数限制在允许范围内。
     *
     * @param value 原始值
     * @return 限制后的值
     */
    public static int clamp(int value) {
        return Math.clamp(value, MIN_MOOD, MAX_MOOD);
    }

    /**
     * 获取心情分数对应的显示颜色。
     * <p>
     * 高于初始值显示金色；61 及以上显示白色；
     * 30 及以上显示蓝色；其余显示红色。
     *
     * @param moodScore 心情分数
     * @return ARGB 颜色值
     */
    public static int getDisplayColor(int moodScore) {
        if (moodScore > INITIAL_MOOD) {
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