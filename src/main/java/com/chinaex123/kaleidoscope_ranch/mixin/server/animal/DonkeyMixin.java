package com.chinaex123.kaleidoscope_ranch.mixin.server.animal;

import com.chinaex123.kaleidoscope_ranch.data.animal.favor.AnimalFavorSavedData;
import com.chinaex123.kaleidoscope_ranch.data.animal.milk.AnimalMilkSavedData;
import com.chinaex123.kaleidoscope_ranch.data.animal.mood.AnimalMoodSavedData;
import com.chinaex123.kaleidoscope_ranch.entity.AnimalFavorEventHandler;
import com.chinaex123.kaleidoscope_ranch.entity.AnimalMilkEventHandler;
import com.chinaex123.kaleidoscope_ranch.entity.AnimalMoodEventHandler;
import com.chinaex123.kaleidoscope_ranch.entity.ai.AnimalChaseAndHurtGoal;
import com.chinaex123.kaleidoscope_ranch.entity.ai.KRFleeFromPlayerGoal;
import com.chinaex123.kaleidoscope_ranch.init.KRItems;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.ai.goal.WrappedGoal;
import net.minecraft.world.entity.animal.horse.AbstractChestedHorse;
import net.minecraft.world.entity.animal.horse.Donkey;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.UUID;

@Mixin(AbstractChestedHorse.class)
public abstract class DonkeyMixin {

    @Unique
    private static final int MILK_BUCKET_AMOUNT = 1000;

    @Inject(method = "mobInteract", at = @At("HEAD"), cancellable = true)
    private void KR$onMobInteract(Player player, InteractionHand hand, CallbackInfoReturnable<InteractionResult> cir) {
        AbstractChestedHorse self = (AbstractChestedHorse) (Object) this;
        if (!(self instanceof Donkey)) return;

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
                self.goalSelector.addGoal(2, new AnimalChaseAndHurtGoal(self, 1.4, 12.0f));
                self.setTarget(player);
                self.getLookControl().setLookAt(player, 10.0F, 10.0F);
                self.getNavigation().moveTo(player, 1.4);
                cir.setReturnValue(InteractionResult.SUCCESS);
                return;
            }

            AnimalMilkSavedData milkData = AnimalMilkEventHandler.getAnimalMilkData(level);
            int currentMilk = milkData.getMilkAmount(uuid);
            if (currentMilk < MILK_BUCKET_AMOUNT) {
                cir.setReturnValue(InteractionResult.SUCCESS);
                return;
            }

            AnimalFavorSavedData favorData = AnimalFavorEventHandler.getAnimalFavorData(level);
            int favorLevel = favorData.getAnimalFavorLevel(uuid);
            if (favorLevel < 8) {
                cir.setReturnValue(InteractionResult.SUCCESS);
                return;
            }

            milkData.drainMilk(uuid, MILK_BUCKET_AMOUNT);

            if (!player.hasInfiniteMaterials()) {
                heldItem.shrink(1);
            }

            ItemStack milkBucket = new ItemStack(KRItems.DONKEY_MILK_BUCKET.get());
            if (!player.getInventory().add(milkBucket)) {
                player.drop(milkBucket, false);
            }

            level.playSound(null, self, SoundEvents.BUCKET_FILL, SoundSource.NEUTRAL, 1.0F, 1.0F);
            cir.setReturnValue(InteractionResult.SUCCESS);
        }
    }
}