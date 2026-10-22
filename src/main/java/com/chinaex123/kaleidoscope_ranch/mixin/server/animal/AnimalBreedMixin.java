package com.chinaex123.kaleidoscope_ranch.mixin.server.animal;

import com.chinaex123.kaleidoscope_ranch.data.animal.hunger.AnimalHungerSavedData;
import com.chinaex123.kaleidoscope_ranch.data.animal.mood.AnimalMoodSavedData;
import com.chinaex123.kaleidoscope_ranch.entity.AnimalHungerEventHandler;
import com.chinaex123.kaleidoscope_ranch.entity.AnimalMoodEventHandler;
import com.chinaex123.kaleidoscope_ranch.init.KREntityTags;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Animal.class)
public abstract class AnimalBreedMixin {

    @Inject(method = "mobInteract", at = @At("HEAD"), cancellable = true)
    private void onMobInteract(Player player, InteractionHand hand, CallbackInfoReturnable<InteractionResult> cir) {
        Animal self = (Animal) (Object) this;

        if (!KREntityTags.isManagedAnimal(self)) return;

        ItemStack stack = player.getItemInHand(hand);
        if (stack.isEmpty()) return;

        Item feedItem = AnimalHungerEventHandler.getFeedForAnimal(self);
        if (feedItem == null) return;

        if (!stack.is(feedItem)) {
            cir.setReturnValue(InteractionResult.PASS);
        }
    }

    @Inject(method = "spawnChildFromBreeding", at = @At("HEAD"), cancellable = true)
    private void onBreed(ServerLevel level, Animal mate, CallbackInfo ci) {
        Animal self = (Animal) (Object) this;

        if (!KREntityTags.isManagedAnimal(self)) return;
        if (!KREntityTags.isManagedAnimal(mate)) return;

        AnimalMoodSavedData moodData = AnimalMoodEventHandler.getAnimalMoodData(level);
        AnimalHungerSavedData hungerData = AnimalHungerEventHandler.getAnimalHungerData(level);

        double selfMood = moodData.getAnimalMoodScore(self.getUUID());
        float selfHunger = hungerData.getAnimalHungerValue(self.getUUID());
        double otherMood = moodData.getAnimalMoodScore(mate.getUUID());
        float otherHunger = hungerData.getAnimalHungerValue(mate.getUUID());

        if (selfMood < 100.0 || selfHunger < 10.0f || otherMood < 100.0 || otherHunger < 10.0f) {
            self.setAge(6000);
            mate.setAge(6000);
            self.resetLove();
            mate.resetLove();
            ci.cancel();
        }
    }
}