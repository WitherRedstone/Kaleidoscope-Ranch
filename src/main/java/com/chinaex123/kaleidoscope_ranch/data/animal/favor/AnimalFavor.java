package com.chinaex123.kaleidoscope_ranch.data.animal.favor;

import com.chinaex123.kaleidoscope_ranch.config.KRConfig;

import java.util.List;

public class AnimalFavor {

    public static final int MAX_LEVEL = 12;

    private int favorExp;

    public AnimalFavor() {
        this(0);
    }

    public AnimalFavor(int favorExp) {
        this.favorExp = Math.max(0, favorExp);
    }

    public int getFavorExp() {
        return favorExp;
    }

    public void setFavorExp(int favorExp) {
        this.favorExp = Math.max(0, favorExp);
    }

    public void addFavorExp(int delta) {
        this.favorExp = Math.max(0, this.favorExp + delta);
    }

    public int getFavorLevel() {
        return expToLevel(favorExp);
    }

    public int getExpForNextLevel() {
        int level = getFavorLevel();
        if (level >= MAX_LEVEL) return 0;
        return getLevelUpExp(level);
    }

    public int getExpInCurrentLevel() {
        int level = getFavorLevel();
        if (level >= MAX_LEVEL) return 0;
        int levelStartExp = levelToExp(level);
        return favorExp - levelStartExp;
    }

    public boolean isMaxed() {
        return getFavorLevel() >= 10;
    }

    public boolean isAbsolutelyMaxed() {
        return getFavorLevel() >= MAX_LEVEL;
    }

    public static int expToLevel(int exp) {
        List<? extends Integer> table = KRConfig.FAVOR_EXP_TABLE.get();
        int cumulative = 0;
        for (int i = 0; i < table.size(); i++) {
            cumulative += table.get(i);
            if (exp < cumulative) return i;
        }
        return table.size();
    }

    public static int levelToExp(int level) {
        if (level <= 0) return 0;
        List<? extends Integer> table = KRConfig.FAVOR_EXP_TABLE.get();
        int cumulative = 0;
        for (int i = 0; i < Math.min(level, table.size()); i++) {
            cumulative += table.get(i);
        }
        return cumulative;
    }

    public static int getLevelUpExp(int level) {
        List<? extends Integer> table = KRConfig.FAVOR_EXP_TABLE.get();
        if (level < 0 || level >= table.size()) return 0;
        return table.get(level);
    }

    public static int getDisplayColor(int level) {
        if (level >= 11) {
            return 0xFFAA00;
        } else if (level >= 8) {
            return 0x55FF55;
        } else if (level >= 5) {
            return 0xFFFFFF;
        } else if (level >= 3) {
            return 0x5599FF;
        } else {
            return 0xFF5555;
        }
    }
}