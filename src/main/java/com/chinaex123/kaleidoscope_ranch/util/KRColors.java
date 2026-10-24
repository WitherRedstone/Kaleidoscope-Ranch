package com.chinaex123.kaleidoscope_ranch.util;

public class KRColors {
    private KRColors() {}

    /**
     * 雾色 + 雾距离。
     *
     * @param color    雾色
     * @param distance 雾完全遮住的距离
     */
    public record Fog(int color, float distance) {}

    public static final Fog SHEEP_MILK_FOG = new Fog(0xF0EDE4, 2.0f);
    public static final Fog GOAT_MILK_FOG = new Fog(0xEFECE2, 2.0f);
    public static final Fog HORSE_MILK_FOG = new Fog(0xEBF0F2, 2.0f);
    public static final Fog DONKEY_MILK_FOG = new Fog(0xEAF0F3, 2.0f);
    public static final Fog CAMEL_MILK_FOG = new Fog(0xF5F5F0, 2.0f);
    public static final Fog ALPACA_MILK_FOG = new Fog(0xF0F0EC, 2.0f);
    public static final Fog RABBIT_MILK_FOG = new Fog(0xEFE8D8, 2.0f);
    public static final Fog PIG_MILK_FOG = new Fog(0xF2F0EB, 2.0f);

}
