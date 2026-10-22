package com.chinaex123.kaleidoscope_ranch.network;

import com.chinaex123.kaleidoscope_ranch.KaleidoscopeRanch;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public record AnimalMoodSyncPayload(Map<UUID, Double> animalMoodMap, Map<UUID, Boolean> petTodayMap, Map<UUID, Boolean> wildMap) implements CustomPacketPayload {

    public static final CustomPacketPayload.Type<AnimalMoodSyncPayload> TYPE =
            new CustomPacketPayload.Type<>(KaleidoscopeRanch.id("animal_mood_sync"));

    private static final StreamCodec<FriendlyByteBuf, UUID> UUID_STREAM_CODEC = StreamCodec.of(
            (buf, uuid) -> {
                buf.writeLong(uuid.getMostSignificantBits());
                buf.writeLong(uuid.getLeastSignificantBits());
            },
            buf -> new UUID(buf.readLong(), buf.readLong())
    );

    public static final StreamCodec<FriendlyByteBuf, AnimalMoodSyncPayload> STREAM_CODEC = StreamCodec.of(
            (buf, payload) -> {
                Map<UUID, Double> moodMap = payload.animalMoodMap();
                buf.writeVarInt(moodMap.size());
                moodMap.forEach((uuid, mood) -> {
                    UUID_STREAM_CODEC.encode(buf, uuid);
                    buf.writeDouble(mood);
                });
                Map<UUID, Boolean> petMap = payload.petTodayMap();
                buf.writeVarInt(petMap.size());
                petMap.forEach((uuid, petted) -> {
                    UUID_STREAM_CODEC.encode(buf, uuid);
                    buf.writeBoolean(petted);
                });
                Map<UUID, Boolean> wildMap = payload.wildMap();
                buf.writeVarInt(wildMap.size());
                wildMap.forEach((uuid, wild) -> {
                    UUID_STREAM_CODEC.encode(buf, uuid);
                    buf.writeBoolean(wild);
                });
            },
            buf -> {
                int moodSize = buf.readVarInt();
                Map<UUID, Double> moodMap = new HashMap<>(moodSize);
                for (int i = 0; i < moodSize; i++) {
                    UUID uuid = UUID_STREAM_CODEC.decode(buf);
                    double mood = buf.readDouble();
                    moodMap.put(uuid, mood);
                }
                int petSize = buf.readVarInt();
                Map<UUID, Boolean> petMap = new HashMap<>(petSize);
                for (int i = 0; i < petSize; i++) {
                    UUID uuid = UUID_STREAM_CODEC.decode(buf);
                    boolean petted = buf.readBoolean();
                    petMap.put(uuid, petted);
                }
                int wildSize = buf.readVarInt();
                Map<UUID, Boolean> wildMap = new HashMap<>(wildSize);
                for (int i = 0; i < wildSize; i++) {
                    UUID uuid = UUID_STREAM_CODEC.decode(buf);
                    boolean wild = buf.readBoolean();
                    wildMap.put(uuid, wild);
                }
                return new AnimalMoodSyncPayload(moodMap, petMap, wildMap);
            }
    );

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}