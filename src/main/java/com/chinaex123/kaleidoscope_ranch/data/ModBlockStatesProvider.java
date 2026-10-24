package com.chinaex123.kaleidoscope_ranch.data;

import com.chinaex123.kaleidoscope_ranch.KaleidoscopeRanch;
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