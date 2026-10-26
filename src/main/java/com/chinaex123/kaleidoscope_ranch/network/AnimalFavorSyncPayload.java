package com.chinaex123.kaleidoscope_ranch.network;

import com.chinaex123.kaleidoscope_ranch.KaleidoscopeRanch;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public record AnimalFavorSyncPayload(Map<UUID, Integer> animalFavorLevelMap, Map<UUID, Integer> animalFavorExpMap, Map<UUID, Integer> breedCountMap) implements CustomPacketPayload {

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
                Map<UUID, Integer> levelMap = payload.animalFavorLevelMap();
                Map<UUID, Integer> expMap = payload.animalFavorExpMap();
                Map<UUID, Integer> breedMap = payload.breedCountMap();
                buf.writeVarInt(levelMap.size());
                levelMap.forEach((uuid, level) -> {
                    UUID_STREAM_CODEC.encode(buf, uuid);
                    buf.writeVarInt(level);
                    buf.writeVarInt(expMap.getOrDefault(uuid, 0));
                    buf.writeVarInt(breedMap.getOrDefault(uuid, 0));
                });
            },
            buf -> {
                int size = buf.readVarInt();
                Map<UUID, Integer> levelMap = new HashMap<>(size);
                Map<UUID, Integer> expMap = new HashMap<>(size);
                Map<UUID, Integer> breedMap = new HashMap<>(size);
                for (int i = 0; i < size; i++) {
                    UUID uuid = UUID_STREAM_CODEC.decode(buf);
                    int level = buf.readVarInt();
                    int exp = buf.readVarInt();
                    int breedCount = buf.readVarInt();
                    levelMap.put(uuid, level);
                    expMap.put(uuid, exp);
                    breedMap.put(uuid, breedCount);
                }
                return new AnimalFavorSyncPayload(levelMap, expMap, breedMap);
            }
    );

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}