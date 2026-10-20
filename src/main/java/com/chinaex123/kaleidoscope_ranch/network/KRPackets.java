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
    }

    private static void handleAnimalMoodSyncOnClient(AnimalMoodSyncPayload payload, IPayloadContext context) {
        context.enqueueWork(() -> {
            ClientAnimalMoodData.getInstance().updateMoodData(payload.animalMoodMap(), payload.petTodayMap());
        });
    }

    private static void handleAnimalHungerSyncOnClient(AnimalHungerSyncPayload payload, IPayloadContext context) {
        context.enqueueWork(() -> {
            ClientAnimalMoodData.getInstance().updateHungerData(payload.animalHungerMap(), payload.feedCooldownMap());
        });
    }
}