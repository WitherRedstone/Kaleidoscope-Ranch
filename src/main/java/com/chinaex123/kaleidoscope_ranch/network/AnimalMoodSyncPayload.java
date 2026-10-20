package com.chinaex123.kaleidoscope_ranch.network;

import com.chinaex123.kaleidoscope_ranch.KaleidoscopeRanch;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public record AnimalMoodSyncPayload(Map<UUID, Integer> animalMoodMap, Map<UUID, Boolean> petTodayMap) implements CustomPacketPayload {

    public static final CustomPacketPayload.Type<AnimalMoodSyncPayload> TYPE =
            new CustomPacketPayload.Type<>(ResourceLocation.fromNamespaceAndPath(KaleidoscopeRanch.MODID, "animal_mood_sync"));

    private static final StreamCodec<FriendlyByteBuf, UUID> UUID_STREAM_CODEC = StreamCodec.of(
            (buf, uuid) -> {
                buf.writeLong(uuid.getMostSignificantBits());
                buf.writeLong(uuid.getLeastSignificantBits());
            },
            buf -> new UUID(buf.readLong(), buf.readLong())
    );

    public static final StreamCodec<FriendlyByteBuf, AnimalMoodSyncPayload> STREAM_CODEC = StreamCodec.of(
            (buf, payload) -> {
                Map<UUID, Integer> moodMap = payload.animalMoodMap();
                buf.writeVarInt(moodMap.size());
                moodMap.forEach((uuid, mood) -> {
                    UUID_STREAM_CODEC.encode(buf, uuid);
                    buf.writeVarInt(mood);
                });
                Map<UUID, Boolean> petMap = payload.petTodayMap();
                buf.writeVarInt(petMap.size());
                petMap.forEach((uuid, petted) -> {
                    UUID_STREAM_CODEC.encode(buf, uuid);
                    buf.writeBoolean(petted);
                });
            },
            buf -> {
                int moodSize = buf.readVarInt();
                Map<UUID, Integer> moodMap = new HashMap<>(moodSize);
                for (int i = 0; i < moodSize; i++) {
                    UUID uuid = UUID_STREAM_CODEC.decode(buf);
                    int mood = buf.readVarInt();
                    moodMap.put(uuid, mood);
                }
                int petSize = buf.readVarInt();
                Map<UUID, Boolean> petMap = new HashMap<>(petSize);
                for (int i = 0; i < petSize; i++) {
                    UUID uuid = UUID_STREAM_CODEC.decode(buf);
                    boolean petted = buf.readBoolean();
                    petMap.put(uuid, petted);
                }
                return new AnimalMoodSyncPayload(moodMap, petMap);
            }
    );

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}