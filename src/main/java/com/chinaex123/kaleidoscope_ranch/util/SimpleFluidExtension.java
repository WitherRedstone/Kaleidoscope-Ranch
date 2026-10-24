package com.chinaex123.kaleidoscope_ranch.util;

import com.mojang.blaze3d.shaders.FogShape;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.Camera;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.FogRenderer;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.client.extensions.common.IClientFluidTypeExtensions;
import org.jetbrains.annotations.NotNull;
import org.joml.Vector3f;

public class SimpleFluidExtension implements IClientFluidTypeExtensions {

    private final ResourceLocation stillTexture;
    private final ResourceLocation flowingTexture;
    private final int tintColor;
    private final int fogColor;
    private final float fogDistance;
    private final boolean noFog;

    public SimpleFluidExtension(
            ResourceLocation stillTexture,
            ResourceLocation flowingTexture,
            int tintColor,
            int fogColor,
            float fogDistance,
            boolean noFog
    ) {
        this.stillTexture = stillTexture;
        this.flowingTexture = flowingTexture;
        this.tintColor = tintColor;
        this.fogColor = fogColor;
        this.fogDistance = fogDistance;
        this.noFog = noFog;
    }

    public SimpleFluidExtension(
            ResourceLocation stillTexture,
            ResourceLocation flowingTexture,
            int fogColor,
            float fogDistance
    ) {
        this(
                stillTexture,
                flowingTexture,
                0xFF000000 | (fogColor & 0xFFFFFF),
                fogColor,
                fogDistance,
                false
        );
    }

    public SimpleFluidExtension(
            ResourceLocation stillTexture,
            ResourceLocation flowingTexture
    ) {
        this(
                stillTexture,
                flowingTexture,
                0xFFFFFFFF,
                0xFFFFFF,
                96.0f,
                true
        );
    }

    @Override
    public ResourceLocation getStillTexture() {
        return this.stillTexture;
    }

    @Override
    public ResourceLocation getFlowingTexture() {
        return this.flowingTexture;
    }

    @Override
    public int getTintColor() {
        return this.tintColor;
    }

    @Override
    public @NotNull Vector3f modifyFogColor(
            Camera camera,
            float partialTick,
            ClientLevel level,
            int renderDistance,
            float darkenWorldAmount,
            Vector3f fluidFogColor
    ) {
        if (this.noFog) {
            return fluidFogColor;
        }
        float r = ((this.fogColor >> 16) & 0xFF) / 255.0F;
        float g = ((this.fogColor >> 8) & 0xFF) / 255.0F;
        float b = (this.fogColor & 0xFF) / 255.0F;
        return new Vector3f(r, g, b);
    }

    @Override
    public void modifyFogRender(
            Camera camera,
            FogRenderer.FogMode mode,
            float renderDistance,
            float partialTick,
            float nearDistance,
            float farDistance,
            FogShape shape
    ) {
        if (this.noFog) {
            return;
        }
        if (camera.getEntity().isSpectator()) {
            return;
        }
        RenderSystem.setShaderFogStart(0.0f);
        RenderSystem.setShaderFogEnd(this.fogDistance);
    }
}
