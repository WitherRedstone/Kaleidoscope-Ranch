package com.chinaex123.kaleidoscope_ranch.init;

import net.minecraft.world.level.block.LiquidBlock;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.MapColor;
import net.neoforged.neoforge.fluids.BaseFlowingFluid;
import net.neoforged.neoforge.registries.DeferredBlock;

import java.util.function.Supplier;

import static com.chinaex123.kaleidoscope_ranch.init.KRBlocks.BLOCKS;

public interface KRFluidBlocks {

    /** 羊奶 */
    DeferredBlock<LiquidBlock> SHEEP_MILK_BLOCK = register("sheep_milk_block", KRFluids.SHEEP_MILK.source, 100.0F, 0.6F, 1.0F, 1.0F, MapColor.SNOW);
    /** 山羊奶 */
    DeferredBlock<LiquidBlock> GOAT_MILK_BLOCK = register("goat_milk_block", KRFluids.GOAT_MILK.source, 100.0F, 0.6F, 1.0F, 1.0F, MapColor.SNOW);
    /** 马奶 */
    DeferredBlock<LiquidBlock> HORSE_MILK_BLOCK = register("horse_milk_block", KRFluids.HORSE_MILK.source, 100.0F, 0.6F, 1.0F, 1.0F, MapColor.SNOW);
    /** 驴奶 */
    DeferredBlock<LiquidBlock> DONKEY_MILK_BLOCK = register("donkey_milk_block", KRFluids.DONKEY_MILK.source, 100.0F, 0.6F, 1.0F, 1.0F, MapColor.SNOW);
    /** 骆驼奶 */
    DeferredBlock<LiquidBlock> CAMEL_MILK_BLOCK = register("camel_milk_block", KRFluids.CAMEL_MILK.source, 100.0F, 0.6F, 1.0F, 1.0F, MapColor.SNOW);
    /** 羊驼奶 */
    DeferredBlock<LiquidBlock> ALPACA_MILK_BLOCK = register("alpaca_milk_block", KRFluids.ALPACA_MILK.source, 100.0F, 0.6F, 1.0F, 1.0F, MapColor.SNOW);
    /** 兔奶 */
    DeferredBlock<LiquidBlock> RABBIT_MILK_BLOCK = register("rabbit_milk_block", KRFluids.RABBIT_MILK.source, 100.0F, 0.6F, 1.0F, 1.0F, MapColor.SNOW);
    /** 猪奶 */
    DeferredBlock<LiquidBlock> PIG_MILK_BLOCK = register("pig_milk_block", KRFluids.PIG_MILK.source, 100.0F, 0.6F, 1.0F, 1.0F, MapColor.SNOW);

    /**
     * @param name         方块的注册名称
     * @param fluid        源流体的 Supplier，延迟获取 Source 实例
     * @param hardness     方块硬度（挖掘难度）
     * @param friction     方块摩擦力（影响实体在其上的滑行）
     * @param speedFactor  速度因子（影响实体在该方块中的移动速度）
     * @param jumpFactor   跳跃因子（影响实体在该方块上的跳跃高度）
     * @param mapColor     地图颜色（用于地图上显示的颜色）
     * @return             返回延迟注册的 LiquidBlock 方块对象
     */
    static DeferredBlock<LiquidBlock> register(String name, Supplier<BaseFlowingFluid.Source> fluid, float hardness, float friction, float speedFactor, float jumpFactor, MapColor mapColor) {
        return BLOCKS.register(name, () -> new LiquidBlock(fluid.get(), BlockBehaviour.Properties.of()
                .noLootTable().liquid().replaceable().noCollission().strength(hardness).friction(friction)
                .speedFactor(speedFactor).jumpFactor(jumpFactor).mapColor(mapColor).sound(SoundType.EMPTY)));
    }
}
