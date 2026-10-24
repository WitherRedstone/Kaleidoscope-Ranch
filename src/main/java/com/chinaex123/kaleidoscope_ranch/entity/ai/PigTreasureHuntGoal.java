package com.chinaex123.kaleidoscope_ranch.entity.ai;

import com.chinaex123.kaleidoscope_ranch.data.animal.favor.AnimalFavorSavedData;
import com.chinaex123.kaleidoscope_ranch.entity.AnimalFavorEventHandler;
import com.chinaex123.kaleidoscope_ranch.init.KRItems;
import com.chinaex123.kaleidoscope_ranch.init.tags.KREntityTags;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.animal.Pig;
import net.minecraft.world.item.ItemStack;

public class PigTreasureHuntGoal extends Goal {

    private static final int TREASURE_INTERVAL = 6000;
    private static final float TREASURE_CHANCE = 0.3f;
    private static final double FAVOR_THRESHOLD = 110.0;

    private final Pig pig;
    private int cooldown;

    public PigTreasureHuntGoal(Pig pig) {
        this.pig = pig;
    }

    @Override
    public boolean canUse() {
        if (pig.level().isClientSide()) return false;
        if (pig.isBaby()) return false;
        if (!KREntityTags.isManagedAnimal(pig)) return false;

        if (cooldown > 0) {
            cooldown--;
            return false;
        }

        ServerLevel level = (ServerLevel) pig.level();
        AnimalFavorSavedData favorData = AnimalFavorEventHandler.getAnimalFavorData(level);
        float favor = favorData.getAnimalFavorValue(pig.getUUID());

        if (favor < FAVOR_THRESHOLD) return false;

        return level.random.nextFloat() < TREASURE_CHANCE;
    }

    @Override
    public void start() {
        if (!(pig.level() instanceof ServerLevel level)) return;

        ItemStack treasure = generateTreasure(level);
        if (!treasure.isEmpty()) {
            pig.spawnAtLocation(treasure);
        }

        cooldown = TREASURE_INTERVAL;
    }

    private ItemStack generateTreasure(ServerLevel level) {
        float roll = level.random.nextFloat();
        if (roll < 0.4f) {
            return new ItemStack(KRItems.ROSE_SALT.get(), 1 + level.random.nextInt(3));
        } else if (roll < 0.7f) {
            return new ItemStack(net.minecraft.world.item.Items.GOLD_NUGGET, 1 + level.random.nextInt(3));
        } else if (roll < 0.9f) {
            return new ItemStack(net.minecraft.world.item.Items.EMERALD, 1);
        } else {
            return new ItemStack(net.minecraft.world.item.Items.DIAMOND, 1);
        }
    }
}