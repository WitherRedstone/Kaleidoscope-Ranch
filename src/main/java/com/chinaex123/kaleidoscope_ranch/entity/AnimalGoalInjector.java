package com.chinaex123.kaleidoscope_ranch.entity;

import com.chinaex123.kaleidoscope_ranch.KaleidoscopeRanch;
import com.chinaex123.kaleidoscope_ranch.entity.ai.KREatFromTroughGoal;
import com.chinaex123.kaleidoscope_ranch.init.tags.KREntityTags;
import net.minecraft.world.entity.Mob;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.EntityJoinLevelEvent;

@EventBusSubscriber(modid = KaleidoscopeRanch.MODID)
public class AnimalGoalInjector {

    @SubscribeEvent
    public static void onEntityJoin(EntityJoinLevelEvent event) {
        if (event.getLevel().isClientSide()) return;
        if (!(event.getEntity() instanceof Mob mob)) return;

        if (!mob.getType().is(KREntityTags.LIVESTOCK) && !mob.getType().is(KREntityTags.POULTRY)) {
            return;
        }

        if (mob.goalSelector.getAvailableGoals().stream()
                .anyMatch(g -> g.getGoal() instanceof KREatFromTroughGoal)) {
            return;
        }

        mob.goalSelector.addGoal(5, new KREatFromTroughGoal(mob));
    }
}