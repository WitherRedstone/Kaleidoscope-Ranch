package com.chinaex123.kaleidoscope_ranch.mixin;

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
    private int kaleidoscopeRanch$eatingTicks = 0;

    public AnimalEatingAnimationMixin(EntityType<?> type, Level level) {
        super(type, level);
    }

    @Override
    public void kaleidoscopeRanch$startEatingAnimation() {
        this.kaleidoscopeRanch$eatingTicks = TOTAL_TICKS;
    }

    @Override
    public int kaleidoscopeRanch$getEatingAnimationTicks() {
        return this.kaleidoscopeRanch$eatingTicks;
    }

    @Inject(method = "aiStep", at = @At("TAIL"))
    private void kaleidoscopeRanch$tickEating(CallbackInfo ci) {
        if (this.kaleidoscopeRanch$eatingTicks > 0) {
            this.kaleidoscopeRanch$eatingTicks--;
        }
    }

    @Inject(method = "handleEntityEvent", at = @At("HEAD"))
    private void kaleidoscopeRanch$onEntityEvent(byte id, CallbackInfo ci) {
        if (id == 60) {
            this.kaleidoscopeRanch$startEatingAnimation();
        }
    }
}