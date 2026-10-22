package com.chinaex123.kaleidoscope_ranch.entity.ai;

import com.chinaex123.kaleidoscope_ranch.mixin.server.TemptGoalAccessor;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.goal.TemptGoal;
import net.minecraft.world.item.crafting.Ingredient;

public class KRTemptGoal extends TemptGoal {

    public KRTemptGoal(PathfinderMob mob, double speedModifier, Ingredient items, boolean canScare) {
        super(mob, speedModifier, items, canScare);
    }

    @Override
    public void stop() {
        super.stop();
        ((TemptGoalAccessor) this).setCalmDown(0);
    }
}