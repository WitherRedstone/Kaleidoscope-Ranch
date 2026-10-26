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
import com.chinaex123.kaleidoscope_ranch.entity.ai.AnimalChaseAndHurtGoal;
import com.chinaex123.kaleidoscope_ranch.entity.ai.SheepEatFromGroundGoal;
import com.chinaex123.kaleidoscope_ranch.init.KRItems;
import com.chinaex123.kaleidoscope_ranch.init.tags.KREntityTags;
import com.chinaex123.kaleidoscope_ranch.mixin.server.MobGoalSelectorAccessor;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.animal.Sheep;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.ItemLike;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Map;
import java.util.UUID;

@Mixin(Sheep.class)
public abstract class SheepMixin {

    @Unique
    private static final int MILK_BUCKET_AMOUNT = 1000;

    @Unique
    private boolean KR$wasShearedBeforeEating = false;

    @Shadow
    public abstract boolean isSheared();

    @Shadow
    public abstract void setSheared(boolean sheared);

    @Shadow
    public abstract DyeColor getColor();

    @Final
    @Shadow
    private static Map<DyeColor, ItemLike> ITEM_BY_DYE;

    @Inject(method = "registerGoals", at = @At("TAIL"))
    private void KR$addSheepGoals(CallbackInfo ci) {
        Sheep self = (Sheep) (Object) this;
        ((MobGoalSelectorAccessor) self).KR$getGoalSelector().addGoal(4, new SheepEatFromGroundGoal(self));
    }

    @Inject(method = "mobInteract", at = @At("HEAD"), cancellable = true)
    private void KR$onMobInteract(Player player, InteractionHand hand, CallbackInfoReturnable<InteractionResult> cir) {
        Sheep self = (Sheep) (Object) this;
        ItemStack heldItem = player.getItemInHand(hand);

        if (heldItem.getItem() == Items.BUCKET) {
            KR$handleMilking(self, player, hand, cir);
            return;
        }

        if (heldItem.getItem() == Items.SHEARS) {
            KR$handleShearing(self, player, hand, heldItem, cir);
        }
    }

    @Inject(method = "ate", at = @At("HEAD"))
    private void KR$onAteHead(CallbackInfo ci) {
        Sheep self = (Sheep) (Object) this;
        KR$wasShearedBeforeEating = self.isSheared();
    }

    @Inject(method = "ate", at = @At("TAIL"))
    private void KR$onAteTail(CallbackInfo ci) {
        Sheep self = (Sheep) (Object) this;
        if (self.level().isClientSide()) return;
        if (!KREntityTags.isManagedAnimal(self)) return;

        ServerLevel level = (ServerLevel) self.level();
        UUID uuid = self.getUUID();

        AnimalMoodSavedData moodData = AnimalMoodEventHandler.getAnimalMoodData(level);
        boolean isWild = moodData.isWild(uuid);

        if (isWild) {
            self.setSheared(KR$wasShearedBeforeEating);
            return;
        }

        AnimalHungerSavedData hungerData = AnimalHungerEventHandler.getAnimalHungerData(level);
        float hunger = hungerData.getAnimalHungerValue(uuid);

        if (hunger < 8.0f) {
            self.setSheared(KR$wasShearedBeforeEating);
            return;
        }

        long gameTime = level.getGameTime();
        if (!hungerData.isShearCooldownExpired(uuid, gameTime)) {
            self.setSheared(KR$wasShearedBeforeEating);
        }
    }

    @Inject(method = "readyForShearing", at = @At("HEAD"), cancellable = true)
    private void KR$onReadyForShearing(CallbackInfoReturnable<Boolean> cir) {
        Sheep self = (Sheep) (Object) this;
        if (self.level().isClientSide()) return;
        if (!KREntityTags.isManagedAnimal(self)) return;
        if (self.isSheared()) {
            cir.setReturnValue(false);
            return;
        }

        ServerLevel level = (ServerLevel) self.level();
        UUID uuid = self.getUUID();

        AnimalMoodSavedData moodData = AnimalMoodEventHandler.getAnimalMoodData(level);
        boolean isWild = moodData.isWild(uuid);

        if (isWild) {
            cir.setReturnValue(true);
            return;
        }

        AnimalFavorSavedData favorData = AnimalFavorEventHandler.getAnimalFavorData(level);
        int favorLevel = favorData.getAnimalFavorLevel(uuid);
        cir.setReturnValue(favorLevel >= 8);
    }

    @Unique
    private void KR$handleMilking(Sheep self, Player player, InteractionHand hand, CallbackInfoReturnable<InteractionResult> cir) {
        if (self.level().isClientSide()) {
            cir.setReturnValue(InteractionResult.SUCCESS);
            return;
        }

        ServerLevel level = (ServerLevel) self.level();
        AnimalMoodSavedData moodData = AnimalMoodEventHandler.getAnimalMoodData(level);
        AnimalMilkSavedData milkData = AnimalMilkEventHandler.getAnimalMilkData(level);
        UUID uuid = self.getUUID();

        boolean isWild = moodData.isWild(uuid);

        if (isWild) {
            KR$triggerWildChase(self, player);
            cir.setReturnValue(InteractionResult.SUCCESS);
            return;
        }

        double currentMilk = milkData.getMilkAmount(uuid);
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
            player.getItemInHand(hand).shrink(1);
        }

        ItemStack milkBucket = new ItemStack(KRItems.SHEEP_MILK_BUCKET.get());
        if (!player.getInventory().add(milkBucket)) {
            player.drop(milkBucket, false);
        }

