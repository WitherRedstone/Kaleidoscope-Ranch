package com.chinaex123.kaleidoscope_ranch.entity.ai;

import com.chinaex123.kaleidoscope_ranch.config.KRConfig;
import com.chinaex123.kaleidoscope_ranch.data.animal.favor.AnimalFavorSavedData;
import com.chinaex123.kaleidoscope_ranch.entity.AnimalFavorEventHandler;
import com.chinaex123.kaleidoscope_ranch.init.tags.KREntityTags;
import com.chinaex123.kaleidoscope_ranch.init.tags.KRItemTags;
import net.minecraft.core.registries.Registries;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.animal.Pig;
import net.minecraft.world.item.ItemStack;

import java.util.List;

public class PigTreasureHuntGoal extends Goal {

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

        if (favor < KRConfig.PIG_TREASURE_FAVOR_THRESHOLD.get().floatValue()) return false;

        return level.random.nextFloat() < KRConfig.PIG_TREASURE_CHANCE.get().floatValue();
    }

    @Override
    public void start() {
        if (!(pig.level() instanceof ServerLevel level)) return;

        ItemStack treasure = generateTreasure(level);
        if (!treasure.isEmpty()) {
            pig.spawnAtLocation(treasure);
        }

        cooldown = KRConfig.PIG_TREASURE_INTERVAL.get();
    }

    private ItemStack generateTreasure(ServerLevel level) {
        List<ItemStack> treasures = level.registryAccess()
                .lookupOrThrow(Registries.ITEM)
                .getOrThrow(KRItemTags.TREASURE_HUNTING_PIG)
                .stream()
                .map(holder -> new ItemStack(holder.value(), 1 + level.random.nextInt(3)))
                .toList();
        if (treasures.isEmpty()) return ItemStack.EMPTY;
        return treasures.get(level.random.nextInt(treasures.size()));
    }
}