package com.github.plastar.fabric.client;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;

import com.github.plastar.Constants;
import com.github.plastar.client.PLASTARClient;
import com.github.plastar.client.model.MechaModelManager;
import com.github.plastar.entity.PEntities;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;
import net.fabricmc.fabric.api.resource.IdentifiableResourceReloadListener;
import net.fabricmc.fabric.api.resource.ResourceManagerHelper;

import net.minecraft.client.renderer.entity.NoopRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.PackType;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.util.profiling.ProfilerFiller;

public class FabricModClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        PLASTARClient.init();
        EntityRendererRegistry.register(PEntities.MECHA_ENTITY, NoopRenderer::new);
        ResourceManagerHelper.get(PackType.CLIENT_RESOURCES).registerReloadListener(
            new IdentifiableResourceReloadListener() {
                @Override
                public ResourceLocation getFabricId() {
                    return Constants.rl("mecha_models");
                }

                @Override
                public CompletableFuture<Void> reload(PreparationBarrier preparationBarrier,
                                                      ResourceManager resourceManager,
                                                      ProfilerFiller preparationsProfiler,
                                                      ProfilerFiller reloadProfiler, Executor backgroundExecutor,
                                                      Executor gameExecutor) {
                    return MechaModelManager.INSTANCE.reload(preparationBarrier,
                        resourceManager,
                        preparationsProfiler,
                        reloadProfiler,
                        backgroundExecutor,
                        gameExecutor);
                }
            });
    }
}
