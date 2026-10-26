package com.chinaex123.kaleidoscope_ranch.entity;

import com.chinaex123.kaleidoscope_ranch.KaleidoscopeRanch;
import com.chinaex123.kaleidoscope_ranch.entity.ai.CamelEatFromGroundGoal;
import com.chinaex123.kaleidoscope_ranch.entity.ai.DonkeyEatFromGroundGoal;
import com.chinaex123.kaleidoscope_ranch.entity.ai.GoatEatFromGroundGoal;
import com.chinaex123.kaleidoscope_ranch.entity.ai.RabbitEatFromGroundGoal;
import com.chinaex123.kaleidoscope_ranch.entity.ai.MooshroomEatFromGroundGoal;
import com.chinaex123.kaleidoscope_ranch.entity.ai.HorseEatFromGroundGoal;
import com.chinaex123.kaleidoscope_ranch.entity.ai.LlamaEatFromGroundGoal;
import com.chinaex123.kaleidoscope_ranch.entity.ai.KREatFromTroughGoal;
import com.chinaex123.kaleidoscope_ranch.init.tags.KREntityTags;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.goal.AvoidEntityGoal;
import net.minecraft.world.entity.animal.Rabbit;
import net.minecraft.world.entity.animal.MushroomCow;
import net.minecraft.world.entity.animal.goat.Goat;
import net.minecraft.world.entity.animal.camel.Camel;
import net.minecraft.world.entity.animal.horse.Donkey;
import net.minecraft.world.entity.animal.horse.Horse;
import net.minecraft.world.entity.animal.horse.Llama;
import net.minecraft.world.entity.player.Player;
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

        if (mob instanceof Horse horse) {
            if (mob.goalSelector.getAvailableGoals().stream()
                    .noneMatch(g -> g.getGoal() instanceof HorseEatFromGroundGoal)) {
                mob.goalSelector.addGoal(4, new HorseEatFromGroundGoal(horse));
            }
        }

        if (mob instanceof Donkey donkey) {
            if (mob.goalSelector.getAvailableGoals().stream()
                    .noneMatch(g -> g.getGoal() instanceof DonkeyEatFromGroundGoal)) {
                mob.goalSelector.addGoal(4, new DonkeyEatFromGroundGoal(donkey));
            }
        }

        if (mob instanceof Llama llama) {
            if (mob.goalSelector.getAvailableGoals().stream()
                    .noneMatch(g -> g.getGoal() instanceof LlamaEatFromGroundGoal)) {
                mob.goalSelector.addGoal(4, new LlamaEatFromGroundGoal(llama));
            }
        }

        if (mob instanceof Camel camel) {
            if (mob.goalSelector.getAvailableGoals().stream()
                    .noneMatch(g -> g.getGoal() instanceof CamelEatFromGroundGoal)) {
                mob.goalSelector.addGoal(4, new CamelEatFromGroundGoal(camel));
            }
        }

        if (mob instanceof Goat goat) {
            if (mob.goalSelector.getAvailableGoals().stream()
                    .noneMatch(g -> g.getGoal() instanceof GoatEatFromGroundGoal)) {
                mob.goalSelector.addGoal(4, new GoatEatFromGroundGoal(goat));
            }
        }

        if (mob instanceof Rabbit rabbit) {
            mob.goalSelector.getAvailableGoals().stream()
                    .filter(g -> g.getGoal() instanceof AvoidEntityGoal)
                    .toList()
                    .forEach(g -> mob.goalSelector.removeGoal(g.getGoal()));
            if (mob.goalSelector.getAvailableGoals().stream()
                    .noneMatch(g -> g.getGoal() instanceof RabbitEatFromGroundGoal)) {
                mob.goalSelector.addGoal(4, new RabbitEatFromGroundGoal(rabbit));
            }
        }

        if (mob instanceof MushroomCow mooshroom) {
            if (mob.goalSelector.getAvailableGoals().stream()
                    .noneMatch(g -> g.getGoal() instanceof MooshroomEatFromGroundGoal)) {
                mob.goalSelector.addGoal(4, new MooshroomEatFromGroundGoal(mooshroom));
            }
        }
    }
}