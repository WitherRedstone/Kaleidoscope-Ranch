package com.chinaex123.kaleidoscope_ranch.data.animal.favor;

import com.chinaex123.kaleidoscope_ranch.config.KRConfig;
import net.minecraft.util.RandomSource;

public class FavorDecline {

    public static int calcHurtPenaltyExp(RandomSource random) {
        return random.nextIntBetweenInclusive(
                KRConfig.FAVOR_DECLINE_HURT_EXP_MIN.get(),
                KRConfig.FAVOR_DECLINE_HURT_EXP_MAX.get());
    }

    public static int calcRainPenaltyExp() {
        return KRConfig.FAVOR_DECLINE_RAIN_EXP.get();
    }
}