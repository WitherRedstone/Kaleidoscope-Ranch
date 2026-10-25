package com.chinaex123.kaleidoscope_ranch.entity.ai;

import com.chinaex123.kaleidoscope_ranch.blockentity.FeedingTroughBlockEntity;
import com.chinaex123.kaleidoscope_ranch.config.KRConfig;
import com.chinaex123.kaleidoscope_ranch.data.animal.favor.FavorRise;
import com.chinaex123.kaleidoscope_ranch.data.animal.hunger.AnimalHunger;
import com.chinaex123.kaleidoscope_ranch.data.animal.hunger.AnimalHungerSavedData;
import com.chinaex123.kaleidoscope_ranch.data.animal.mood.AnimalMoodSavedData;
import com.chinaex123.kaleidoscope_ranch.entity.AnimalFavorEventHandler;
import com.chinaex123.kaleidoscope_ranch.entity.AnimalHungerEventHandler;
import com.chinaex123.kaleidoscope_ranch.entity.AnimalMoodEventHandler;
import com.chinaex123.kaleidoscope_ranch.entity.animation.KREatingAnimation;
import com.chinaex123.kaleidoscope_ranch.init.KRBlocks;
import com.chinaex123.kaleidoscope_ranch.init.KRItems;
import com.chinaex123.kaleidoscope_ranch.init.tags.KREntityTags;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;

import java.util.EnumSet;
import java.util.UUID;

public class KREatFromTroughGoal extends Goal {

    public static float getDetectRadius() {
        return KRConfig.TROUGH_DETECT_RADIUS.get().floatValue();
    }

    public static long getTroughFeedCooldownTicks() {
        return (long) KRConfig.TROUGH_FEED_COOLDOWN_TICKS.get();
    }

    private static final int EAT_DURATION = 40;
    private static final double REACH_DISTANCE = 1.8;

    private final Mob mob;
    private final Animal animal;
    private BlockPos targetTroughPos;
    private int eatTimer;
    private boolean isEating;
    private long nextScanTime = 0;

    public KREatFromTroughGoal(Mob mob) {
        this.mob = mob;
        this.animal = (mob instanceof Animal a) ? a : null;
        this.setFlags(EnumSet.of(Flag.MOVE, Flag.LOOK));
    }

    @Override
    public boolean canUse() {
        if (animal == null) return false;
        if (mob.level().isClientSide()) return false;

        ServerLevel level = (ServerLevel) mob.level();

        AnimalMoodSavedData moodData = AnimalMoodEventHandler.getAnimalMoodData(level);
        if (moodData.isWild(animal.getUUID())) return false;

        long now = level.getGameTime();
        if (now < nextScanTime) return false;
        nextScanTime = now + 20;

        Item feedItem = getFeedItemFor(mob);
        if (feedItem == null) return false;

        AnimalHungerSavedData hungerData = AnimalHungerEventHandler.getAnimalHungerData(level);
        UUID uuid = animal.getUUID();
        if (!hungerData.hasAnimalHungerData(uuid)) return false;

        float currentHunger = hungerData.getAnimalHungerValue(uuid);
        if (currentHunger >= AnimalHunger.getMaxHunger()) return false;

        if (!hungerData.isFeedCooldownExpired(uuid, now)) return false;

        if (!hungerData.isTroughFeedCooldownExpired(uuid, now)) return false;

        BlockPos troughPos = findNearbyTroughWithFeed(feedItem);
        if (troughPos == null) return false;

        targetTroughPos = troughPos;
        return true;
    }

    @Override
    public boolean canContinueToUse() {
        if (isEating) return eatTimer > 0;
        return targetTroughPos != null
                && mob.level().getBlockEntity(targetTroughPos) instanceof FeedingTroughBlockEntity;
    }

    @Override
    public void start() {
        isEating = false;
        eatTimer = 0;
        if (targetTroughPos != null) {
            mob.getNavigation().moveTo(
                    targetTroughPos.getX() + 0.5,
                    targetTroughPos.getY(),
                    targetTroughPos.getZ() + 0.5, 1.0);
        }
    }

