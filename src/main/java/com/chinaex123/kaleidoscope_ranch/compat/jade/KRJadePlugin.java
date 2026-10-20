package com.chinaex123.kaleidoscope_ranch.compat.jade;

import com.chinaex123.kaleidoscope_ranch.KaleidoscopeRanch;
import com.chinaex123.kaleidoscope_ranch.compat.jade.animal.AnimalMoodComponentProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.animal.Animal;
import snownee.jade.api.IWailaClientRegistration;
import snownee.jade.api.IWailaPlugin;
import snownee.jade.api.WailaPlugin;

@WailaPlugin
public class KRJadePlugin implements IWailaPlugin {
    public static final ResourceLocation ANIMAL_MOOD = ResourceLocation.fromNamespaceAndPath(KaleidoscopeRanch.MODID, "animal_mood");

    @Override
    public void registerClient(IWailaClientRegistration registration) {
        registration.registerEntityComponent(AnimalMoodComponentProvider.INSTANCE, Animal.class);
    }
}