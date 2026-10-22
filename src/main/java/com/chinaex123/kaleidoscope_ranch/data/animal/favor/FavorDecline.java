package com.chinaex123.kaleidoscope_ranch.data.animal.favor;

import com.chinaex123.kaleidoscope_ranch.config.KRConfig;

public class FavorDecline {

    public static float calcHurtPenalty() {
        return KRConfig.FAVOR_DECLINE_HURT_AMOUNT.get().floatValue();
    }
}