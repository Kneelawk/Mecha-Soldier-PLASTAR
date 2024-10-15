package com.github.plastar.neoforge.client;

import com.github.plastar.Constants;

import com.github.plastar.client.ClientPatternManager;
import com.github.plastar.client.PLASTARClient;

import com.github.plastar.client.model.MechaModelManager;
import com.github.plastar.entity.PEntities;

import net.minecraft.client.renderer.entity.NoopRenderer;

import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.neoforge.client.event.ClientPlayerNetworkEvent;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;
import net.neoforged.neoforge.client.event.RegisterClientReloadListenersEvent;
import net.neoforged.neoforge.common.NeoForge;

@Mod(value = Constants.MOD_ID, dist = Dist.CLIENT)
public class NeoForgeModClient {
    public NeoForgeModClient(IEventBus modBus) {
        modBus.addListener((FMLCommonSetupEvent event) -> event.enqueueWork(PLASTARClient::init));
        modBus.addListener((EntityRenderersEvent.RegisterRenderers event) -> event.registerEntityRenderer(PEntities.MECHA_ENTITY.get(), NoopRenderer::new));
        modBus.addListener((RegisterClientReloadListenersEvent event) -> event.registerReloadListener(MechaModelManager.INSTANCE));
        NeoForge.EVENT_BUS.addListener((ClientPlayerNetworkEvent.LoggingOut event) -> ClientPatternManager.INSTANCE.clear());
    }
}
