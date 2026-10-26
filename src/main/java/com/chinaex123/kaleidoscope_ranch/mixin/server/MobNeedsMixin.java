package com.chinaex123.kaleidoscope_ranch.mixin.server;

import com.chinaex123.kaleidoscope_ranch.config.KRConfig;
import com.chinaex123.kaleidoscope_ranch.data.animal.favor.*;
import com.chinaex123.kaleidoscope_ranch.data.animal.hunger.*;
import com.chinaex123.kaleidoscope_ranch.data.animal.mood.*;
import com.chinaex123.kaleidoscope_ranch.data.animal.milk.*;
import com.chinaex123.kaleidoscope_ranch.entity.*;
import com.chinaex123.kaleidoscope_ranch.init.tags.KREntityTags;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.animal.Cow;
import net.minecraft.world.entity.animal.goat.Goat;
import net.minecraft.world.entity.animal.Pig;
import net.minecraft.world.entity.animal.Sheep;
import net.minecraft.world.entity.animal.camel.Camel;
import net.minecraft.world.entity.animal.horse.Donkey;
import net.minecraft.world.entity.animal.horse.Horse;
import net.minecraft.world.entity.animal.horse.Llama;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.UUID;

@Mixin(Mob.class)
abstract class MobNeedsMixin {

    @Unique
    private static final int SPREAD_FACTOR = 20;


    @Inject(method = "serverAiStep", at = @At("TAIL"))
    private void KR$tickAnimalNeeds(CallbackInfo ci) {
        Mob self = (Mob) (Object) this;
        if (!(self instanceof Animal animal)) return;
        if (!KREntityTags.isManagedAnimal(animal)) return;

        Level level = animal.level();
        if (level.isClientSide()) return;
        if (!(level instanceof ServerLevel serverLevel)) return;

        long gameTime = serverLevel.getGameTime();
        int idMod = Math.floorMod(animal.getId(), SPREAD_FACTOR);
        int timeMod = Math.floorMod(gameTime, SPREAD_FACTOR);
        if (idMod != timeMod) return;

        UUID uuid = animal.getUUID();

        KR$tickMood(serverLevel, animal, uuid, gameTime);
        KR$tickHunger(serverLevel, animal, uuid, gameTime);
        KR$tickMilk(serverLevel, animal, uuid, gameTime);
    }

    @Unique
    private void KR$tickMood(ServerLevel level, Animal animal, UUID uuid, long gameTime) {
        AnimalMoodSavedData data = AnimalMoodEventHandler.getAnimalMoodData(level);
        if (!data.hasAnimalMoodData(uuid)) return;

        double currentMood = data.getAnimalMoodScore(uuid);

        boolean anyPenalty = gameTime % MoodDecline.getDiverseSpeciesPenaltyTickInterval() == 0
                || gameTime % MoodDecline.getFecesPenaltyTickInterval() == 0
                || gameTime % MoodDecline.getRainPenaltyTickInterval() == 0;
        boolean anyBonus = gameTime % MoodRise.getSaltBlockMoodBonusTickInterval() == 0
                || gameTime % MoodRise.getMudMoodBonusTickInterval() == 0;
        boolean recovery = gameTime % MoodDecline.getMoodRecoveryTickInterval() == 0;

        if (!anyPenalty && !anyBonus && !recovery) return;

        double bonusDelta = MoodRise.calcTickBonus(level, animal, currentMood, gameTime);
        if (gameTime % MoodRise.getMudMoodBonusTickInterval() == 0) {
            bonusDelta += MoodRise.calcMudBonus(level, animal, currentMood);
        }
        double moodDelta = MoodDecline.calcTickPenalty(level, animal, gameTime) + bonusDelta;

        if (recovery && moodDelta == 0) {
            moodDelta += MoodDecline.calcRecovery(level, animal, currentMood);
        }

        if (moodDelta != 0) {
            data.applyMoodDelta(uuid, moodDelta);

            if (bonusDelta > 0) {
                AnimalFavorEventHandler.addFavorExp(level, animal, FavorRise.calcMoodRiseFavorExp(bonusDelta, level.random));
            }
        }

        if (gameTime % MoodDecline.getRainPenaltyTickInterval() == 0) {
            double rainPenalty = MoodDecline.calcRainPenalty(level, animal);
            if (rainPenalty > 0) {
                AnimalFavorEventHandler.addFavorExp(level, animal, -FavorDecline.calcRainPenaltyExp());
            }
        }

        if (moodDelta != 0) {
            AnimalMoodEventHandler.syncMoodToTrackingPlayers(animal, data);
        }
    }