    @Override
    public void tick() {
        if (isEating) {
            eatTimer--;
            mob.getLookControl().setLookAt(
                    targetTroughPos.getX() + 0.5,
                    targetTroughPos.getY() + 0.5,
                    targetTroughPos.getZ() + 0.5);
            mob.getNavigation().stop();

            if (eatTimer == 4) {
                performEat();
            }

            if (eatTimer <= 0) {
                isEating = false;
                targetTroughPos = null;
            }
            return;
        }

        if (targetTroughPos == null) return;

        double dx = mob.getX() - (targetTroughPos.getX() + 0.5);
        double dz = mob.getZ() - (targetTroughPos.getZ() + 0.5);
        double dy = mob.getY() - targetTroughPos.getY();

        if (dx * dx + dz * dz < REACH_DISTANCE * REACH_DISTANCE && Math.abs(dy) < 2.0) {
            isEating = true;
            eatTimer = EAT_DURATION;
            mob.getNavigation().stop();

            if (mob instanceof KREatingAnimation anim) {
                anim.KR$startEatingAnimation();
                if (mob.level() instanceof ServerLevel sl) {
                    sl.broadcastEntityEvent(mob, (byte) 60);
                }
            }

            mob.getLookControl().setLookAt(
                    targetTroughPos.getX() + 0.5,
                    targetTroughPos.getY() + 0.5,
                    targetTroughPos.getZ() + 0.5);
        } else if (mob.getNavigation().isDone()) {
            mob.getNavigation().moveTo(
                    targetTroughPos.getX() + 0.5,
                    targetTroughPos.getY(),
                    targetTroughPos.getZ() + 0.5, 1.0);
        }
    }

    @Override
    public void stop() {
        isEating = false;
        eatTimer = 0;
        targetTroughPos = null;
        mob.getNavigation().stop();
    }

    private void spawnHappyParticles(ServerLevel level, Animal animal) {
        double x = animal.getX();
        double y = animal.getY() + animal.getBbHeight() + 0.3;
        double z = animal.getZ();
        for (int i = 0; i < 5; i++) {
            double offsetX = (level.random.nextDouble() - 0.5) * 0.5;
            double offsetY = level.random.nextDouble() * 0.3;
            double offsetZ = (level.random.nextDouble() - 0.5) * 0.5;
            level.sendParticles(ParticleTypes.HAPPY_VILLAGER,
                    x + offsetX, y + offsetY, z + offsetZ,
                    1, 0, 0.02, 0, 0);
        }
    }

    private void performEat() {
        if (!(mob.level() instanceof ServerLevel level)) return;
        BlockEntity be = level.getBlockEntity(targetTroughPos);
        if (!(be instanceof FeedingTroughBlockEntity trough)) return;

        Item feedItem = getFeedItemFor(mob);
        if (feedItem == null) return;

        ItemStack consumed = trough.consumeOneFeed(feedItem);
        if (consumed.isEmpty()) return;

        AnimalHungerSavedData hungerData = AnimalHungerEventHandler.getAnimalHungerData(level);
        UUID uuid = animal.getUUID();
        long currentGameTime = level.getGameTime();

        float gain = KRConfig.TROUGH_FEED_GAIN.get().floatValue();
        if (gain <= 0) return;

        hungerData.addAnimalHungerValue(uuid, gain);
        hungerData.setLastTroughFeedGameTime(uuid, currentGameTime);
        AnimalHungerEventHandler.syncHungerToTrackingPlayers(animal, hungerData);

        AnimalFavorEventHandler.addFavor(level, animal, FavorRise.calcHungerRiseFavor(gain));

        spawnHappyParticles(level, animal);
    }

    private Item getFeedItemFor(Mob mob) {
        // 根据 tag 决定饲料类型
        if (mob.getType().is(KREntityTags.POULTRY)) {
            return KRItems.POULTRY_FEED.get();
        }
        if (mob.getType().is(KREntityTags.LIVESTOCK)) {
            return KRItems.LIVESTOCK_FEED.get();
        }
        return null;
    }

    private BlockPos findNearbyTroughWithFeed(Item feedItem) {
        BlockPos animalPos = mob.blockPosition();
        int r = (int) getDetectRadius();

        for (int dx = -r; dx <= r; dx++) {
            for (int dy = -3; dy <= 3; dy++) {
                for (int dz = -r; dz <= r; dz++) {
                    BlockPos pos = animalPos.offset(dx, dy, dz);
                    if (!mob.level().getBlockState(pos).is(KRBlocks.FEEDING_TROUGH.get())) continue;

                    if (mob.level().getBlockEntity(pos) instanceof FeedingTroughBlockEntity trough) {
                        for (int i = 0; i < FeedingTroughBlockEntity.SLOT_COUNT; i++) {
                            ItemStack stack = trough.getItem(i);
                            if (!stack.isEmpty() && stack.is(feedItem)) {
                                return pos;
                            }
                        }
                    }
                }
            }
        }
        return null;
    }
}