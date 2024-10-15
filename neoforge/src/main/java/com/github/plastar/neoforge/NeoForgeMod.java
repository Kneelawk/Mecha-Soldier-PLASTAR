package com.github.plastar.neoforge;

import com.github.plastar.Constants;
import com.github.plastar.PLASTARMod;
import com.github.plastar.data.PRegistries;
import com.github.plastar.entity.MechaEntity;
import com.github.plastar.entity.PEntities;
import com.github.plastar.network.PNetworking;

import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.neoforge.event.entity.EntityAttributeCreationEvent;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import net.neoforged.neoforge.registries.DataPackRegistryEvent;
import net.neoforged.neoforge.registries.NeoForgeRegistries;
import net.neoforged.neoforge.registries.RegisterEvent;

import com.mojang.serialization.Codec;

import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;

import com.kneelawk.knet.neoforge.api.KNetRegistrarNeoForge;

@Mod(Constants.MOD_ID)
public class NeoForgeMod {
    public NeoForgeMod(IEventBus modBus) {
        PLASTARMod.init();

        modBus.addListener(this::onInit);
        modBus.addListener(this::onRegister);
        
        modBus.addListener((RegisterPayloadHandlersEvent event) -> PNetworking.register(new KNetRegistrarNeoForge(event.registrar("1"))));
        modBus.addListener((EntityAttributeCreationEvent event) -> event.put(PEntities.MECHA_ENTITY.get(), MechaEntity.createAttributes().build()));
        modBus.addListener((DataPackRegistryEvent.NewRegistry event) -> PRegistries.registerCustomDynamicRegistries(
            // Generic method can't be lambda :(
            new PRegistries.RegistryConsumer() {
                @Override
                public <T> void accept(ResourceKey<Registry<T>> key, Codec<T> codec) {
                    event.dataPackRegistry(key, codec, codec);
                }
            }));
    }

    private void onInit(FMLCommonSetupEvent event) {
        event.enqueueWork(PLASTARMod::initSync);
    }

    private void onRegister(RegisterEvent event) {
        PLASTARMod.REGISTRARS.register(event.getRegistry());
        if (event.getRegistry() == NeoForgeRegistries.ENTITY_DATA_SERIALIZERS) {
            PEntities.registerSerializers((location, serializer) -> Registry.register(NeoForgeRegistries.ENTITY_DATA_SERIALIZERS, location, serializer));
        }
    }
}
