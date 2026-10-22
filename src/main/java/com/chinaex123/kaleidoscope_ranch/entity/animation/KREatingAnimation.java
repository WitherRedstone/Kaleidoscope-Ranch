package com.chinaex123.kaleidoscope_ranch.entity.animation;

public interface KREatingAnimation {
    int TOTAL_TICKS = 40;

    void KR$startEatingAnimation();

    int KR$getEatingAnimationTicks();

    default float kaleidoscopeRanch$getEatingAnimationProgress(float partialTick) {
        float elapsed = TOTAL_TICKS - Math.max(0.0F, (float) this.KR$getEatingAnimationTicks() - partialTick);
        if (elapsed < 10.0F) {
            return elapsed / 10.0F;
        } else if (elapsed > 30.0F) {
            return Math.max(0.0F, (TOTAL_TICKS - elapsed) / 10.0F);
        } else {
            return 1.0F;
        }
    }
}