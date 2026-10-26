package com.chinaex123.kaleidoscope_ranch.mixin.server.animal;

import com.chinaex123.kaleidoscope_ranch.config.KRConfig;
import com.chinaex123.kaleidoscope_ranch.data.animal.favor.AnimalFavorSavedData;
import com.chinaex123.kaleidoscope_ranch.data.animal.hunger.AnimalHungerSavedData;
import com.chinaex123.kaleidoscope_ranch.data.animal.milk.AnimalMilkSavedData;
import com.chinaex123.kaleidoscope_ranch.data.animal.mood.AnimalMoodSavedData;
import com.chinaex123.kaleidoscope_ranch.entity.AnimalFavorEventHandler;
import com.chinaex123.kaleidoscope_ranch.entity.AnimalHungerEventHandler;
import com.chinaex123.kaleidoscope_ranch.entity.AnimalMilkEventHandler;
import com.chinaex123.kaleidoscope_ranch.entity.AnimalMoodEventHandler;
import com.chinaex123.kaleidoscope_ranch.entity.BreedContext;
import com.chinaex123.kaleidoscope_ranch.entity.ai.AnimalChaseAndHurtGoal;
import com.chinaex123.kaleidoscope_ranch.entity.ai.KRFleeFromPlayerGoal;
import com.chinaex123.kaleidoscope_ranch.init.KRItems;
import com.chinaex123.kaleidoscope_ranch.init.tags.KREntityTags;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.ai.goal.WrappedGoal;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.animal.Cow;
import net.minecraft.world.entity.animal.MushroomCow;
import net.minecraft.world.entity.animal.Rabbit;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.UUID;

@Mixin(Animal.class)
public abstract class AnimalBreedMixin {

    @Unique
    private static final int RABBIT_MILK_BUCKET_AMOUNT = 1000;

    @Inject(method = "mobInteract", at = @At("HEAD"), cancellable = true)
    private void onMobInteract(Player player, InteractionHand hand, CallbackInfoReturnable<InteractionResult> cir) {
        Animal self = (Animal) (Object) this;

        if (self instanceof Rabbit) {
            KR$handleRabbitInteract(self, player, hand, cir);
            if (cir.isCancelled()) return;
        }

        if (!KREntityTags.isManagedAnimal(self)) return;

        ItemStack stack = player.getItemInHand(hand);
        if (stack.isEmpty()) return;

        Item feedItem = AnimalHungerEventHandler.getFeedForAnimal(self);
        if (feedItem == null) return;

        if (!stack.is(feedItem)) {
            cir.setReturnValue(InteractionResult.PASS);
        }
    }

    @Unique
    private void KR$handleRabbitInteract(Animal self, Player player, InteractionHand hand, CallbackInfoReturnable<InteractionResult> cir) {
        ItemStack heldItem = player.getItemInHand(hand);

        if (heldItem.is(KRItems.CREATIVE_FEED.get())) {
            InteractionResult result = heldItem.interactLivingEntity(player, self, hand);
            if (result.consumesAction()) {
                cir.setReturnValue(result);
            }
            return;
        }

        if (self.level().isClientSide()) {
            cir.setReturnValue(InteractionResult.SUCCESS);
            return;
        }

        ServerLevel level = (ServerLevel) self.level();
        AnimalMoodSavedData moodData = AnimalMoodEventHandler.getAnimalMoodData(level);
        UUID uuid = self.getUUID();
        boolean isWild = moodData.isWild(uuid);

        if (isWild && heldItem.getItem() != Items.BUCKET) {
            AnimalFavorSavedData favorData = AnimalFavorEventHandler.getAnimalFavorData(level);
            int favorLevel = favorData.getAnimalFavorLevel(uuid);
            if (favorLevel < 8) {
                cir.setReturnValue(InteractionResult.FAIL);
                return;
            }
        }

        if (heldItem.getItem() == Items.BUCKET) {
            if (isWild) {
                self.goalSelector.getAvailableGoals().stream()
                        .map(WrappedGoal::getGoal)
                        .filter(goal -> goal instanceof AnimalChaseAndHurtGoal)
                        .toList()
                        .forEach(self.goalSelector::removeGoal);
                self.goalSelector.getAvailableGoals().stream()
                        .map(WrappedGoal::getGoal)
                        .filter(goal -> goal instanceof KRFleeFromPlayerGoal)
                        .toList()
                        .forEach(self.goalSelector::removeGoal);
                self.getNavigation().stop();
                self.goalSelector.addGoal(2, new AnimalChaseAndHurtGoal(self, 1.0, 2.0f));
                self.setTarget(player);
                self.getLookControl().setLookAt(player, 10.0F, 10.0F);
                self.getNavigation().moveTo(player, 1.0);
                cir.setReturnValue(InteractionResult.SUCCESS);
                return;
            }

            AnimalMilkSavedData milkData = AnimalMilkEventHandler.getAnimalMilkData(level);
            double currentMilk = milkData.getMilkAmount(uuid);
            if (currentMilk < RABBIT_MILK_BUCKET_AMOUNT) {
                cir.setReturnValue(InteractionResult.SUCCESS);
                return;
            }

            AnimalFavorSavedData favorData = AnimalFavorEventHandler.getAnimalFavorData(level);
            int favorLevel = favorData.getAnimalFavorLevel(uuid);
            if (favorLevel < 8) {
                cir.setReturnValue(InteractionResult.SUCCESS);
                return;
            }

            milkData.drainMilk(uuid, RABBIT_MILK_BUCKET_AMOUNT);

            if (!player.hasInfiniteMaterials()) {
                heldItem.shrink(1);
            }

            ItemStack milkBucket = new ItemStack(KRItems.RABBIT_MILK_BUCKET.get());
            if (!player.getInventory().add(milkBucket)) {
                player.drop(milkBucket, false);
            }

            level.playSound(null, self, SoundEvents.BUCKET_FILL, SoundSource.NEUTRAL, 1.0F, 1.0F);
            cir.setReturnValue(InteractionResult.SUCCESS);
        }
    }

