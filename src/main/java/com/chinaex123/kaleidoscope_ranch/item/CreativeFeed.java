package com.chinaex123.kaleidoscope_ranch.item;

import com.chinaex123.kaleidoscope_ranch.data.animal.favor.AnimalFavor;
import com.chinaex123.kaleidoscope_ranch.data.animal.favor.AnimalFavorSavedData;
import com.chinaex123.kaleidoscope_ranch.data.animal.hunger.AnimalHunger;
import com.chinaex123.kaleidoscope_ranch.data.animal.hunger.AnimalHungerSavedData;
import com.chinaex123.kaleidoscope_ranch.data.animal.mood.AnimalMood;
import com.chinaex123.kaleidoscope_ranch.data.animal.mood.AnimalMoodSavedData;
import com.chinaex123.kaleidoscope_ranch.entity.AnimalFavorEventHandler;
import com.chinaex123.kaleidoscope_ranch.entity.AnimalHungerEventHandler;
import com.chinaex123.kaleidoscope_ranch.entity.AnimalMoodEventHandler;
import com.chinaex123.kaleidoscope_ranch.init.KREntityTags;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

public class CreativeFeed extends Item {

    public CreativeFeed(Properties properties) {
        super(properties);
    }

    @Override
    public @NotNull InteractionResult interactLivingEntity(ItemStack stack, Player player, LivingEntity entity, InteractionHand hand) {
        if (player.level().isClientSide()) return InteractionResult.PASS;
        if (!(entity instanceof Animal animal)) return InteractionResult.PASS;
        if (!KREntityTags.isManagedAnimal(animal)) return InteractionResult.PASS;

        ServerLevel level = (ServerLevel) player.level();
        UUID uuid = animal.getUUID();

        AnimalMoodSavedData moodData = AnimalMoodEventHandler.getAnimalMoodData(level);
        AnimalHungerSavedData hungerData = AnimalHungerEventHandler.getAnimalHungerData(level);
        AnimalFavorSavedData favorData = AnimalFavorEventHandler.getAnimalFavorData(level);

        if (!moodData.hasAnimalMoodData(uuid) || !hungerData.hasAnimalHungerData(uuid) || !favorData.hasAnimalFavorData(uuid)) {
            return InteractionResult.PASS;
        }

        moodData.setAnimalMoodScore(uuid, AnimalMood.getMaxMood());
        hungerData.setAnimalHungerValue(uuid, AnimalHunger.getMaxHunger());
        favorData.setAnimalFavorValue(uuid, AnimalFavor.getMaxFavor());

        AnimalMoodEventHandler.syncMoodToTrackingPlayers(animal, moodData);
        AnimalHungerEventHandler.syncHungerToTrackingPlayers(animal, hungerData);
        AnimalFavorEventHandler.syncFavorToTrackingPlayers(animal, favorData);

        level.sendParticles(ParticleTypes.HEART, animal.getX(), animal.getY() + animal.getBbHeight() + 0.5, animal.getZ(),
                8, 0.5, 0.5, 0.5, 0.1);

        if (!player.hasInfiniteMaterials()) {
            stack.shrink(1);
        }

        player.swing(hand);
        return InteractionResult.sidedSuccess(level.isClientSide());
    }
}