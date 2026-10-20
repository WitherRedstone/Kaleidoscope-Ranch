package com.chinaex123.kaleidoscope_ranch.network;

import com.chinaex123.kaleidoscope_ranch.KaleidoscopeRanch;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public record AnimalHungerSyncPayload(
        Map<UUID, Float> animalHungerMap,
        Map<UUID, Long> feedCooldownExpireMap
) implements CustomPacketPayload {

    public static final CustomPacketPayload.Type<AnimalHungerSyncPayload> TYPE =
            new CustomPacketPayload.Type<>(ResourceLocation.fromNamespaceAndPath(KaleidoscopeRanch.MODID, "animal_hunger_sync"));

    private static final StreamCodec<FriendlyByteBuf, UUID> UUID_STREAM_CODEC = StreamCodec.of(
            (buf, uuid) -> {
                buf.writeLong(uuid.getMostSignificantBits());
                buf.writeLong(uuid.getLeastSignificantBits());
            },
            buf -> new UUID(buf.readLong(), buf.readLong())
    );

    public static final StreamCodec<FriendlyByteBuf, AnimalHungerSyncPayload> STREAM_CODEC = StreamCodec.of(
            (buf, payload) -> {
                Map<UUID, Float> hungerMap = payload.animalHungerMap();
                buf.writeVarInt(hungerMap.size());
                hungerMap.forEach((uuid, hunger) -> {
                    UUID_STREAM_CODEC.encode(buf, uuid);
                    buf.writeFloat(hunger);
                });
                Map<UUID, Long> expireMap = payload.feedCooldownExpireMap();
                buf.writeVarInt(expireMap.size());
                expireMap.forEach((uuid, expire) -> {
                    UUID_STREAM_CODEC.encode(buf, uuid);
                    buf.writeVarLong(expire);
                });
            },
            buf -> {
                int size = buf.readVarInt();
                Map<UUID, Float> hungerMap = new HashMap<>(size);
                for (int i = 0; i < size; i++) {
                    UUID uuid = UUID_STREAM_CODEC.decode(buf);
                    float hunger = buf.readFloat();
                    hungerMap.put(uuid, hunger);
                }
                int expireSize = buf.readVarInt();
                Map<UUID, Long> expireMap = new HashMap<>(expireSize);
                for (int i = 0; i < expireSize; i++) {
                    UUID uuid = UUID_STREAM_CODEC.decode(buf);
                    long expire = buf.readVarLong();
                    expireMap.put(uuid, expire);
                }
                return new AnimalHungerSyncPayload(hungerMap, expireMap);
            }
    );

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}