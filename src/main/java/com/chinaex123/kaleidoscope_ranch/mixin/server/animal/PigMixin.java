package com.chinaex123.kaleidoscope_ranch.mixin.server.animal;

import com.chinaex123.kaleidoscope_ranch.data.animal.favor.AnimalFavorSavedData;
import com.chinaex123.kaleidoscope_ranch.data.animal.milk.AnimalMilkSavedData;
import com.chinaex123.kaleidoscope_ranch.data.animal.mood.AnimalMoodSavedData;
import com.chinaex123.kaleidoscope_ranch.entity.AnimalFavorEventHandler;
import com.chinaex123.kaleidoscope_ranch.entity.AnimalMilkEventHandler;
import com.chinaex123.kaleidoscope_ranch.entity.AnimalMoodEventHandler;
import com.chinaex123.kaleidoscope_ranch.entity.ai.AnimalChaseAndHurtGoal;
import com.chinaex123.kaleidoscope_ranch.entity.ai.PigEatFromGroundGoal;
import com.chinaex123.kaleidoscope_ranch.entity.ai.PigMudBoostGoal;
import com.chinaex123.kaleidoscope_ranch.entity.ai.PigTreasureHuntGoal;
import com.chinaex123.kaleidoscope_ranch.init.KRItems;
import com.chinaex123.kaleidoscope_ranch.mixin.server.MobGoalSelectorAccessor;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.ai.goal.WrappedGoal;
import net.minecraft.world.entity.animal.Pig;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.UUID;

@Mixin(Pig.class)
public abstract class PigMixin {

    @Unique
    private static final int MILK_BUCKET_AMOUNT = 1000;

    @Inject(method = "registerGoals", at = @At("TAIL"))
    private void KR$addPigGoals(CallbackInfo ci) {
        Pig self = (Pig) (Object) this;
        ((MobGoalSelectorAccessor) self).KR$getGoalSelector().addGoal(4, new PigEatFromGroundGoal(self));
        ((MobGoalSelectorAccessor) self).KR$getGoalSelector().addGoal(5, new PigTreasureHuntGoal(self));
        ((MobGoalSelectorAccessor) self).KR$getGoalSelector().addGoal(6, new PigMudBoostGoal(self));
    }

    @Inject(method = "mobInteract", at = @At("HEAD"), cancellable = true)
    private void KR$onMobInteract(Player player, InteractionHand hand, CallbackInfoReturnable<InteractionResult> cir) {
        Pig self = (Pig) (Object) this;

        ItemStack heldItem = player.getItemInHand(hand);
        if (heldItem.getItem() != Items.BUCKET) return;

        if (self.level().isClientSide()) {
            cir.setReturnValue(InteractionResult.FAIL);
            return;
        }

        ServerLevel level = (ServerLevel) self.level();
        AnimalMoodSavedData moodData = AnimalMoodEventHandler.getAnimalMoodData(level);
        AnimalMilkSavedData milkData = AnimalMilkEventHandler.getAnimalMilkData(level);
        UUID uuid = self.getUUID();

        boolean isWild = moodData.isWild(uuid);

        if (isWild) {
            self.goalSelector.getAvailableGoals().stream()
                    .map(WrappedGoal::getGoal)
                    .filter(goal -> goal instanceof AnimalChaseAndHurtGoal)
                    .toList()
                    .forEach(self.goalSelector::removeGoal);
            self.goalSelector.getAvailableGoals().stream()
                    .map(WrappedGoal::getGoal)
                    .filter(goal -> goal instanceof com.chinaex123.kaleidoscope_ranch.entity.ai.KRFleeFromPlayerGoal)
                    .toList()
                    .forEach(self.goalSelector::removeGoal);
            self.getNavigation().stop();
            self.goalSelector.addGoal(2, new AnimalChaseAndHurtGoal(self, 1.2, 4.0f));
            self.setTarget(player);
            self.getLookControl().setLookAt(player, 10.0F, 10.0F);
            self.getNavigation().moveTo(player, 1.2);
            cir.setReturnValue(InteractionResult.FAIL);
            return;
        }

        int currentMilk = milkData.getMilkAmount(uuid);
        if (currentMilk < MILK_BUCKET_AMOUNT) {
            cir.setReturnValue(InteractionResult.FAIL);
            return;
        }

        AnimalFavorSavedData favorData = AnimalFavorEventHandler.getAnimalFavorData(level);
        float favor = favorData.getAnimalFavorValue(uuid);
        if (favor < 8.0f) {
            cir.setReturnValue(InteractionResult.FAIL);
            return;
        }

        milkData.drainMilk(uuid, MILK_BUCKET_AMOUNT);

        if (!player.hasInfiniteMaterials()) {
            heldItem.shrink(1);
        }

        ItemStack milkBucket = new ItemStack(KRItems.PIG_MILK_BUCKET.get());
        if (!player.getInventory().add(milkBucket)) {
            player.drop(milkBucket, false);
        }

        level.playSound(null, self, SoundEvents.BUCKET_FILL, SoundSource.NEUTRAL, 1.0F, 1.0F);
        cir.setReturnValue(InteractionResult.SUCCESS);
    }
}