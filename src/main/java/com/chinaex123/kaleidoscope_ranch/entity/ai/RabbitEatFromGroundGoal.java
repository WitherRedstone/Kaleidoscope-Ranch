package com.chinaex123.kaleidoscope_ranch.entity.ai;

import com.chinaex123.kaleidoscope_ranch.data.animal.hunger.AnimalHungerSavedData;
import com.chinaex123.kaleidoscope_ranch.entity.AnimalHungerEventHandler;
import com.chinaex123.kaleidoscope_ranch.entity.animation.KREatingAnimation;
import com.chinaex123.kaleidoscope_ranch.init.KRItems;
import com.chinaex123.kaleidoscope_ranch.init.tags.KREntityTags;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.animal.Rabbit;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.phys.AABB;

import java.util.EnumSet;
import java.util.List;
import java.util.UUID;

public class RabbitEatFromGroundGoal extends Goal {

    private static final double REACH_DISTANCE_SQ = 1.8 * 1.8;
    private static final float CARROT_HUNGER_GAIN = 0.1f;
    private static final float FEED_HUNGER_GAIN = 0.5f;
    private static final float STOP_HUNGER = 7.0f;
    private static final float START_HUNGER = 5.0f;
    private static final double DETECT_RADIUS = 8.0;

    private final Rabbit rabbit;
    private final boolean isManaged;
    private ItemEntity targetItem;
    private int eatDelay;

    public RabbitEatFromGroundGoal(Rabbit rabbit) {
        this.rabbit = rabbit;
        this.isManaged = KREntityTags.isManagedAnimal(rabbit);
        this.setFlags(EnumSet.of(Flag.MOVE, Flag.LOOK));
    }

    @Override
    public boolean canUse() {
        if (rabbit.level().isClientSide()) return false;
        if (rabbit.isBaby()) return false;

        ServerLevel level = (ServerLevel) rabbit.level();
        AnimalHungerSavedData hungerData = AnimalHungerEventHandler.getAnimalHungerData(level);
        float hunger = hungerData.getAnimalHungerValue(rabbit.getUUID());

        if (hunger > START_HUNGER) return false;

        targetItem = findNearbyEdibleItem();
        return targetItem != null;
    }

    @Override
    public boolean canContinueToUse() {
        if (targetItem == null || targetItem.isRemoved()) return false;
        if (rabbit.level().isClientSide()) return false;

        ServerLevel level = (ServerLevel) rabbit.level();
        AnimalHungerSavedData hungerData = AnimalHungerEventHandler.getAnimalHungerData(level);
        float hunger = hungerData.getAnimalHungerValue(rabbit.getUUID());

        return hunger < STOP_HUNGER;
    }

    @Override
    public void start() {
        eatDelay = 0;
        if (targetItem != null) {
            rabbit.getNavigation().moveTo(targetItem, 1.0);
        }
    }

    @Override
    public void tick() {
        if (targetItem == null || targetItem.isRemoved()) {
            stop();
            return;
        }

        rabbit.getLookControl().setLookAt(targetItem, 10.0f, 10.0f);

        double distSq = rabbit.distanceToSqr(targetItem);

        if (distSq <= REACH_DISTANCE_SQ) {
            rabbit.getNavigation().stop();
            eatDelay++;

            if (eatDelay >= 20) {
                eatDelay = 0;
                if (rabbit instanceof KREatingAnimation anim) {
                    anim.KR$startEatingAnimation();
                }
                performEat();
            }
        } else {
            if (rabbit.getNavigation().isDone() || eatDelay > 0) {
                eatDelay = 0;
                rabbit.getNavigation().moveTo(targetItem, 1.0);
            }
        }
    }

    @Override
    public void stop() {
        targetItem = null;
        eatDelay = 0;
        rabbit.getNavigation().stop();
    }

    private void performEat() {
        if (targetItem == null || targetItem.isRemoved()) return;
        if (!(rabbit.level() instanceof ServerLevel level)) return;

        ItemStack stack = targetItem.getItem();
        if (stack.isEmpty()) return;

        float gain = 0;
        if (isCarrot(stack)) {
            gain = CARROT_HUNGER_GAIN;
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
        UUID uuid = rabbit.getUUID();
        hungerData.addAnimalHungerValue(uuid, gain);
        AnimalHungerEventHandler.syncHungerToTrackingPlayers(rabbit, hungerData);
    }

    private ItemEntity findNearbyEdibleItem() {
        AABB searchBox = rabbit.getBoundingBox().inflate(DETECT_RADIUS);
        List<ItemEntity> items = rabbit.level().getEntitiesOfClass(ItemEntity.class, searchBox);

        ItemEntity closest = null;
        double closestDist = Double.MAX_VALUE;

        for (ItemEntity itemEntity : items) {
            if (itemEntity.isRemoved()) continue;
            ItemStack stack = itemEntity.getItem();
            if (!isEdible(stack)) continue;

            double dist = rabbit.distanceToSqr(itemEntity);
            if (dist < closestDist) {
                closestDist = dist;
                closest = itemEntity;
            }
        }

        return closest;
    }

    private boolean isEdible(ItemStack stack) {
        if (isCarrot(stack)) return true;
        return isManaged && isLivestockFeed(stack);
    }

    private static boolean isCarrot(ItemStack stack) {
        return stack.is(Items.CARROT);
    }

    private static boolean isLivestockFeed(ItemStack stack) {
        return stack.is(KRItems.LIVESTOCK_FEED.get());
    }
}