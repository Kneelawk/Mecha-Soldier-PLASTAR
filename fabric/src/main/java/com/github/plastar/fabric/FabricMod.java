package com.github.plastar.fabric;

import com.github.plastar.PLASTARMod;

import com.github.plastar.entity.MechaEntity;
import com.github.plastar.entity.PEntities;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricDefaultAttributeRegistry;

public class FabricMod implements ModInitializer {
    @Override
    public void onInitialize() {
        PLASTARMod.init();
        PLASTARMod.initSync();
        PLASTARMod.REGISTRARS.registerAll();

        FabricDefaultAttributeRegistry.register(PEntities.MECHA_ENTITY, MechaEntity.createAttributes());
    }
}
