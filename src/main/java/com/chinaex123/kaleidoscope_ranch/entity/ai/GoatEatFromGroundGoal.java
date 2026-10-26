package com.chinaex123.kaleidoscope_ranch.entity.ai;

import com.chinaex123.kaleidoscope_ranch.data.animal.hunger.AnimalHungerSavedData;
import com.chinaex123.kaleidoscope_ranch.entity.AnimalHungerEventHandler;
import com.chinaex123.kaleidoscope_ranch.entity.animation.KREatingAnimation;
import com.chinaex123.kaleidoscope_ranch.init.KRItems;
import com.chinaex123.kaleidoscope_ranch.init.tags.KREntityTags;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.animal.goat.Goat;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.AABB;

import java.util.EnumSet;
import java.util.List;
import java.util.UUID;

public class GoatEatFromGroundGoal extends Goal {

    private static final double REACH_DISTANCE_SQ = 1.8 * 1.8;
    private static final float FEED_HUNGER_GAIN = 0.5f;
    private static final float STOP_HUNGER = 7.0f;
    private static final float START_HUNGER = 5.0f;
    private static final double DETECT_RADIUS = 8.0;

    private final Goat goat;
    private final boolean isManaged;
    private ItemEntity targetItem;
    private int eatDelay;

    public GoatEatFromGroundGoal(Goat goat) {
        this.goat = goat;
        this.isManaged = KREntityTags.isManagedAnimal(goat);
        this.setFlags(EnumSet.of(Flag.MOVE, Flag.LOOK));
    }

    @Override
    public boolean canUse() {
        if (goat.level().isClientSide()) return false;
        if (goat.isBaby()) return false;
        ServerLevel level = (ServerLevel) goat.level();
        AnimalHungerSavedData hungerData = AnimalHungerEventHandler.getAnimalHungerData(level);
        float hunger = hungerData.getAnimalHungerValue(goat.getUUID());
        if (hunger > START_HUNGER) return false;
        targetItem = findNearbyEdibleItem();
        return targetItem != null;
    }

    @Override
    public boolean canContinueToUse() {
        if (targetItem == null || targetItem.isRemoved()) return false;
        if (goat.level().isClientSide()) return false;
        ServerLevel level = (ServerLevel) goat.level();
        AnimalHungerSavedData hungerData = AnimalHungerEventHandler.getAnimalHungerData(level);
        float hunger = hungerData.getAnimalHungerValue(goat.getUUID());
        return hunger < STOP_HUNGER;
    }

    @Override
    public void start() {
        eatDelay = 0;
        if (targetItem != null) {
            goat.getNavigation().moveTo(targetItem, 1.0);
        }
    }

    @Override
    public void tick() {
        if (targetItem == null || targetItem.isRemoved()) {
            stop();
            return;
        }
        goat.getLookControl().setLookAt(targetItem, 10.0f, 10.0f);
        double distSq = goat.distanceToSqr(targetItem);
        if (distSq <= REACH_DISTANCE_SQ) {
            goat.getNavigation().stop();
            eatDelay++;
            if (eatDelay >= 20) {
                eatDelay = 0;
                if (goat instanceof KREatingAnimation anim) {
                    anim.KR$startEatingAnimation();
                }
                performEat();
            }
        } else {
            if (goat.getNavigation().isDone() || eatDelay > 0) {
                eatDelay = 0;
                goat.getNavigation().moveTo(targetItem, 1.0);
            }
        }
    }

    @Override
    public void stop() {
        targetItem = null;
        eatDelay = 0;
        goat.getNavigation().stop();
    }

    private void performEat() {
        if (targetItem == null || targetItem.isRemoved()) return;
        if (!(goat.level() instanceof ServerLevel level)) return;
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
        UUID uuid = goat.getUUID();
        hungerData.addAnimalHungerValue(uuid, FEED_HUNGER_GAIN);
        AnimalHungerEventHandler.syncHungerToTrackingPlayers(goat, hungerData);
    }

    private ItemEntity findNearbyEdibleItem() {
        AABB searchBox = goat.getBoundingBox().inflate(DETECT_RADIUS);
        List<ItemEntity> items = goat.level().getEntitiesOfClass(ItemEntity.class, searchBox);
        ItemEntity closest = null;
        double closestDist = Double.MAX_VALUE;
        for (ItemEntity itemEntity : items) {
            if (itemEntity.isRemoved()) continue;
            ItemStack stack = itemEntity.getItem();
            if (!isEdible(stack)) continue;
            double dist = goat.distanceToSqr(itemEntity);
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