package com.chinaex123.kaleidoscope_ranch.entity.ai;

import com.chinaex123.kaleidoscope_ranch.data.animal.hunger.AnimalHungerSavedData;
import com.chinaex123.kaleidoscope_ranch.entity.AnimalHungerEventHandler;
import com.chinaex123.kaleidoscope_ranch.entity.animation.KREatingAnimation;
import com.chinaex123.kaleidoscope_ranch.init.KRItems;
import com.chinaex123.kaleidoscope_ranch.init.tags.KREntityTags;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.animal.Sheep;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.phys.AABB;

import java.util.EnumSet;
import java.util.List;
import java.util.UUID;

public class SheepEatFromGroundGoal extends Goal {

    private static final double REACH_DISTANCE_SQ = 1.8 * 1.8;
    private static final float WHEAT_HUNGER_GAIN = 0.1f;
    private static final float FEED_HUNGER_GAIN = 0.5f;
    private static final float STOP_HUNGER = 7.0f;
    private static final float START_HUNGER = 5.0f;
    private static final double DETECT_RADIUS = 8.0;

    private final Sheep sheep;
    private final boolean isManaged;
    private ItemEntity targetItem;
    private int eatDelay;

    public SheepEatFromGroundGoal(Sheep sheep) {
        this.sheep = sheep;
        this.isManaged = KREntityTags.isManagedAnimal(sheep);
        this.setFlags(EnumSet.of(Flag.MOVE, Flag.LOOK));
    }

    @Override
    public boolean canUse() {
        if (sheep.level().isClientSide()) return false;
        if (sheep.isBaby()) return false;

        ServerLevel level = (ServerLevel) sheep.level();
        AnimalHungerSavedData hungerData = AnimalHungerEventHandler.getAnimalHungerData(level);
        float hunger = hungerData.getAnimalHungerValue(sheep.getUUID());

        if (hunger > START_HUNGER) return false;

        targetItem = findNearbyEdibleItem();
        return targetItem != null;
    }

    @Override
    public boolean canContinueToUse() {
        if (targetItem == null || targetItem.isRemoved()) return false;
        if (sheep.level().isClientSide()) return false;

        ServerLevel level = (ServerLevel) sheep.level();
        AnimalHungerSavedData hungerData = AnimalHungerEventHandler.getAnimalHungerData(level);
        float hunger = hungerData.getAnimalHungerValue(sheep.getUUID());

        return hunger < STOP_HUNGER;
    }

    @Override
    public void start() {
        eatDelay = 0;
        if (targetItem != null) {
            sheep.getNavigation().moveTo(targetItem, 1.0);
        }
    }

    @Override
    public void tick() {
        if (targetItem == null || targetItem.isRemoved()) {
            stop();
            return;
        }

        sheep.getLookControl().setLookAt(targetItem, 10.0f, 10.0f);

        double distSq = sheep.distanceToSqr(targetItem);

        if (distSq <= REACH_DISTANCE_SQ) {
            sheep.getNavigation().stop();
            eatDelay++;

            if (eatDelay >= 20) {
                eatDelay = 0;
                if (sheep instanceof KREatingAnimation anim) {
                    anim.KR$startEatingAnimation();
                }
                performEat();
            }
        } else {
            if (sheep.getNavigation().isDone() || eatDelay > 0) {
                eatDelay = 0;
                sheep.getNavigation().moveTo(targetItem, 1.0);
            }
        }
    }

    @Override
    public void stop() {
        targetItem = null;
        eatDelay = 0;
        sheep.getNavigation().stop();
    }

    private void performEat() {
        if (targetItem == null || targetItem.isRemoved()) return;
        if (!(sheep.level() instanceof ServerLevel level)) return;

        ItemStack stack = targetItem.getItem();
        if (stack.isEmpty()) return;

        float gain = 0;
        if (isWheat(stack)) {
            gain = WHEAT_HUNGER_GAIN;
        } else if (isManaged && isLivestockFeed(stack)) {
            gain = FEED_HUNGER_GAIN;
        }

        if (gain <= 0) return;

        stack.shrink(1);
        if (stack.isEmpty()) {
            targetItem.discard();
        } else {
            targetItem.setItem(stack);
        }

        AnimalHungerSavedData hungerData = AnimalHungerEventHandler.getAnimalHungerData(level);
        UUID uuid = sheep.getUUID();
        hungerData.addAnimalHungerValue(uuid, gain);
        AnimalHungerEventHandler.syncHungerToTrackingPlayers(sheep, hungerData);
    }

    private ItemEntity findNearbyEdibleItem() {
        AABB searchBox = sheep.getBoundingBox().inflate(DETECT_RADIUS);
        List<ItemEntity> items = sheep.level().getEntitiesOfClass(ItemEntity.class, searchBox);

        ItemEntity closest = null;
        double closestDist = Double.MAX_VALUE;

        for (ItemEntity itemEntity : items) {
            if (itemEntity.isRemoved()) continue;
            ItemStack stack = itemEntity.getItem();
            if (!isEdible(stack)) continue;

            double dist = sheep.distanceToSqr(itemEntity);
            if (dist < closestDist) {
                closestDist = dist;
                closest = itemEntity;
            }
        }

        return closest;
    }

    private boolean isEdible(ItemStack stack) {
        if (isWheat(stack)) return true;
        return isManaged && isLivestockFeed(stack);
    }

    private static boolean isWheat(ItemStack stack) {
        return stack.is(Items.WHEAT);
    }

    private static boolean isLivestockFeed(ItemStack stack) {
        return stack.is(KRItems.LIVESTOCK_FEED.get());
    }
}