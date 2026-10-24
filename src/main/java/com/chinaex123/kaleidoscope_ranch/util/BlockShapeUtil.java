package com.chinaex123.kaleidoscope_ranch.util;

import net.minecraft.core.Direction;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.phys.shapes.VoxelShape;

public final class BlockShapeUtil {

    public static final VoxelShape FEEDING_TROUGH_SHAPE = Block.box(0.5, 0, 0.5, 15.5, 7, 15.5);
    public static final VoxelShape HAYSTACK_HOLLOW_SHAPE = Block.box(0.5, 0, 0.5, 15.5, 5, 15.5);

    private BlockShapeUtil() {}

    public static VoxelShape getFeedingTroughShape(Direction facing) {
        return FEEDING_TROUGH_SHAPE;
    }

    public static VoxelShape getHaystackHollowShape(Direction facing) {
        return HAYSTACK_HOLLOW_SHAPE;
    }
}