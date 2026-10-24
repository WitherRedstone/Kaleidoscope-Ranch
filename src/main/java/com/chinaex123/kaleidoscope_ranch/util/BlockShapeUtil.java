package com.chinaex123.kaleidoscope_ranch.util;

import net.minecraft.core.Direction;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.phys.shapes.VoxelShape;

public final class BlockShapeUtil {

    public static final VoxelShape FEEDING_TROUGH_NORTH = Block.box(0, 0, 2, 16, 10, 16);
    public static final VoxelShape FEEDING_TROUGH_SOUTH = Block.box(0, 0, 0, 16, 10, 14);
    public static final VoxelShape FEEDING_TROUGH_EAST = Block.box(0, 0, 0, 14, 10, 16);
    public static final VoxelShape FEEDING_TROUGH_WEST = Block.box(2, 0, 0, 16, 10, 16);

    public static final VoxelShape HAYSTACK_HOLLOW_SHAPE = Block.box(0.5, 0, 0.5, 15.5, 5, 15.5);

    private BlockShapeUtil() {}

    public static VoxelShape getFeedingTroughShape(Direction facing) {
        return switch (facing) {
            case NORTH -> FEEDING_TROUGH_NORTH;
            case SOUTH -> FEEDING_TROUGH_SOUTH;
            case EAST -> FEEDING_TROUGH_EAST;
            case WEST -> FEEDING_TROUGH_WEST;
            default -> FEEDING_TROUGH_NORTH;
        };
    }

    public static VoxelShape getHaystackHollowShape(Direction facing) {
        return HAYSTACK_HOLLOW_SHAPE;
    }
}