package com.chinaex123.kaleidoscope_ranch.data;

import com.chinaex123.kaleidoscope_ranch.KaleidoscopeRanch;
import com.chinaex123.kaleidoscope_ranch.block.FeedingTroughBlock;
import com.chinaex123.kaleidoscope_ranch.init.KRBlocks;
import net.minecraft.core.Direction;
import net.minecraft.data.PackOutput;
import net.minecraft.world.level.block.AmethystClusterBlock;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.neoforged.neoforge.client.model.generators.BlockStateProvider;
import net.neoforged.neoforge.client.model.generators.ModelFile;
import net.neoforged.neoforge.common.data.ExistingFileHelper;

public class ModBlockStatesProvider extends BlockStateProvider {
    public ModBlockStatesProvider(PackOutput output, ExistingFileHelper exFileHelper) {
        super(output, KaleidoscopeRanch.MODID, exFileHelper);
    }

    @Override
    protected void registerStatesAndModels() {
        simpleBlockWithItem(KRBlocks.ROSE_SALT_ROCK.get(), cubeAll(KRBlocks.ROSE_SALT_ROCK.get()));
        simpleBlockWithItem(KRBlocks.ROSE_SALT_CRYSTAL.get(), cubeAll(KRBlocks.ROSE_SALT_CRYSTAL.get()));
        simpleBlockWithItem(KRBlocks.ROSE_SALT_CRYSTAL_CORE.get(), cubeAll(KRBlocks.ROSE_SALT_CRYSTAL_CORE.get()));
        registerClusterBlock(KRBlocks.SMALL_ROSE_SALT_CRYSTAL_BUD.get(), "small_rose_salt_crystal_bud");
        registerClusterBlock(KRBlocks.MEDIUM_ROSE_SALT_CRYSTAL_BUD.get(), "medium_rose_salt_crystal_bud");
        registerClusterBlock(KRBlocks.LARGE_ROSE_SALT_CRYSTAL_BUD.get(), "large_rose_salt_crystal_bud");
        registerClusterBlock(KRBlocks.ROSE_SALT_CLUSTER.get(), "rose_salt_cluster");
        simpleBlockWithItem(KRBlocks.ROSE_SALT_BLOCK.get(), cubeAll(KRBlocks.ROSE_SALT_BLOCK.get()));
        simpleBlockItem(KRBlocks.HAYSTACK_HOLLOW.get(), registerHaystackHollow());
        simpleBlockItem(KRBlocks.FEEDING_TROUGH.get(), registerFeedingTrough());
    }

    private ModelFile registerHaystackHollow() {
        ModelFile model = models().getExistingFile(modLoc("block/haystack_hollow"));
        getVariantBuilder(KRBlocks.HAYSTACK_HOLLOW.get())
                .partialState().with(BlockStateProperties.HORIZONTAL_FACING, Direction.NORTH).modelForState().modelFile(model).addModel()
                .partialState().with(BlockStateProperties.HORIZONTAL_FACING, Direction.SOUTH).modelForState().modelFile(model).rotationY(180).addModel()
                .partialState().with(BlockStateProperties.HORIZONTAL_FACING, Direction.EAST).modelForState().modelFile(model).rotationY(90).addModel()
                .partialState().with(BlockStateProperties.HORIZONTAL_FACING, Direction.WEST).modelForState().modelFile(model).rotationY(270).addModel();
        return model;
    }

    private ModelFile registerFeedingTrough() {
        ModelFile[] troughModels = new ModelFile[7];
        troughModels[0] = models().getExistingFile(modLoc("block/feeding_trough/feeding_trough"));
        troughModels[1] = models().getExistingFile(modLoc("block/feeding_trough/feeding_trough_fill1"));
        troughModels[2] = models().getExistingFile(modLoc("block/feeding_trough/feeding_trough_fill2"));
        troughModels[3] = models().getExistingFile(modLoc("block/feeding_trough/feeding_trough_fill3"));
        troughModels[4] = models().getExistingFile(modLoc("block/feeding_trough/feeding_trough_fill4"));
        troughModels[5] = models().getExistingFile(modLoc("block/feeding_trough/feeding_trough_fill5"));
        troughModels[6] = models().getExistingFile(modLoc("block/feeding_trough/feeding_trough_fill6"));

        var troughBuilder = getVariantBuilder(KRBlocks.FEEDING_TROUGH.get());
        for (int fill = 0; fill <= 6; fill++) {
            for (Direction facing : new Direction[]{Direction.NORTH, Direction.SOUTH, Direction.EAST, Direction.WEST}) {
                int rotY = switch (facing) {
                    case SOUTH -> 180;
                    case EAST -> 90;
                    case WEST -> 270;
                    default -> 0;
                };
                troughBuilder.partialState()
                        .with(BlockStateProperties.HORIZONTAL_FACING, facing)
                        .with(FeedingTroughBlock.FILL_LEVEL, fill)
                        .modelForState().modelFile(troughModels[fill]).rotationY(rotY).addModel();
            }
        }

        return troughModels[0];
    }

    private void registerClusterBlock(AmethystClusterBlock block, String name) {
        ModelFile model = models().withExistingParent(name, mcLoc("block/cross"))
                .texture("cross", modLoc("block/" + name))
                .renderType("cutout");

        getVariantBuilder(block)
                .partialState().with(BlockStateProperties.FACING, Direction.UP).modelForState().modelFile(model).addModel()
                .partialState().with(BlockStateProperties.FACING, Direction.DOWN).modelForState().modelFile(model).rotationX(180).addModel()
                .partialState().with(BlockStateProperties.FACING, Direction.NORTH).modelForState().modelFile(model).rotationX(90).addModel()
                .partialState().with(BlockStateProperties.FACING, Direction.SOUTH).modelForState().modelFile(model).rotationX(90).rotationY(180).addModel()
                .partialState().with(BlockStateProperties.FACING, Direction.EAST).modelForState().modelFile(model).rotationX(90).rotationY(90).addModel()
                .partialState().with(BlockStateProperties.FACING, Direction.WEST).modelForState().modelFile(model).rotationX(90).rotationY(270).addModel();

        simpleBlockItem(block, model);
    }
}