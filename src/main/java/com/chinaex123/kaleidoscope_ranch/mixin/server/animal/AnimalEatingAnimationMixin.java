package com.chinaex123.kaleidoscope_ranch.mixin.server.animal;

import com.chinaex123.kaleidoscope_ranch.entity.animation.KREatingAnimation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Animal.class)
public abstract class AnimalEatingAnimationMixin extends Entity implements KREatingAnimation {

    @Unique
    private int KR$eatingTicks = 0;

    public AnimalEatingAnimationMixin(EntityType<?> type, Level level) {
        super(type, level);
    }

    @Override
    public void KR$startEatingAnimation() {
        this.KR$eatingTicks = TOTAL_TICKS;
    }

    @Override
    public int KR$getEatingAnimationTicks() {
        return this.KR$eatingTicks;
    }

    @Inject(method = "aiStep", at = @At("TAIL"))
    private void KR$tickEating(CallbackInfo ci) {
        if (this.KR$eatingTicks > 0) {
            this.KR$eatingTicks--;
        }
    }

    @Inject(method = "handleEntityEvent", at = @At("HEAD"))
    private void KR$onEntityEvent(byte id, CallbackInfo ci) {
        if (id == 60) {
            this.KR$startEatingAnimation();
        }
    }
}