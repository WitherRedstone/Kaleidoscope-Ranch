package com.chinaex123.kaleidoscope_ranch.entity;

public class BreedContext {
    private static boolean breeding = false;

    public static boolean isBreeding() {
        return breeding;
    }

    public static void setBreeding(boolean value) {
        breeding = value;
    }
}