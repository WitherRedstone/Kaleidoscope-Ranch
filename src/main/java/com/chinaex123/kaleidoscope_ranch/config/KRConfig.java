package com.chinaex123.kaleidoscope_ranch.config;

import net.neoforged.neoforge.common.ModConfigSpec;

public class KRConfig {
    public static final ModConfigSpec SPEC;

    static {
    ModConfigSpec.Builder builder = new ModConfigSpec.Builder();

        builder.comment("通用配置").push("Common Config");

        builder.pop();

        SPEC = builder.build();
    }
}
