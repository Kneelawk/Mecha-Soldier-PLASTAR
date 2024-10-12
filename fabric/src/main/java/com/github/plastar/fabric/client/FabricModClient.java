package com.github.plastar.fabric.client;

import com.github.plastar.client.PLASTARClient;
import com.github.plastar.entity.PEntities;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;

import net.minecraft.client.renderer.entity.NoopRenderer;

public class FabricModClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        PLASTARClient.init();
        EntityRendererRegistry.register(PEntities.MECHA_ENTITY, NoopRenderer::new);
    }
}
