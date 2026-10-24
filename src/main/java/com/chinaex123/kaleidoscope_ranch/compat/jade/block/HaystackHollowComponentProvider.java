package com.chinaex123.kaleidoscope_ranch.compat.jade.block;

import com.chinaex123.kaleidoscope_ranch.blockentity.HaystackHollowBlockEntity;
import com.chinaex123.kaleidoscope_ranch.compat.jade.KRJadePlugin;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;
import snownee.jade.api.BlockAccessor;
import snownee.jade.api.IBlockComponentProvider;
import snownee.jade.api.ITooltip;
import snownee.jade.api.config.IPluginConfig;

import java.util.LinkedHashMap;
import java.util.Map;

public enum HaystackHollowComponentProvider implements IBlockComponentProvider {
    INSTANCE;

    @Override
    public void appendTooltip(ITooltip tooltip, BlockAccessor accessor, IPluginConfig pluginConfig) {
        BlockEntity be = accessor.getBlockEntity();
        if (!(be instanceof HaystackHollowBlockEntity hollow)) return;

        if (!hollow.hasItems()) {
            tooltip.add(Component.translatable("jade.plugin_kaleidoscope_ranch.haystack_hollow_empty")
                    .withStyle(style -> style.withColor(0xAAAAAA)));
            return;
        }

        Map<Item, Integer> itemCounts = new LinkedHashMap<>();
        for (int i = 0; i < HaystackHollowBlockEntity.SLOT_COUNT; i++) {
            ItemStack stack = hollow.getItem(i);
            if (stack.isEmpty()) continue;
            itemCounts.merge(stack.getItem(), stack.getCount(), Integer::sum);
        }

        for (Map.Entry<Item, Integer> entry : itemCounts.entrySet()) {
            Item item = entry.getKey();
            int count = entry.getValue();
            tooltip.add(Component.literal(new ItemStack(item).getHoverName().getString() + " x" + count)
                    .withStyle(style -> style.withColor(0xFFFFFF)));
        }
    }

    @Override
    public ResourceLocation getUid() {
        return KRJadePlugin.HAYSTACK_HOLLOW;
    }
}