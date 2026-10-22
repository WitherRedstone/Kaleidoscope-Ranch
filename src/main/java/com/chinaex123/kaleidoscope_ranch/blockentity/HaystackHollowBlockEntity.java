package com.chinaex123.kaleidoscope_ranch.blockentity;

import com.chinaex123.kaleidoscope_ranch.config.KRConfig;
import com.chinaex123.kaleidoscope_ranch.init.KRBlockEntityTypes;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;

public class HaystackHollowBlockEntity extends BlockEntity {

    public static final int SLOT_COUNT = 9;

    private final ItemStack[] items = new ItemStack[SLOT_COUNT];

    public HaystackHollowBlockEntity(BlockPos pos, BlockState state) {
        super(KRBlockEntityTypes.HAYSTACK_HOLLOW.get(), pos, state);
        Arrays.fill(items, ItemStack.EMPTY);
    }

    public int insertItem(ItemStack stack) {
        if (stack.isEmpty()) return 0;
        int remaining = stack.getCount();
        int maxStack = stack.getMaxStackSize();

        for (int i = 0; i < SLOT_COUNT && remaining > 0; i++) {
            if (items[i].isEmpty()) {
                int toInsert = Math.min(remaining, maxStack);
                items[i] = stack.copy();
                items[i].setCount(toInsert);
                remaining -= toInsert;
            } else if (ItemStack.isSameItemSameComponents(items[i], stack)) {
                int canInsert = maxStack - items[i].getCount();
                int toInsert = Math.min(remaining, canInsert);
                if (toInsert > 0) {
                    items[i].grow(toInsert);
                    remaining -= toInsert;
                }
            }
        }

        int inserted = stack.getCount() - remaining;
        if (inserted > 0) {
            stack.shrink(inserted);
        }
        return inserted;
    }

    public ItemStack extractOneGroup() {
        for (int i = SLOT_COUNT - 1; i >= 0; i--) {
            if (!items[i].isEmpty()) {
                ItemStack result = items[i].copy();
                items[i] = ItemStack.EMPTY;
                return result;
            }
        }
        return ItemStack.EMPTY;
    }

    public boolean hasItems() {
        for (int i = 0; i < SLOT_COUNT; i++) {
            if (!items[i].isEmpty()) return true;
        }
        return false;
    }

    public ItemStack getItem(int slot) {
        if (slot < 0 || slot >= SLOT_COUNT) return ItemStack.EMPTY;
        return items[slot];
    }

    public int getTotalItemCount() {
        int total = 0;
        for (int i = 0; i < SLOT_COUNT; i++) {
            if (!items[i].isEmpty()) {
                total += items[i].getCount();
            }
        }
        return total;
    }

    @Override
    protected void saveAdditional(@NotNull CompoundTag tag, @NotNull HolderLookup.Provider registries) {
        super.saveAdditional(tag, registries);
        ListTag list = new ListTag();
        for (int i = 0; i < SLOT_COUNT; i++) {
            if (!items[i].isEmpty()) {
                CompoundTag itemTag = (CompoundTag) items[i].save(registries);
                itemTag.putInt("Slot", i);
                list.add(itemTag);
            }
        }
        tag.put("Items", list);
    }

    @Override
    protected void loadAdditional(@NotNull CompoundTag tag, @NotNull HolderLookup.Provider registries) {
        super.loadAdditional(tag, registries);
        Arrays.fill(items, ItemStack.EMPTY);
        ListTag list = tag.getList("Items", 10);
        for (int j = 0; j < list.size(); j++) {
            CompoundTag itemTag = list.getCompound(j);
            int slot = itemTag.getInt("Slot");
            if (slot >= 0 && slot < SLOT_COUNT) {
                items[slot] = ItemStack.parseOptional(registries, itemTag);
            }
        }
    }

    @Override
    public @NotNull CompoundTag getUpdateTag(HolderLookup.@NotNull Provider registries) {
        CompoundTag tag = new CompoundTag();
        saveAdditional(tag, registries);
        return tag;
    }

    @Override
    public Packet<ClientGamePacketListener> getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }

    @Override
    public void setChanged() {
        super.setChanged();
        if (level != null && !level.isClientSide()) {
            level.sendBlockUpdated(getBlockPos(), getBlockState(), getBlockState(), 3);
        }
    }

    public static void serverTick(Level level, BlockPos pos, BlockState state, HaystackHollowBlockEntity be) {
        if (level.getGameTime() % KRConfig.HAYSTACK_COLLECT_INTERVAL.get() != 0) return;

        int radius = KRConfig.HAYSTACK_COLLECT_RADIUS.get();
        int radiusSq = radius * radius;

        for (ItemEntity itemEntity : level.getEntitiesOfClass(ItemEntity.class,
                net.minecraft.world.phys.AABB.ofSize(net.minecraft.world.phys.Vec3.atCenterOf(pos), radius * 2, radius * 2, radius * 2))) {
            if (itemEntity.isRemoved()) continue;
            ItemStack stack = itemEntity.getItem();
            if (stack.isEmpty()) continue;

            int before = stack.getCount();
            be.insertItem(stack);
            int inserted = before - stack.getCount();

            if (inserted > 0) {
                if (stack.isEmpty()) {
                    itemEntity.discard();
                } else {
                    itemEntity.setItem(stack);
                }
                be.setChanged();
            }
        }
    }
}