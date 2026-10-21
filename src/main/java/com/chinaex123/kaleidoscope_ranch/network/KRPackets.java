package com.chinaex123.kaleidoscope_ranch.network;

import com.chinaex123.kaleidoscope_ranch.KaleidoscopeRanch;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import net.neoforged.neoforge.network.handling.IPayloadContext;

public class KRPackets {

    private static final String PROTOCOL_VERSION = "1";

    @SubscribeEvent
    public static void onRegisterPayloadHandlers(RegisterPayloadHandlersEvent event) {
        var registrar = event.registrar(KaleidoscopeRanch.MODID).versioned(PROTOCOL_VERSION);
        registrar.playToClient(
                AnimalMoodSyncPayload.TYPE,
                AnimalMoodSyncPayload.STREAM_CODEC,
                KRPackets::handleAnimalMoodSyncOnClient
        );
        registrar.playToClient(
                AnimalHungerSyncPayload.TYPE,
                AnimalHungerSyncPayload.STREAM_CODEC,
                KRPackets::handleAnimalHungerSyncOnClient
        );
        registrar.playToClient(
                AnimalFavorSyncPayload.TYPE,
                AnimalFavorSyncPayload.STREAM_CODEC,
                KRPackets::handleAnimalFavorSyncOnClient
        );
    }

    private static void handleAnimalMoodSyncOnClient(AnimalMoodSyncPayload payload, IPayloadContext context) {
        context.enqueueWork(() ->
                ClientAnimalData.getInstance().updateMoodData(payload.animalMoodMap(), payload.petTodayMap(), payload.wildMap())
        );
    }

    private static void handleAnimalHungerSyncOnClient(AnimalHungerSyncPayload payload, IPayloadContext context) {
        context.enqueueWork(() ->
                ClientAnimalData.getInstance().updateHungerData(payload.animalHungerMap(), payload.feedCooldownExpireMap(), payload.troughCooldownExpireMap())
        );
    }

    private static void handleAnimalFavorSyncOnClient(AnimalFavorSyncPayload payload, IPayloadContext context) {
        context.enqueueWork(() ->
                ClientAnimalData.getInstance().updateFavorData(payload.animalFavorMap())
        );
    }
}