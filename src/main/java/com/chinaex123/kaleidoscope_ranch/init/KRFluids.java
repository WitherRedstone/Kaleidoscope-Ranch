package com.chinaex123.kaleidoscope_ranch.init;

import com.chinaex123.kaleidoscope_ranch.KaleidoscopeRanch;
import com.chinaex123.kaleidoscope_ranch.util.KRColors;
import com.chinaex123.kaleidoscope_ranch.util.SimpleFluidExtension;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.BucketItem;
import net.minecraft.world.level.block.LiquidBlock;
import net.minecraft.world.level.material.Fluid;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.client.extensions.common.RegisterClientExtensionsEvent;
import net.neoforged.neoforge.fluids.BaseFlowingFluid;
import net.neoforged.neoforge.fluids.FluidType;
import net.neoforged.neoforge.registries.DeferredBlock;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

public interface KRFluids {
    DeferredRegister<Fluid> FLUIDS = DeferredRegister.create(BuiltInRegistries.FLUID, KaleidoscopeRanch.MODID);

    /** 羊奶 */
    FluidHolder SHEEP_MILK = registerFluid("sheep_milk", () -> KRItems.SHEEP_MILK_BUCKET, () -> KRFluidBlocks.SHEEP_MILK_BLOCK,
            KRFluidTypes.SHEEP_MILK_TYPE, 100.0F, 4, 1, 10);
    /** 山羊奶 */
    FluidHolder GOAT_MILK = registerFluid("goat_milk", () -> KRItems.GOAT_MILK_BUCKET, () -> KRFluidBlocks.GOAT_MILK_BLOCK,
            KRFluidTypes.GOAT_MILK_TYPE, 100.0F, 4, 1, 10);
    /** 马奶 */
    FluidHolder HORSE_MILK = registerFluid("horse_milk", () -> KRItems.HORSE_MILK_BUCKET, () -> KRFluidBlocks.HORSE_MILK_BLOCK,
            KRFluidTypes.HORSE_MILK_TYPE, 100.0F, 4, 1, 10);
    /** 驴奶 */
    FluidHolder DONKEY_MILK = registerFluid("donkey_milk", () -> KRItems.DONKEY_MILK_BUCKET, () -> KRFluidBlocks.DONKEY_MILK_BLOCK,
            KRFluidTypes.DONKEY_MILK_TYPE, 100.0F, 4, 1, 10);
    /** 骆驼奶 */
    FluidHolder CAMEL_MILK = registerFluid("camel_milk", () -> KRItems.CAMEL_MILK_BUCKET, () -> KRFluidBlocks.CAMEL_MILK_BLOCK,
            KRFluidTypes.CAMEL_MILK_TYPE, 100.0F, 4, 1, 10);
    /** 羊驼奶 */
    FluidHolder ALPACA_MILK = registerFluid("alpaca_milk", () -> KRItems.ALPACA_MILK_BUCKET, () -> KRFluidBlocks.ALPACA_MILK_BLOCK,
            KRFluidTypes.ALPACA_MILK_TYPE, 100.0F, 4, 1, 10);
    /** 兔奶 */
    FluidHolder RABBIT_MILK = registerFluid("rabbit_milk", () -> KRItems.RABBIT_MILK_BUCKET, () -> KRFluidBlocks.RABBIT_MILK_BLOCK,
            KRFluidTypes.RABBIT_MILK_TYPE, 100.0F, 4, 1, 10);
    /** 猪奶 */
    FluidHolder PIG_MILK = registerFluid("pig_milk", () -> KRItems.PIG_MILK_BUCKET, () -> KRFluidBlocks.PIG_MILK_BLOCK,
            KRFluidTypes.PIG_MILK_TYPE, 100.0F, 4, 1, 10);

