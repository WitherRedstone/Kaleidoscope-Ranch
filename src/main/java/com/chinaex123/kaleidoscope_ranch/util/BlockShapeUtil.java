package com.chinaex123.kaleidoscope_ranch.util;

import net.minecraft.core.Direction;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.phys.shapes.VoxelShape;

public final class BlockShapeUtil {

    public static final VoxelShape NORTH_SHAPE = Block.box(0, 0, 2, 16, 10, 16);
    public static final VoxelShape SOUTH_SHAPE = Block.box(0, 0, 0, 16, 10, 14);
    public static final VoxelShape EAST_SHAPE = Block.box(0, 0, 0, 14, 10, 16);
    public static final VoxelShape WEST_SHAPE = Block.box(2, 0, 0, 16, 10, 16);

    private BlockShapeUtil() {}

    public static VoxelShape getHorizontalFacingShape(Direction facing) {
        return switch (facing) {
            case NORTH -> NORTH_SHAPE;
            case SOUTH -> SOUTH_SHAPE;
            case EAST -> EAST_SHAPE;
            case WEST -> WEST_SHAPE;
            default -> NORTH_SHAPE;
        };
    }
}