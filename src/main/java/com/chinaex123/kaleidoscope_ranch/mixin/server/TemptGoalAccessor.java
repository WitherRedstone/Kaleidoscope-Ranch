package com.chinaex123.kaleidoscope_ranch.mixin.server;

import net.minecraft.world.entity.ai.goal.TemptGoal;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(TemptGoal.class)
public interface TemptGoalAccessor {

    @Accessor("calmDown")
    void setCalmDown(int calmDown);
}