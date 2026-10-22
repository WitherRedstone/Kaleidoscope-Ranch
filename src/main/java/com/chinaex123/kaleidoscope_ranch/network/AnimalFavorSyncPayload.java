package com.chinaex123.kaleidoscope_ranch.network;

import com.chinaex123.kaleidoscope_ranch.KaleidoscopeRanch;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public record AnimalFavorSyncPayload(Map<UUID, Float> animalFavorMap) implements CustomPacketPayload {

    public static final CustomPacketPayload.Type<AnimalFavorSyncPayload> TYPE =
            new CustomPacketPayload.Type<>(KaleidoscopeRanch.id("animal_favor_sync"));

    private static final StreamCodec<FriendlyByteBuf, UUID> UUID_STREAM_CODEC = StreamCodec.of(
            (buf, uuid) -> {
                buf.writeLong(uuid.getMostSignificantBits());
                buf.writeLong(uuid.getLeastSignificantBits());
            },
            buf -> new UUID(buf.readLong(), buf.readLong())
    );

    public static final StreamCodec<FriendlyByteBuf, AnimalFavorSyncPayload> STREAM_CODEC = StreamCodec.of(
            (buf, payload) -> {
                Map<UUID, Float> favorMap = payload.animalFavorMap();
                buf.writeVarInt(favorMap.size());
                favorMap.forEach((uuid, favor) -> {
                    UUID_STREAM_CODEC.encode(buf, uuid);
                    buf.writeFloat(favor);
                });
            },
            buf -> {
                int size = buf.readVarInt();
                Map<UUID, Float> favorMap = new HashMap<>(size);
                for (int i = 0; i < size; i++) {
                    UUID uuid = UUID_STREAM_CODEC.decode(buf);
                    float favor = buf.readFloat();
                    favorMap.put(uuid, favor);
                }
                return new AnimalFavorSyncPayload(favorMap);
            }
    );

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}