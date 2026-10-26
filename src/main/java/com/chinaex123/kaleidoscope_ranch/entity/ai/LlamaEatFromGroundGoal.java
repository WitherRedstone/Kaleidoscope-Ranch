package com.chinaex123.kaleidoscope_ranch.entity.ai;

import com.chinaex123.kaleidoscope_ranch.data.animal.hunger.AnimalHungerSavedData;
import com.chinaex123.kaleidoscope_ranch.entity.AnimalHungerEventHandler;
import com.chinaex123.kaleidoscope_ranch.entity.animation.KREatingAnimation;
import com.chinaex123.kaleidoscope_ranch.init.KRItems;
import com.chinaex123.kaleidoscope_ranch.init.tags.KREntityTags;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.animal.horse.Llama;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.AABB;

import java.util.EnumSet;
import java.util.List;
import java.util.UUID;

public class LlamaEatFromGroundGoal extends Goal {

    private static final double REACH_DISTANCE_SQ = 1.8 * 1.8;
    private static final float FEED_HUNGER_GAIN = 0.5f;
    private static final float STOP_HUNGER = 7.0f;
    private static final float START_HUNGER = 5.0f;
    private static final double DETECT_RADIUS = 8.0;

    private final Llama llama;
    private final boolean isManaged;
    private ItemEntity targetItem;
    private int eatDelay;

    public LlamaEatFromGroundGoal(Llama llama) {
        this.llama = llama;
        this.isManaged = KREntityTags.isManagedAnimal(llama);
        this.setFlags(EnumSet.of(Flag.MOVE, Flag.LOOK));
    }

    @Override
    public boolean canUse() {
        if (llama.level().isClientSide()) return false;
        if (llama.isBaby()) return false;

        ServerLevel level = (ServerLevel) llama.level();
        AnimalHungerSavedData hungerData = AnimalHungerEventHandler.getAnimalHungerData(level);
        float hunger = hungerData.getAnimalHungerValue(llama.getUUID());

        if (hunger > START_HUNGER) return false;

        targetItem = findNearbyEdibleItem();
        return targetItem != null;
    }

    @Override
    public boolean canContinueToUse() {
        if (targetItem == null || targetItem.isRemoved()) return false;
        if (llama.level().isClientSide()) return false;

        ServerLevel level = (ServerLevel) llama.level();
        AnimalHungerSavedData hungerData = AnimalHungerEventHandler.getAnimalHungerData(level);
        float hunger = hungerData.getAnimalHungerValue(llama.getUUID());

        return hunger < STOP_HUNGER;
    }

    @Override
    public void start() {
        eatDelay = 0;
        if (targetItem != null) {
            llama.getNavigation().moveTo(targetItem, 1.0);
        }
    }

    @Override
    public void tick() {
        if (targetItem == null || targetItem.isRemoved()) {
            stop();
            return;
        }

        llama.getLookControl().setLookAt(targetItem, 10.0f, 10.0f);

        double distSq = llama.distanceToSqr(targetItem);

        if (distSq <= REACH_DISTANCE_SQ) {
            llama.getNavigation().stop();
            eatDelay++;

            if (eatDelay >= 20) {
                eatDelay = 0;
                if (llama instanceof KREatingAnimation anim) {
                    anim.KR$startEatingAnimation();
                }
                performEat();
            }
        } else {
            if (llama.getNavigation().isDone() || eatDelay > 0) {
                eatDelay = 0;
                llama.getNavigation().moveTo(targetItem, 1.0);
            }
        }
    }

    @Override
    public void stop() {
        targetItem = null;
        eatDelay = 0;
        llama.getNavigation().stop();
    }

    private void performEat() {
        if (targetItem == null || targetItem.isRemoved()) return;
        if (!(llama.level() instanceof ServerLevel level)) return;

        ItemStack stack = targetItem.getItem();
        if (stack.isEmpty()) return;

        if (!isManaged || !isLivestockFeed(stack)) return;

        stack.shrink(1);
        if (stack.isEmpty()) {
            targetItem.discard();
        } else {
            targetItem.setItem(stack);
        }

        AnimalHungerSavedData hungerData = AnimalHungerEventHandler.getAnimalHungerData(level);
        UUID uuid = llama.getUUID();
        hungerData.addAnimalHungerValue(uuid, FEED_HUNGER_GAIN);
        AnimalHungerEventHandler.syncHungerToTrackingPlayers(llama, hungerData);
    }

    private ItemEntity findNearbyEdibleItem() {
        AABB searchBox = llama.getBoundingBox().inflate(DETECT_RADIUS);
        List<ItemEntity> items = llama.level().getEntitiesOfClass(ItemEntity.class, searchBox);

        ItemEntity closest = null;
        double closestDist = Double.MAX_VALUE;

        for (ItemEntity itemEntity : items) {
            if (itemEntity.isRemoved()) continue;
            ItemStack stack = itemEntity.getItem();
            if (!isEdible(stack)) continue;

            double dist = llama.distanceToSqr(itemEntity);
            if (dist < closestDist) {
                closestDist = dist;
                closest = itemEntity;
            }
        }

        return closest;
    }

    private boolean isEdible(ItemStack stack) {
        return isManaged && isLivestockFeed(stack);
    }

    private static boolean isLivestockFeed(ItemStack stack) {
        return stack.is(KRItems.LIVESTOCK_FEED.get());
    }
}