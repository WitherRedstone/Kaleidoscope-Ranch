package com.chinaex123.kaleidoscope_ranch.data.animal.mood;

import com.chinaex123.kaleidoscope_ranch.config.KRConfig;
import com.chinaex123.kaleidoscope_ranch.init.KRBlocks;
import com.chinaex123.kaleidoscope_ranch.init.tags.KREntityTags;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class MoodDecline {

    public static int getDiverseSpeciesPenaltyTickInterval() {
        return KRConfig.MOOD_DECLINE_DIVERSE_SPECIES_TICK_INTERVAL.get();
    }

    public static int getFecesPenaltyTickInterval() {
        return KRConfig.MOOD_DECLINE_FECES_TICK_INTERVAL.get();
    }

    public static int getRainPenaltyTickInterval() {
        return KRConfig.MOOD_DECLINE_RAIN_TICK_INTERVAL.get();
    }

    public static int getMoodRecoveryTickInterval() {
        return KRConfig.MOOD_DECLINE_RECOVERY_TICK_INTERVAL.get();
    }

    public static double calcTickPenalty(ServerLevel level, Animal animal, long tickCount) {
        double delta = 0;
        if (tickCount % KRConfig.MOOD_DECLINE_DIVERSE_SPECIES_TICK_INTERVAL.get() == 0) {
            delta -= calcDiverseSpeciesPenalty(level, animal);
        }
        if (tickCount % KRConfig.MOOD_DECLINE_FECES_TICK_INTERVAL.get() == 0) {
            delta -= calcFecesPenalty(level, animal);
        }
        if (tickCount % KRConfig.MOOD_DECLINE_RAIN_TICK_INTERVAL.get() == 0) {
            delta -= calcRainPenalty(level, animal);
        }
        return delta;
    }

    public static double calcHurtPenalty() {
        return KRConfig.MOOD_DECLINE_HURT_AMOUNT.get();
    }

    public static double calcRecovery(ServerLevel level, Animal animal, double currentMood) {
        if (currentMood >= AnimalMood.getInitialMood()) return 0;
        if (countNearbyFeces(level, animal.blockPosition()) > 0) return 0;
        if (level.isRaining() && animal.level().canSeeSky(animal.blockPosition())) return 0;
        return KRConfig.MOOD_DECLINE_RECOVERY_AMOUNT.get();
    }

    public static double calcDiverseSpeciesPenalty(ServerLevel level, Animal animal) {
        int radius = KRConfig.MOOD_DECLINE_DIVERSE_SPECIES_RADIUS.get();
        List<? extends Animal> nearbyAnimals = level.getEntitiesOfClass(
                Animal.class,
                animal.getBoundingBox().inflate(radius),
                other -> other != animal && KREntityTags.isManagedAnimal(other)
        );
        Set<Class<? extends Animal>> speciesTypes = new HashSet<>();
        for (Animal nearby : nearbyAnimals) {
            if (hasBlockObstruction(level, animal, nearby)) continue;
            speciesTypes.add(nearby.getClass());
        }
        return speciesTypes.size() >= KRConfig.MOOD_DECLINE_DIVERSE_SPECIES_REQUIRED.get()
                ? KRConfig.MOOD_DECLINE_DIVERSE_SPECIES_AMOUNT.get() : 0;
    }

    private static boolean hasBlockObstruction(ServerLevel level, Animal from, Animal to) {
        Vec3 start = from.getEyePosition();
        Vec3 end = to.getEyePosition();
        Vec3 direction = end.subtract(start);
        double distance = direction.length();
        if (distance < 0.1) return false;

        Vec3 step = direction.normalize().scale(0.5);
        int steps = (int) Math.ceil(distance / 0.5);
        BlockPos.MutableBlockPos mutablePos = new BlockPos.MutableBlockPos();
        for (int i = 1; i < steps; i++) {
            Vec3 pos = start.add(step.scale(i));
            mutablePos.set(pos.x, pos.y, pos.z);
            BlockState blockState = level.getBlockState(mutablePos);
            if (blockState.isSolidRender(level, mutablePos)) return true;
        }
        return false;
    }

    public static double calcFecesPenalty(ServerLevel level, Animal animal) {
        int fecesCount = countNearbyFeces(level, animal.blockPosition());
        return fecesCount * KRConfig.MOOD_DECLINE_FECES_AMOUNT.get();
    }

    public static int countNearbyFeces(ServerLevel level, BlockPos center) {
        int count = 0;
        Block fecesBlock = KRBlocks.FECES.get();
        int r = KRConfig.MOOD_DECLINE_FECES_RADIUS.get();
        for (BlockPos pos : BlockPos.betweenClosed(
                center.getX() - r, center.getY() - r, center.getZ() - r,
                center.getX() + r, center.getY() + r, center.getZ() + r)) {
            if (level.getBlockState(pos).getBlock() == fecesBlock) {
                count++;
            }
        }
        return count;
    }

    public static double calcRainPenalty(ServerLevel level, Animal animal) {
        if (level.isRaining() && animal.level().canSeeSky(animal.blockPosition())) {
            return KRConfig.MOOD_DECLINE_RAIN_AMOUNT.get();
        }
        return 0;
    }
}