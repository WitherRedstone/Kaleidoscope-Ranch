package com.chinaex123.kaleidoscope_ranch.data.animal.mood;

import com.chinaex123.kaleidoscope_ranch.data.animal.AnimalMood;
import com.chinaex123.kaleidoscope_ranch.init.KRBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.level.block.Block;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * 动物心情下降类。
 * <p>
 * 负责计算动物心情的下降量，
 * 包括物种多样性惩罚、粪便惩罚、雨天惩罚、受伤惩罚，
 * 以及低于初始心情时的缓慢恢复。
 */
public class MoodDecline {

    /** 初始心情分数 */
    public static final int INITIAL_MOOD = AnimalMood.INITIAL_MOOD;

    /** 物种多样性检测半径 */
    public static final int DIVERSE_SPECIES_CHECK_RADIUS = 4;
    /** 触发物种多样性惩罚所需的物种数量 */
    public static final int DIVERSE_SPECIES_REQUIRED = 3;
    /** 物种多样性惩罚的 tick 检查间隔 */
    public static final int DIVERSE_SPECIES_PENALTY_TICK_INTERVAL = 3600;
    /** 物种多样性惩罚量 */
    public static final int DIVERSE_SPECIES_PENALTY_AMOUNT = 1;

    /** 粪便检测半径 */
    public static final int FECES_CHECK_RADIUS = 8;
    /** 粪便惩罚的 tick 检查间隔 */
    public static final int FECES_PENALTY_TICK_INTERVAL = 3600;
    /** 每份粪便的惩罚量 */
    public static final int FECES_PENALTY_PER_FECES = 2;

    /** 雨天惩罚的 tick 检查间隔 */
    public static final int RAIN_PENALTY_TICK_INTERVAL = 400;
    /** 雨天惩罚量 */
    public static final int RAIN_PENALTY_AMOUNT = 1;

    /** 受伤惩罚量 */
    public static final int HURT_PENALTY_AMOUNT = 5;

    /** 心情恢复的 tick 检查间隔 */
    public static final int MOOD_RECOVERY_TICK_INTERVAL = 3600;
    /** 心情恢复量 */
    public static final int MOOD_RECOVERY_AMOUNT = 1;

    /**
     * 计算 tick 触发的心情下降量。
     * <p>
     * 根据各自的 tick 间隔检测物种多样性、粪便与雨天惩罚，
     * 并将结果累加为负值返回。
     *
     * @param level     服务端世界
     * @param animal    动物
     * @param tickCount 当前 tick 数
     * @return 心情变化量（负值）
     */
    public static int calcTickPenalty(ServerLevel level, Animal animal, long tickCount) {
        int delta = 0;
        if (tickCount % DIVERSE_SPECIES_PENALTY_TICK_INTERVAL == 0) {
            delta -= calcDiverseSpeciesPenalty(level, animal);
        }
        if (tickCount % FECES_PENALTY_TICK_INTERVAL == 0) {
            delta -= calcFecesPenalty(level, animal);
        }
        if (tickCount % RAIN_PENALTY_TICK_INTERVAL == 0) {
            delta -= calcRainPenalty(level, animal);
        }
        return delta;
    }

    /**
     * 计算受伤带来的心情惩罚。
     *
     * @return 受伤惩罚量
     */
    public static int calcHurtPenalty() {
        return HURT_PENALTY_AMOUNT;
    }

    /**
     * 计算心情恢复量。
     * <p>
     * 仅当当前心情低于初始心情时才会恢复。
     *
     * @param currentMood 当前心情
     * @return 心情恢复量
     */
    public static int calcRecovery(ServerLevel level, Animal animal, int currentMood) {
        if (currentMood >= INITIAL_MOOD) return 0;
        if (countNearbyFeces(level, animal.blockPosition()) > 0) return 0;
        if (level.isRaining() && animal.level().canSeeSky(animal.blockPosition())) return 0;
        return MOOD_RECOVERY_AMOUNT;
    }

    /**
     * 计算物种多样性带来的心情惩罚。
     * <p>
     * 统计动物周围指定半径内其他动物的物种数量，
     * 达到所需物种数量时返回惩罚量。
     *
     * @param level  服务端世界
     * @param animal 动物
     * @return 物种多样性惩罚量
     */
    public static int calcDiverseSpeciesPenalty(ServerLevel level, Animal animal) {
        List<? extends Animal> nearbyAnimals = level.getEntitiesOfClass(
                Animal.class,
                animal.getBoundingBox().inflate(DIVERSE_SPECIES_CHECK_RADIUS),
                other -> other != animal
        );
        Set<Class<? extends Animal>> speciesTypes = new HashSet<>();
        for (Animal nearby : nearbyAnimals) {
            speciesTypes.add(nearby.getClass());
        }
        return speciesTypes.size() >= DIVERSE_SPECIES_REQUIRED
                ? DIVERSE_SPECIES_PENALTY_AMOUNT : 0;
    }

    /**
     * 计算粪便带来的心情惩罚。
     * <p>
     * 惩罚量 = 附近粪便数量 × 每份粪便惩罚量。
     *
     * @param level  服务端世界
     * @param animal 动物
     * @return 粪便惩罚量
     */
    public static int calcFecesPenalty(ServerLevel level, Animal animal) {
        int fecesCount = countNearbyFeces(level, animal.blockPosition());
        return fecesCount * FECES_PENALTY_PER_FECES;
    }

    /**
     * 统计指定位置周围指定半径内的粪便数量。
     *
     * @param level  服务端世界
     * @param center 中心位置
     * @return 粪便数量
     */
    public static int countNearbyFeces(ServerLevel level, BlockPos center) {
        int count = 0;
        Block fecesBlock = KRBlocks.FECES.get();
        int r = FECES_CHECK_RADIUS;
        for (BlockPos pos : BlockPos.betweenClosed(
                center.getX() - r, center.getY() - r, center.getZ() - r,
                center.getX() + r, center.getY() + r, center.getZ() + r)) {
            if (level.getBlockState(pos).getBlock() == fecesBlock) {
                count++;
            }
        }
        return count;
    }

    /**
     * 计算雨天带来的心情惩罚。
     * <p>
     * 世界下雨且动物能看到天空时返回惩罚量，否则返回 0。
     *
     * @param level  服务端世界
     * @param animal 动物
     * @return 雨天惩罚量
     */
    public static int calcRainPenalty(ServerLevel level, Animal animal) {
        if (level.isRaining() && animal.level().canSeeSky(animal.blockPosition())) {
            return RAIN_PENALTY_AMOUNT;
        }
        return 0;
    }
}