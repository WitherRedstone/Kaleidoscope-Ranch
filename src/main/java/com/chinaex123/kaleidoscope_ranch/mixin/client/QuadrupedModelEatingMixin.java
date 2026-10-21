package com.chinaex123.kaleidoscope_ranch.mixin.client;

import com.chinaex123.kaleidoscope_ranch.entity.animation.KREatingAnimation;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.QuadrupedModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.world.entity.Entity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(QuadrupedModel.class)
public abstract class QuadrupedModelEatingMixin {

    @Inject(method = "setupAnim", at = @At("TAIL"))
    private void kaleidoscopeRanch$applyEating(Entity entity, float limbSwing, float limbSwingAmount,
                                               float ageInTicks, float netHeadYaw, float headPitch,
                                               CallbackInfo ci) {
        if (!(entity instanceof KREatingAnimation anim)) return;

        float progress = anim.kaleidoscopeRanch$getEatingAnimationProgress(
                Minecraft.getInstance().getTimer().getGameTimeDeltaPartialTick(false));
        if (progress <= 0.0F) return;

        ModelPart head = ((QuadrupedModelAccessor) this).kaleidoscopeRanch$getHead();
        head.xRot += progress * 0.7F;
    }
}