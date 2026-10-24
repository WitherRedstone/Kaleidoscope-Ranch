package com.chinaex123.kaleidoscope_ranch.network;

import com.chinaex123.kaleidoscope_ranch.KaleidoscopeRanch;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public record AnimalMilkSyncPayload(Map<UUID, Integer> milkMap, Map<UUID, Long> dailyMaxMap) implements CustomPacketPayload {

    public static final ResourceLocation ID = KaleidoscopeRanch.id("animal_milk_sync");
    public static final Type<AnimalMilkSyncPayload> TYPE = new Type<>(ID);

    public static final StreamCodec<FriendlyByteBuf, AnimalMilkSyncPayload> STREAM_CODEC = StreamCodec.of(
            (buf, payload) -> {
                buf.writeVarInt(payload.milkMap.size());
                for (Map.Entry<UUID, Integer> entry : payload.milkMap.entrySet()) {
                    buf.writeUUID(entry.getKey());
                    buf.writeVarInt(entry.getValue());
                    buf.writeVarLong(payload.dailyMaxMap.getOrDefault(entry.getKey(), 0L));
                }
            },
            buf -> {
                int size = buf.readVarInt();
                Map<UUID, Integer> milkMap = new HashMap<>();
                Map<UUID, Long> dailyMaxMap = new HashMap<>();
                for (int i = 0; i < size; i++) {
                    UUID uuid = buf.readUUID();
                    int milk = buf.readVarInt();
                    long dailyMax = buf.readVarLong();
                    milkMap.put(uuid, milk);
                    dailyMaxMap.put(uuid, dailyMax);
                }
                return new AnimalMilkSyncPayload(milkMap, dailyMaxMap);
            }
    );

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}