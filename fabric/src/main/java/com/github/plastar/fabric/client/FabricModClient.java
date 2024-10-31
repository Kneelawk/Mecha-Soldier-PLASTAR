package com.github.plastar.fabric.client;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;

import com.github.plastar.Constants;
import com.github.plastar.client.MechaItemRenderer;
import com.github.plastar.client.MechaPartItemRenderer;
import com.github.plastar.client.PLASTARClient;
import com.github.plastar.client.model.MechaModelManager;
import com.github.plastar.client.screen.PMenuScreens;
import com.github.plastar.entity.PEntities;
import com.github.plastar.item.PItems;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.rendering.v1.BuiltinItemRendererRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;
import net.fabricmc.fabric.api.resource.IdentifiableResourceReloadListener;
import net.fabricmc.fabric.api.resource.ResourceManagerHelper;

import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraft.client.renderer.entity.NoopRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.PackType;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.util.profiling.ProfilerFiller;

public class FabricModClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        PLASTARClient.init();
        EntityRendererRegistry.register(PEntities.MECHA_ENTITY.get(), NoopRenderer::new);
        BuiltinItemRendererRegistry.INSTANCE.register(PItems.MECHA.get(), new MechaItemRenderer()::renderByItem);
        BuiltinItemRendererRegistry.INSTANCE.register(PItems.MECHA_PART.get(), new MechaPartItemRenderer()::renderByItem);
        PMenuScreens.register(MenuScreens::register);
        
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
