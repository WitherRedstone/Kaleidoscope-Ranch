package com.chinaex123.kaleidoscope_ranch.init;

import com.chinaex123.kaleidoscope_ranch.KaleidoscopeRanch;
import net.minecraft.sounds.SoundEvents;
import net.neoforged.neoforge.common.SoundActions;
import net.neoforged.neoforge.fluids.FluidType;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.NeoForgeRegistries;

public interface KRFluidTypes {
    DeferredRegister<FluidType> FLUID_TYPES = DeferredRegister.create(NeoForgeRegistries.FLUID_TYPES, KaleidoscopeRanch.MODID);

    /** 羊奶 */
    DeferredHolder<FluidType, FluidType> SHEEP_MILK_TYPE = registerElement("sheep_milk", 0, 0, 1, 300, 10, 0.05, false, false, false, false, true, true, true);
    /** 山羊奶 */
    DeferredHolder<FluidType, FluidType> GOAT_MILK_TYPE = registerElement("goat_milk", 0, 0, 1, 300, 10, 0.05, false, false, false, false, true, true, true);
    /** 马奶 */
    DeferredHolder<FluidType, FluidType> HORSE_MILK_TYPE = registerElement("horse_milk", 0, 0, 1, 300, 10, 0.05, false, false, false, false, true, true, true);
    /** 驴奶 */
    DeferredHolder<FluidType, FluidType> DONKEY_MILK_TYPE = registerElement("donkey_milk", 0, 0, 1, 300, 10, 0.05, false, false, false, false, true, true, true);
    /** 骆驼奶 */
    DeferredHolder<FluidType, FluidType> CAMEL_MILK_TYPE = registerElement("camel_milk", 0, 0, 1, 300, 10, 0.05, false, false, false, false, true, true, true);
    /** 羊驼奶 */
    DeferredHolder<FluidType, FluidType> ALPACA_MILK_TYPE = registerElement("alpaca_milk", 0, 0, 1, 300, 10, 0.05, false, false, false, false, true, true, true);
    /** 兔奶 */
    DeferredHolder<FluidType, FluidType> RABBIT_MILK_TYPE = registerElement("rabbit_milk", 0, 0, 1, 300, 10, 0.05, false, false, false, false, true, true, true);
    /** 猪奶 */
    DeferredHolder<FluidType, FluidType> PIG_MILK_TYPE = registerElement("pig_milk", 0, 0, 1, 300, 10, 0.05, false, false, false, false, true, true, true);

    /**
     * @param name            流体名称
     * @param fallDistance    摔落距离修正系数。影响实体落入流体时受到的摔落伤害减免
     * @param lightLevel      发光等级
     * @param density         密度，影响浮沉
     * @param temperature     温度，影响表现与伤害
     * @param viscosity       粘稠度。数值越大流动越慢
     * @param motionScale     实体在流体中的运动速度缩放。数值越小越慢
     * @param canHydrate      是否能湿润耕地
     * @param convertToSource 是否能形成无限源
     * @param canExtinguish   是否能扑灭实体身上的火
     * @param supportsBoating 是否能漂浮在流体中
     * @param canPushEntity   流体是否能推动实体
     * @param canDrown        实体是否会在其中溺水
     * @param canSwim         实体是否能在其中游泳
     *
     * @return 用于延迟获取注册结果。
     */
    private static DeferredHolder<FluidType, FluidType> registerElement(
            String name,
            float fallDistance,
            int lightLevel,
            int density,
            int temperature,
            int viscosity,
            double motionScale,
            boolean canHydrate,
            boolean convertToSource,
            boolean supportsBoating,
            boolean canExtinguish,
            boolean canPushEntity,
            boolean canDrown,
            boolean canSwim
    ) {
        return FLUID_TYPES.register(name, () -> new FluidType(FluidType.Properties.create()
                .descriptionId("block." + KaleidoscopeRanch.MODID + "." + name)
                .fallDistanceModifier(fallDistance)
                .lightLevel(lightLevel)
                .density(density)
                .temperature(temperature)
                .viscosity(viscosity)
                .canHydrate(canHydrate)
                .canConvertToSource(convertToSource)
                .canSwim(canSwim)
                .canDrown(canDrown)
                .canPushEntity(canPushEntity)
                .canExtinguish(canExtinguish)
                .supportsBoating(supportsBoating)
                .motionScale(motionScale)
                .sound(SoundActions.BUCKET_FILL, SoundEvents.BUCKET_FILL)
                .sound(SoundActions.BUCKET_EMPTY, SoundEvents.BUCKET_EMPTY)
        ));
    }
}
