package com.chinaex123.kaleidoscope_ranch.mixin.client;

import net.minecraft.client.model.ChickenModel;
import net.minecraft.client.model.geom.ModelPart;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(ChickenModel.class)
public interface ChickenModelAccessor {

    @Accessor("head")
    ModelPart kaleidoscopeRanch$getHead();
}