package com.chinaex123.kaleidoscope_ranch.compat.jade;

import com.chinaex123.kaleidoscope_ranch.KaleidoscopeRanch;
import com.chinaex123.kaleidoscope_ranch.compat.jade.animal.AnimalMoodComponentProvider;
import com.chinaex123.kaleidoscope_ranch.compat.jade.block.FeedingTroughComponentProvider;
import com.chinaex123.kaleidoscope_ranch.block.FeedingTroughBlock;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.animal.Animal;
import snownee.jade.api.IWailaClientRegistration;
import snownee.jade.api.IWailaCommonRegistration;
import snownee.jade.api.IWailaPlugin;
import snownee.jade.api.WailaPlugin;

@WailaPlugin
public class KRJadePlugin implements IWailaPlugin {
    public static final ResourceLocation ANIMAL_MOOD = KaleidoscopeRanch.id("animal_mood");
    public static final ResourceLocation FEEDING_TROUGH = KaleidoscopeRanch.id("feeding_trough");

    @Override
    public void registerClient(IWailaClientRegistration registration) {
        registration.registerEntityComponent(AnimalMoodComponentProvider.INSTANCE, Animal.class);
        registration.registerBlockComponent(FeedingTroughComponentProvider.INSTANCE, FeedingTroughBlock.class);
    }
}