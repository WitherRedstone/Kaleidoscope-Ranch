package com.chinaex123.kaleidoscope_ranch.compat.jade.block;

import com.chinaex123.kaleidoscope_ranch.blockentity.FeedingTroughBlockEntity;
import com.chinaex123.kaleidoscope_ranch.compat.jade.KRJadePlugin;
import com.chinaex123.kaleidoscope_ranch.init.KRItems;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;
import snownee.jade.api.BlockAccessor;
import snownee.jade.api.IBlockComponentProvider;
import snownee.jade.api.ITooltip;
import snownee.jade.api.config.IPluginConfig;

public enum FeedingTroughComponentProvider implements IBlockComponentProvider {
    INSTANCE;

    @Override
    public void appendTooltip(ITooltip tooltip, BlockAccessor accessor, IPluginConfig pluginConfig) {
        BlockEntity be = accessor.getBlockEntity();
        if (!(be instanceof FeedingTroughBlockEntity trough)) return;

        int feedCount = trough.getFeedCount();
        if (feedCount == 0) {
            tooltip.add(Component.translatable("jade.plugin_kaleidoscope_ranch.feeding_trough_empty")
                    .withStyle(style -> style.withColor(0xAAAAAA)));
            return;
        }

        int livestockCount = 0;
        int poultryCount = 0;
        for (int i = 0; i < FeedingTroughBlockEntity.SLOT_COUNT; i++) {
            ItemStack stack = trough.getItem(i);
            if (stack.isEmpty()) continue;
            if (stack.is(KRItems.LIVESTOCK_FEED.get())) {
                livestockCount += stack.getCount();
            } else if (stack.is(KRItems.POULTRY_FEED.get())) {
                poultryCount += stack.getCount();
            }
        }

        if (livestockCount > 0) {
            tooltip.add(Component.translatable("jade.plugin_kaleidoscope_ranch.feeding_trough_livestock", livestockCount)
                    .withStyle(style -> style.withColor(0x55FF55)));
        }
        if (poultryCount > 0) {
            tooltip.add(Component.translatable("jade.plugin_kaleidoscope_ranch.feeding_trough_poultry", poultryCount)
                    .withStyle(style -> style.withColor(0x55FFFF)));
        }
    }

    @Override
    public ResourceLocation getUid() {
        return KRJadePlugin.FEEDING_TROUGH;
    }
}