        level.playSound(null, self, SoundEvents.BUCKET_FILL, SoundSource.NEUTRAL, 1.0F, 1.0F);
        cir.setReturnValue(InteractionResult.SUCCESS);
    }

    @Unique
    private void KR$handleShearing(Sheep self, Player player, InteractionHand hand, ItemStack heldItem, CallbackInfoReturnable<InteractionResult> cir) {
        if (self.level().isClientSide()) {
            cir.setReturnValue(InteractionResult.SUCCESS);
            return;
        }

        if (self.isSheared() || self.isBaby()) {
            cir.setReturnValue(InteractionResult.PASS);
            return;
        }

        ServerLevel level = (ServerLevel) self.level();
        AnimalMoodSavedData moodData = AnimalMoodEventHandler.getAnimalMoodData(level);
        UUID uuid = self.getUUID();
        boolean isWild = moodData.isWild(uuid);

        if (isWild) {
            if (!player.hasInfiniteMaterials()) {
                heldItem.hurtAndBreak(1, player, hand == InteractionHand.MAIN_HAND ? EquipmentSlot.MAINHAND : EquipmentSlot.OFFHAND);
            }
            if (level.random.nextFloat() < 0.8f) {
                level.playSound(null, self, SoundEvents.SHEEP_SHEAR, SoundSource.NEUTRAL, 1.0F, 0.5F);
                for (int i = 0; i < 5; i++) {
                    double offsetX = (level.random.nextDouble() - 0.5) * 0.5;
                    double offsetY = level.random.nextDouble() * 0.5 + 0.5;
                    double offsetZ = (level.random.nextDouble() - 0.5) * 0.5;
                    level.sendParticles(ParticleTypes.SMOKE, self.getX() + offsetX, self.getY() + offsetY, self.getZ() + offsetZ, 1, 0, 0, 0, 0);
                }
                cir.setReturnValue(InteractionResult.SUCCESS);
                return;
            }
            DyeColor color = self.getColor();
            KR$spawnWoolWithVelocity(self, new ItemStack(ITEM_BY_DYE.get(color), 1));
            self.setSheared(true);
            KR$setShearCooldown(self, level);
            cir.setReturnValue(InteractionResult.SUCCESS);
            return;
        }

        AnimalFavorSavedData favorData = AnimalFavorEventHandler.getAnimalFavorData(level);
        int favorLevel = favorData.getAnimalFavorLevel(uuid);
        if (favorLevel < 8) {
            cir.setReturnValue(InteractionResult.SUCCESS);
            return;
        }

        int woolCount;
        if (favorLevel >= 12) {
            woolCount = 4 + level.random.nextInt(5);
        } else if (favorLevel >= 10) {
            woolCount = 3 + level.random.nextInt(4);
        } else {
            woolCount = 1 + level.random.nextInt(3);
        }
        DyeColor color = self.getColor();
        for (int i = 0; i < woolCount; i++) {
            KR$spawnWoolWithVelocity(self, new ItemStack(ITEM_BY_DYE.get(color), 1));
        }
        self.setSheared(true);
        KR$setShearCooldown(self, level);

        if (!player.hasInfiniteMaterials()) {
            heldItem.hurtAndBreak(1, player, hand == InteractionHand.MAIN_HAND ? EquipmentSlot.MAINHAND : EquipmentSlot.OFFHAND);
        }

        level.playSound(null, self, SoundEvents.SHEEP_SHEAR, SoundSource.NEUTRAL, 1.0F, 1.0F);
        cir.setReturnValue(InteractionResult.SUCCESS);
    }

    @Unique
    private void KR$triggerWildChase(Sheep self, Player player) {
        self.goalSelector.getAvailableGoals().stream()
                .map(net.minecraft.world.entity.ai.goal.WrappedGoal::getGoal)
                .filter(goal -> goal instanceof AnimalChaseAndHurtGoal)
                .toList()
                .forEach(self.goalSelector::removeGoal);
        self.goalSelector.getAvailableGoals().stream()
                .map(net.minecraft.world.entity.ai.goal.WrappedGoal::getGoal)
                .filter(goal -> goal instanceof com.chinaex123.kaleidoscope_ranch.entity.ai.KRFleeFromPlayerGoal)
                .toList()
                .forEach(self.goalSelector::removeGoal);
        self.getNavigation().stop();
        self.goalSelector.addGoal(2, new AnimalChaseAndHurtGoal(self, 1.2, 4.0f));
        self.setTarget(player);
        self.getLookControl().setLookAt(player, 10.0F, 10.0F);
        self.getNavigation().moveTo(player, 1.2);
    }

    @Unique
    private void KR$spawnWoolWithVelocity(Sheep self, ItemStack stack) {
        ItemEntity itemEntity = self.spawnAtLocation(stack, 1.0F);
        if (itemEntity != null) {
            itemEntity.setDeltaMovement(itemEntity.getDeltaMovement().add(
                    (self.getRandom().nextFloat() - self.getRandom().nextFloat()) * 0.1F,
                    self.getRandom().nextFloat() * 0.05F + 0.05F,
                    (self.getRandom().nextFloat() - self.getRandom().nextFloat()) * 0.1F));
        }
    }

    @Unique
    private void KR$setShearCooldown(Sheep self, ServerLevel level) {
        AnimalHungerSavedData hungerData = AnimalHungerEventHandler.getAnimalHungerData(level);
        long gameTime = level.getGameTime();
        long cooldownTicks = KRConfig.SHEEP_SHEAR_COOLDOWN_TICKS.get();
        hungerData.setShearCooldownExpire(self.getUUID(), gameTime + cooldownTicks);
        AnimalHungerEventHandler.syncHungerToTrackingPlayers(self, hungerData);
    }
}