package com.chinaex123.kaleidoscope_ranch.mixin.server.animal;

import com.chinaex123.kaleidoscope_ranch.blockentity.HaystackHollowBlockEntity;
import com.chinaex123.kaleidoscope_ranch.data.animal.favor.AnimalFavorSavedData;
import com.chinaex123.kaleidoscope_ranch.data.animal.hunger.AnimalHungerSavedData;
import com.chinaex123.kaleidoscope_ranch.data.animal.mood.AnimalMoodSavedData;
import com.chinaex123.kaleidoscope_ranch.entity.AnimalFavorEventHandler;
import com.chinaex123.kaleidoscope_ranch.entity.AnimalHungerEventHandler;
import com.chinaex123.kaleidoscope_ranch.entity.AnimalMoodEventHandler;
import com.chinaex123.kaleidoscope_ranch.entity.ai.ChickenEatFromGroundGoal;
import com.chinaex123.kaleidoscope_ranch.entity.ai.LayEggInHaystackGoal;
import com.chinaex123.kaleidoscope_ranch.init.KRBlocks;
import com.chinaex123.kaleidoscope_ranch.init.KRItems;
import com.chinaex123.kaleidoscope_ranch.init.tags.KREntityTags;
import com.chinaex123.kaleidoscope_ranch.mixin.server.MobGoalSelectorAccessor;
import com.chinaex123.kaleidoscope_ranch.util.KRHoldEgg;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.animal.Chicken;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.gameevent.GameEvent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.UUID;

@Mixin(Chicken.class)
public abstract class ChickenLayEggMixin implements KRHoldEgg {

    @Shadow
    public int eggTime;

    @Unique
    private boolean KR$holdingEgg = false;

    @Override
    public boolean KR$isHoldingEgg() {
        return KR$holdingEgg;
    }

    @Override
    public void KR$setHoldingEgg(boolean holding) {
        KR$holdingEgg = holding;
    }

    @Inject(method = "registerGoals", at = @At("TAIL"))
    private void KR$addLayEggGoal(CallbackInfo ci) {
        Chicken self = (Chicken) (Object) this;
        ((MobGoalSelectorAccessor) self).KR$getGoalSelector().addGoal(3, new LayEggInHaystackGoal(self));
        ((MobGoalSelectorAccessor) self).KR$getGoalSelector().addGoal(4, new ChickenEatFromGroundGoal(self));
    }

    @Inject(method = "aiStep", at = @At("HEAD"))
    private void onLayEgg(CallbackInfo ci) {
        Chicken self = (Chicken) (Object) this;

        if (self.level().isClientSide) return;
        if (!self.isAlive() || self.isBaby() || self.isChickenJockey()) return;
        if (!KREntityTags.isManagedAnimal(self)) return;

        if (this.eggTime > 1 && !KR$holdingEgg) return;

        if (!(self.level() instanceof ServerLevel level)) return;

        AnimalHungerSavedData hungerData = AnimalHungerEventHandler.getAnimalHungerData(level);
        AnimalMoodSavedData moodData = AnimalMoodEventHandler.getAnimalMoodData(level);
        AnimalFavorSavedData favorData = AnimalFavorEventHandler.getAnimalFavorData(level);

        UUID uuid = self.getUUID();
        float hunger = hungerData.getAnimalHungerValue(uuid);
        double mood = moodData.getAnimalMoodScore(uuid);
        int favorLevel = favorData.getAnimalFavorLevel(uuid);

        if (hunger < 8 || mood < 70) {
            KR$holdingEgg = true;
            this.eggTime = 2;
            return;
        }

        BlockPos haystackPos = KR$findHaystackHollow(self);

        if (haystackPos == null) {
            KR$holdingEgg = true;
            this.eggTime = 2;
            return;
        }

        KR$holdingEgg = false;
        this.eggTime = self.getRandom().nextInt(6000) + 6000;

        BlockEntity be = level.getBlockEntity(haystackPos);
        if (!(be instanceof HaystackHollowBlockEntity hollow)) return;

        ItemStack eggStack = new ItemStack(Items.EGG);
        int inserted = hollow.insertItem(eggStack);
        if (inserted > 0) {
            hollow.setChanged();
            level.playSound(null, self, SoundEvents.CHICKEN_EGG, SoundSource.NEUTRAL, 1.0F,
                    (level.random.nextFloat() - level.random.nextFloat()) * 0.2F + 1.0F);
            self.gameEvent(GameEvent.ENTITY_PLACE);
        }

        if (mood >= 90 && level.random.nextFloat() < 0.05) {
            ItemStack goldenEgg = new ItemStack(KRItems.GOLDEN_EGG.get());
            int goldenInserted = hollow.insertItem(goldenEgg);
            if (goldenInserted > 0) {
                hollow.setChanged();
            }
        }

        if (hunger >= 8 && mood >= 80 && favorLevel >= 10) {
            float featherChance = Math.min(0.5f, (favorLevel - 9) * 0.1f);
            if (level.random.nextFloat() < featherChance) {
                self.spawnAtLocation(Items.FEATHER);
            }
        }
    }

    @Unique
    private static BlockPos KR$findHaystackHollow(Chicken chicken) {
        BlockPos origin = chicken.blockPosition();

        if (chicken.level().getBlockState(origin).getBlock() == KRBlocks.HAYSTACK_HOLLOW.get()) return origin;
        if (chicken.level().getBlockState(origin.below()).getBlock() == KRBlocks.HAYSTACK_HOLLOW.get()) return origin.below();

        for (BlockPos pos : BlockPos.betweenClosed(origin.offset(-1, -1, -1), origin.offset(1, 1, 1))) {
            if (chicken.level().getBlockState(pos).getBlock() == KRBlocks.HAYSTACK_HOLLOW.get()) {
                return pos.immutable();
            }
        }
        return null;
    }
}