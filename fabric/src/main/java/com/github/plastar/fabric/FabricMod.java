package com.github.plastar.fabric;

import com.github.plastar.PLASTARMod;
import com.github.plastar.data.PRegistries;
import com.github.plastar.entity.MechaEntity;
import com.github.plastar.entity.PEntities;
import com.github.plastar.network.PNetworking;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.registry.DynamicRegistries;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricDefaultAttributeRegistry;

import net.minecraft.network.syncher.EntityDataSerializers;

import com.kneelawk.knet.fabric.api.KNetRegistrarFabric;

public class FabricMod implements ModInitializer {
    @Override
    public void onInitialize() {
        PLASTARMod.init();
        PLASTARMod.initSync();
        PLASTARMod.REGISTRARS.registerAll();
        PEntities.registerSerializers((location, serializer) -> EntityDataSerializers.registerSerializer(serializer));
        PNetworking.register(new KNetRegistrarFabric());
        FabricDefaultAttributeRegistry.register(PEntities.MECHA_ENTITY.get(), MechaEntity.createAttributes());
        PRegistries.registerCustomDynamicRegistries(DynamicRegistries::registerSynced);
    }
}
