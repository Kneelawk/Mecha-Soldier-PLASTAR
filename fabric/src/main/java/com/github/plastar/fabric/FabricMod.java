package com.github.plastar.fabric;

import com.github.plastar.PLASTARMod;

import com.github.plastar.data.PRegistries;
import com.github.plastar.entity.MechaEntity;
import com.github.plastar.entity.PEntities;

import com.github.plastar.network.PNetworking;

import com.kneelawk.knet.fabric.api.KNetRegistrarFabric;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricDefaultAttributeRegistry;

import net.minecraft.network.syncher.EntityDataSerializers;

public class FabricMod implements ModInitializer {
    @Override
    public void onInitialize() {
        PLASTARMod.init();
        PLASTARMod.initSync();
        PLASTARMod.REGISTRARS.registerAll();
        PEntities.registerSerializers((location, serializer) -> EntityDataSerializers.registerSerializer(serializer));
        PNetworking.register(new KNetRegistrarFabric());
        ServerLifecycleEvents.SYNC_DATA_PACK_CONTENTS.register((player, joined) -> PRegistries.syncData(player));

        FabricDefaultAttributeRegistry.register(PEntities.MECHA_ENTITY.get(), MechaEntity.createAttributes());
    }
}
