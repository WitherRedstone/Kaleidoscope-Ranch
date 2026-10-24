package com.chinaex123.kaleidoscope_ranch.entity.ai;

import com.chinaex123.kaleidoscope_ranch.data.animal.hunger.AnimalHungerSavedData;
import com.chinaex123.kaleidoscope_ranch.entity.AnimalHungerEventHandler;
import com.chinaex123.kaleidoscope_ranch.entity.animation.KREatingAnimation;
import com.chinaex123.kaleidoscope_ranch.init.KRItems;
import com.chinaex123.kaleidoscope_ranch.init.tags.KREntityTags;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.animal.Chicken;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.AABB;
import net.neoforged.neoforge.common.Tags;

import java.util.EnumSet;
import java.util.List;
import java.util.UUID;

public class ChickenEatFromGroundGoal extends Goal {

    private static final double REACH_DISTANCE_SQ = 1.8 * 1.8;
    private static final float SEED_HUNGER_GAIN = 0.1f;
    private static final float POULTRY_FEED_HUNGER_GAIN = 0.5f;
    private static final float STOP_HUNGER = 7.0f;
    private static final float START_HUNGER = 5.0f;
    private static final double DETECT_RADIUS = 8.0;

    private final Chicken chicken;
    private final boolean isManaged;
    private ItemEntity targetItem;
    private int eatDelay;

    public ChickenEatFromGroundGoal(Chicken chicken) {
        this.chicken = chicken;
        this.isManaged = KREntityTags.isManagedAnimal(chicken);
        this.setFlags(EnumSet.of(Flag.MOVE, Flag.LOOK));
    }

    @Override
    public boolean canUse() {
        if (chicken.level().isClientSide()) return false;
        if (chicken.isBaby()) return false;

        ServerLevel level = (ServerLevel) chicken.level();
        AnimalHungerSavedData hungerData = AnimalHungerEventHandler.getAnimalHungerData(level);
        float hunger = hungerData.getAnimalHungerValue(chicken.getUUID());

        if (hunger > START_HUNGER) return false;

        targetItem = findNearbyEdibleItem();
        return targetItem != null;
    }

    @Override
    public boolean canContinueToUse() {
        if (targetItem == null || targetItem.isRemoved()) return false;
        if (chicken.level().isClientSide()) return false;

        ServerLevel level = (ServerLevel) chicken.level();
        AnimalHungerSavedData hungerData = AnimalHungerEventHandler.getAnimalHungerData(level);
        float hunger = hungerData.getAnimalHungerValue(chicken.getUUID());

        return hunger < STOP_HUNGER;
    }

    @Override
    public void start() {
        eatDelay = 0;
        if (targetItem != null) {
            chicken.getNavigation().moveTo(targetItem, 1.0);
        }
    }

    @Override
    public void tick() {
        if (targetItem == null || targetItem.isRemoved()) {
            stop();
            return;
        }

        chicken.getLookControl().setLookAt(targetItem, 10.0f, 10.0f);

        double distSq = chicken.distanceToSqr(targetItem);

        if (distSq <= REACH_DISTANCE_SQ) {
            chicken.getNavigation().stop();
            eatDelay++;

            if (eatDelay >= 20) {
                eatDelay = 0;
                if (chicken instanceof KREatingAnimation anim) {
                    anim.KR$startEatingAnimation();
                }
                performEat();
            }
        } else {
            if (chicken.getNavigation().isDone() || eatDelay > 0) {
                eatDelay = 0;
                chicken.getNavigation().moveTo(targetItem, 1.0);
            }
        }
    }

    @Override
    public void stop() {
        targetItem = null;
        eatDelay = 0;
        chicken.getNavigation().stop();
    }

    private void performEat() {
        if (targetItem == null || targetItem.isRemoved()) return;
        if (!(chicken.level() instanceof ServerLevel level)) return;

        ItemStack stack = targetItem.getItem();
        if (stack.isEmpty()) return;

        float gain = 0;
        if (isPoultryFeed(stack)) {
            gain = POULTRY_FEED_HUNGER_GAIN;
        } else if (isSeed(stack)) {
            gain = SEED_HUNGER_GAIN;
        }

        if (gain <= 0) return;

        stack.shrink(1);
        if (stack.isEmpty()) {
            targetItem.discard();
        } else {
            targetItem.setItem(stack);
        }

        AnimalHungerSavedData hungerData = AnimalHungerEventHandler.getAnimalHungerData(level);
        UUID uuid = chicken.getUUID();
        hungerData.addAnimalHungerValue(uuid, gain);
        AnimalHungerEventHandler.syncHungerToTrackingPlayers(chicken, hungerData);
    }

    private ItemEntity findNearbyEdibleItem() {
        AABB searchBox = chicken.getBoundingBox().inflate(DETECT_RADIUS);
        List<ItemEntity> items = chicken.level().getEntitiesOfClass(ItemEntity.class, searchBox);

        ItemEntity closest = null;
        double closestDist = Double.MAX_VALUE;

        for (ItemEntity itemEntity : items) {
            if (itemEntity.isRemoved()) continue;
            ItemStack stack = itemEntity.getItem();
            if (!isEdible(stack)) continue;

            double dist = chicken.distanceToSqr(itemEntity);
            if (dist < closestDist) {
                closestDist = dist;
                closest = itemEntity;
            }
        }

        return closest;
    }

    private boolean isEdible(ItemStack stack) {
        if (isSeed(stack)) return true;
        if (isManaged && isPoultryFeed(stack)) return true;
        return false;
    }

    private static boolean isSeed(ItemStack stack) {
        return stack.is(Tags.Items.SEEDS);
    }

    private static boolean isPoultryFeed(ItemStack stack) {
        return stack.is(KRItems.POULTRY_FEED.get());
    }
}