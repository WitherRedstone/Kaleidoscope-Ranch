package com.chinaex123.kaleidoscope_ranch.data.animal.mood;

import com.chinaex123.kaleidoscope_ranch.init.KRBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.level.block.Block;

/**
 * 动物心情提升类。
 * <p>
 * 负责计算动物心情的提升量，
 * 包括抚摸加成与盐块加成。
 */
public class MoodRise {

    /** 抚摸带来的心情加成 */
    public static final int PET_MOOD_BONUS = 1;

    /** 盐块心情加成量 */
    public static final int SALT_BLOCK_MOOD_BONUS_AMOUNT = 1;
    /** 盐块检测半径 */
    public static final int SALT_BLOCK_CHECK_RADIUS = 4;
    /** 盐块心情加成的 tick 检查间隔 */
    public static final int SALT_BLOCK_MOOD_BONUS_TICK_INTERVAL = 4800;
    /** 盐块心情加成上限（不超过初始心情） */
    public static final int SALT_BLOCK_MOOD_CAP = AnimalMood.INITIAL_MOOD;

    /**
     * 计算 tick 触发的心情加成。
     * <p>
     * 每隔固定 tick 间隔检测一次盐块加成，
     * 未到间隔时返回 0。
     *
     * @param level       服务端世界
     * @param animal      动物
     * @param currentMood 当前心情
     * @param tickCount   当前 tick 数
     * @return 心情加成量
     */
    public static int calcTickBonus(ServerLevel level, Animal animal, int currentMood, long tickCount) {
        if (tickCount % SALT_BLOCK_MOOD_BONUS_TICK_INTERVAL == 0) {
            return calcSaltBlockBonus(level, animal, currentMood);
        }
        return 0;
    }

    /**
     * 计算抚摸带来的心情加成。
     *
     * @return 抚摸心情加成量
     */
    public static int calcPetBonus() {
        return PET_MOOD_BONUS;
    }

    /**
     * 计算盐块带来的心情加成。
     * <p>
     * 若当前心情已达到上限则返回 0；
     * 否则在动物周围检测盐块，存在则返回加成量。
     *
     * @param level       服务端世界
     * @param animal      动物
     * @param currentMood 当前心情
     * @return 盐块心情加成量
     */
    public static int calcSaltBlockBonus(ServerLevel level, Animal animal, int currentMood) {
        if (currentMood >= SALT_BLOCK_MOOD_CAP) return 0;

        Block saltBlock = KRBlocks.SALT_BLOCK.get();
        BlockPos center = animal.blockPosition();
        int r = SALT_BLOCK_CHECK_RADIUS;
        for (BlockPos pos : BlockPos.betweenClosed(
                center.getX() - r, center.getY() - r, center.getZ() - r,
                center.getX() + r, center.getY() + r, center.getZ() + r)) {
            if (level.getBlockState(pos).getBlock() == saltBlock) {
                return SALT_BLOCK_MOOD_BONUS_AMOUNT;
            }
        }
        return 0;
    }
}