    @Unique
    private void KR$tickHunger(ServerLevel level, Animal animal, UUID uuid, long gameTime) {
        AnimalHungerSavedData data = AnimalHungerEventHandler.getAnimalHungerData(level);
        if (!data.hasAnimalHungerData(uuid)) return;

        boolean synced = false;

        if (gameTime % 1200 == 0) {
            float naturalDecline = KRConfig.HUNGER_NATURAL_DECLINE_PER_MINUTE.get().floatValue();
            if (naturalDecline > 0) {
                float currentHunger = data.getAnimalHungerValue(uuid);
                if (currentHunger > AnimalHunger.getMinHunger()) {
                    data.addAnimalHungerValue(uuid, -naturalDecline);
                    synced = true;
                }
            }
        }

        if (gameTime % HungerDecline.getFecesDecayTickInterval() == 0) {
            if (data.isFeedCooldownExpired(uuid, gameTime) && data.getConsecutiveFeedCount(uuid) > 0) {
                data.resetConsecutiveFeedCount(uuid);
                synced = true;
            }

            float currentHunger = data.getAnimalHungerValue(uuid);
            if (currentHunger > AnimalHunger.getMinHunger()) {
                boolean wasFedToday = data.wasFedToday(uuid, gameTime);
                float delta = HungerDecline.calcTickPenalty(level, animal, wasFedToday);
                if (delta != 0) {
                    data.addAnimalHungerValue(uuid, delta);
                    synced = true;
                }
            }
        }

        if (synced) {
            AnimalHungerEventHandler.syncHungerToTrackingPlayers(animal, data);
        }
    }

    @Unique
    private void KR$tickMilk(ServerLevel level, Animal animal, UUID uuid, long gameTime) {
        boolean isPig = animal instanceof Pig;
        boolean isCow = animal instanceof Cow;
        boolean isSheep = animal instanceof Sheep;
        boolean isHorse = animal instanceof Horse;
        boolean isDonkey = animal instanceof Donkey;
        boolean isLlama = animal instanceof Llama;
        boolean isCamel = animal instanceof Camel;
        boolean isGoat = animal instanceof Goat;
        if (!isPig && !isCow && !isSheep && !isHorse && !isDonkey && !isLlama && !isCamel && !isGoat) return;

        AnimalMoodSavedData moodData = AnimalMoodEventHandler.getAnimalMoodData(level);
        AnimalHungerSavedData hungerData = AnimalHungerEventHandler.getAnimalHungerData(level);

        double mood = moodData.getAnimalMoodScore(uuid);
        float hunger = hungerData.getAnimalHungerValue(uuid);

        if (hunger < 8.0f || mood < 70.0) return;

        AnimalMilkSavedData milkData = AnimalMilkEventHandler.getAnimalMilkData(level);
        if (!milkData.hasMilkData(uuid)) return;

        double currentMilk = milkData.getMilkAmount(uuid);
        double maxCapacity = milkData.getMilkMaxCapacity(uuid);
        if (currentMilk >= maxCapacity) return;

        if (isLlama) {
            milkData.addMilk(uuid, 0.42);
        } else {
            double rate;
            if (isPig) {
                rate = 4.17 + level.random.nextDouble() * 3.33;
            } else if (isSheep) {
                rate = 0.50 + level.random.nextDouble() * 0.13;
            } else if (isHorse) {
                rate = 12.5 + level.random.nextDouble() * 3.3;
            } else if (isDonkey) {
                rate = 1.98 + level.random.nextDouble() * 2.98;
            } else if (isCamel) {
                rate = 4.17 + level.random.nextDouble() * 20.83;
            } else if (isGoat) {
                rate = 0.83 + level.random.nextDouble() * 0.84;
            } else {
                rate = 16.7 + level.random.nextDouble() * 8.3;
            }
            milkData.addMilk(uuid, rate);
        }
        AnimalMilkEventHandler.syncMilkToTrackingPlayers(animal, milkData);
    }
}