    /**
     * @param elementName           流体名称
     * @param bucketSupplier        关联桶物品的 Supplier
     * @param blockSupplier         关联液体方块的 Supplier
     * @param typeHolder            已注册的流体类型的 DeferredHolder
     * @param explosionResistance   流体爆炸抗性
     * @param slopeFindDistance     寻找斜坡的距离
     * @param levelDecreasePerBlock 每格流体等级衰减
     * @param tickRate              流体更新刻率
     * @return                      返回持有 type / source / flowing 三个引用的 FluidHolder
     */
    private static FluidHolder registerFluid(
            String elementName,
            Supplier<DeferredItem<BucketItem>> bucketSupplier,
            Supplier<DeferredBlock<LiquidBlock>> blockSupplier,
            DeferredHolder<FluidType, FluidType> typeHolder,
            float explosionResistance,
            int slopeFindDistance,
            int levelDecreasePerBlock,
            int tickRate
    ) {
        FluidHolder holder = new FluidHolder();
        holder.type = typeHolder;

        holder.flowing = KRFluids.FLUIDS.register("flowing_" + elementName, () ->
                new BaseFlowingFluid.Flowing(
                        new BaseFlowingFluid.Properties(
                                holder.type,
                                holder.source,
                                holder.flowing
                        )
                                .bucket(bucketSupplier.get())
                                .block(blockSupplier.get())
                                .explosionResistance(explosionResistance)
                                .slopeFindDistance(slopeFindDistance)
                                .levelDecreasePerBlock(levelDecreasePerBlock)
                                .tickRate(tickRate)
                )
        );

        holder.source = KRFluids.FLUIDS.register(elementName, () ->
                new BaseFlowingFluid.Source(
                        new BaseFlowingFluid.Properties(
                                holder.type,
                                holder.source,
                                holder.flowing
                        )
                                .bucket(bucketSupplier.get())
                                .block(blockSupplier.get())
                                .explosionResistance(explosionResistance)
                                .slopeFindDistance(slopeFindDistance)
                                .levelDecreasePerBlock(levelDecreasePerBlock)
                                .tickRate(tickRate)
                )
        );

        return holder;
    }

    static void onRegisterFluidType(RegisterClientExtensionsEvent e) {
        registerFluidType(e, KRFluidTypes.SHEEP_MILK_TYPE, KRColors.SHEEP_MILK_FOG);
        registerFluidType(e, KRFluidTypes.GOAT_MILK_TYPE, KRColors.GOAT_MILK_FOG);
        registerFluidType(e, KRFluidTypes.HORSE_MILK_TYPE, KRColors.HORSE_MILK_FOG);
        registerFluidType(e, KRFluidTypes.DONKEY_MILK_TYPE, KRColors.DONKEY_MILK_FOG);
        registerFluidType(e, KRFluidTypes.CAMEL_MILK_TYPE, KRColors.CAMEL_MILK_FOG);
        registerFluidType(e, KRFluidTypes.ALPACA_MILK_TYPE, KRColors.ALPACA_MILK_FOG);
        registerFluidType(e, KRFluidTypes.RABBIT_MILK_TYPE, KRColors.RABBIT_MILK_FOG);
        registerFluidType(e, KRFluidTypes.PIG_MILK_TYPE, KRColors.PIG_MILK_FOG);
    }

    private static void registerFluidType(
            RegisterClientExtensionsEvent e,
            DeferredHolder<FluidType, FluidType> type,
            KRColors.Fog fog
    ) {
        e.registerFluidType(
                new SimpleFluidExtension(
                        ResourceLocation.withDefaultNamespace("block/water_still"),
                        ResourceLocation.withDefaultNamespace("block/water_flow"),
                        fog.color(),
                        fog.distance()
                ),
                type
        );
    }

    static void register(IEventBus eventBus) {
        KRFluidTypes.FLUID_TYPES.register(eventBus);
        FLUIDS.register(eventBus);
    }

    class FluidHolder {
        public DeferredHolder<FluidType, FluidType> type;
        public DeferredHolder<Fluid, BaseFlowingFluid.Source> source;
        public DeferredHolder<Fluid, BaseFlowingFluid.Flowing> flowing;
    }
}
