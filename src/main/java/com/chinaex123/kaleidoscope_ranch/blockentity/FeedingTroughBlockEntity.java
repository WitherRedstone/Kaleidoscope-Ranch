package com.chinaex123.kaleidoscope_ranch.blockentity;

import com.chinaex123.kaleidoscope_ranch.block.FeedingTroughBlock;
import com.chinaex123.kaleidoscope_ranch.init.KRBlockEntityTypes;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;

public class FeedingTroughBlockEntity extends BlockEntity {

    public static final int SLOT_COUNT = 9;
    public static final int MAX_FEED_COUNT = SLOT_COUNT * 64;

    private final ItemStack[] items = new ItemStack[SLOT_COUNT];

    public FeedingTroughBlockEntity(BlockPos pos, BlockState state) {
        super(KRBlockEntityTypes.FEEDING_TROUGH.get(), pos, state);
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

    public boolean hasFeed() {
        for (int i = 0; i < SLOT_COUNT; i++) {
            if (!items[i].isEmpty()) return true;
        }
        return false;
    }

    public ItemStack consumeOneFeed() {
        for (int i = 0; i < SLOT_COUNT; i++) {
            if (!items[i].isEmpty()) {
                ItemStack result = items[i].split(1);
                if (items[i].isEmpty()) {
                    items[i] = ItemStack.EMPTY;
                }
                setChanged();
                return result;
            }
        }
        return ItemStack.EMPTY;
    }

    public ItemStack consumeOneFeed(Item feedItem) {
        for (int i = 0; i < SLOT_COUNT; i++) {
            if (!items[i].isEmpty() && items[i].is(feedItem)) {
                ItemStack result = items[i].split(1);
                if (items[i].isEmpty()) {
                    items[i] = ItemStack.EMPTY;
                }
                setChanged();
                return result;
            }
        }
        return ItemStack.EMPTY;
    }

    public ItemStack getItem(int slot) {
        if (slot < 0 || slot >= SLOT_COUNT) return ItemStack.EMPTY;
        return items[slot];
    }

    public int getFeedCount() {
        int total = 0;
        for (int i = 0; i < SLOT_COUNT; i++) {
            if (!items[i].isEmpty()) {
                total += items[i].getCount();
            }
        }
        return total;
    }

    public java.util.List<ItemStack> getDrops() {
        java.util.List<ItemStack> drops = new java.util.ArrayList<>();
        for (int i = 0; i < SLOT_COUNT; i++) {
            if (!items[i].isEmpty()) {
                drops.add(items[i].copy());
            }
        }
        return drops;
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

    public int getFillLevel() {
        int count = getFeedCount();
        if (count == 0) return 0;
        if (count >= MAX_FEED_COUNT) return 6;
        return Math.min(6, (int) Math.ceil(count * 6.0 / MAX_FEED_COUNT));
    }

    @Override
    public void setChanged() {
        super.setChanged();
        if (level != null && !level.isClientSide()) {
            int newLevel = getFillLevel();
            BlockState currentState = getBlockState();
            if (currentState.getValue(FeedingTroughBlock.FILL_LEVEL) != newLevel) {
                level.setBlockAndUpdate(getBlockPos(), currentState.setValue(FeedingTroughBlock.FILL_LEVEL, newLevel));
            }
            level.sendBlockUpdated(getBlockPos(), getBlockState(), getBlockState(), 3);
        }
    }

    public static void serverTick(Level level, BlockPos pos, BlockState state, FeedingTroughBlockEntity be) {
    }
}