    @Inject(method = "spawnChildFromBreeding", at = @At("HEAD"), cancellable = true)
    private void onBreed(ServerLevel level, Animal mate, CallbackInfo ci) {
        Animal self = (Animal) (Object) this;

        if (!KREntityTags.isManagedAnimal(self)) return;
        if (!KREntityTags.isManagedAnimal(mate)) return;

        BreedContext.setBreeding(true);

        AnimalMoodSavedData moodData = AnimalMoodEventHandler.getAnimalMoodData(level);
        AnimalHungerSavedData hungerData = AnimalHungerEventHandler.getAnimalHungerData(level);

        double selfMood = moodData.getAnimalMoodScore(self.getUUID());
        float selfHunger = hungerData.getAnimalHungerValue(self.getUUID());
        double otherMood = moodData.getAnimalMoodScore(mate.getUUID());
        float otherHunger = hungerData.getAnimalHungerValue(mate.getUUID());

        if (selfMood < 100.0 || selfHunger < 10.0f || otherMood < 100.0 || otherHunger < 10.0f) {
            int cooldown = KRConfig.BREED_COOLDOWN_TICKS.get();
            self.setAge(cooldown);
            mate.setAge(cooldown);
            self.resetLove();
            mate.resetLove();
            ci.cancel();
        }
    }

    @Inject(method = "spawnChildFromBreeding", at = @At("TAIL"))
    private void onBreedTail(ServerLevel level, Animal mate, CallbackInfo ci) {
        Animal self = (Animal) (Object) this;

        int cooldown = KRConfig.BREED_COOLDOWN_TICKS.get();
        self.setAge(cooldown);
        mate.setAge(cooldown);

        boolean selfIsCow = self instanceof Cow && !(self instanceof MushroomCow);
        boolean selfIsMooshroom = self instanceof MushroomCow;
        boolean mateIsCow = mate instanceof Cow && !(mate instanceof MushroomCow);
        boolean mateIsMooshroom = mate instanceof MushroomCow;

        if ((selfIsCow && mateIsCow) || (selfIsMooshroom && mateIsMooshroom)) {
            AnimalFavorSavedData favorData = AnimalFavorEventHandler.getAnimalFavorData(level);
            int selfFavor = favorData.getAnimalFavorLevel(self.getUUID());
            int mateFavor = favorData.getAnimalFavorLevel(mate.getUUID());

            if (selfFavor >= 10 && mateFavor >= 10) {
                double chance = KRConfig.BREED_EXTRA_OFFSPRING_CHANCE.get();
                if (level.random.nextDouble() < chance) {
                    int extraCount = KRConfig.BREED_EXTRA_OFFSPRING_COUNT.get();
                    for (int i = 0; i < extraCount; i++) {
                        Animal baby = (Animal) self.getBreedOffspring(level, mate);
                        if (baby == null) break;

                        baby.setBaby(true);
                        baby.moveTo(self.getX(), self.getY(), self.getZ(), 0.0F, 0.0F);
                        level.addFreshEntityWithPassengers(baby);
                    }
                }
            }
        }

        BreedContext.setBreeding(false);
    }
}