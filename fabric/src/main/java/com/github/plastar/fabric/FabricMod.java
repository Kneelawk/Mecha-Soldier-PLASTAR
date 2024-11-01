package com.github.plastar.fabric;

import com.github.plastar.Constants;
import com.github.plastar.PLASTARMod;
import com.github.plastar.data.PRegistries;
import com.github.plastar.entity.MechaEntity;
import com.github.plastar.entity.PEntities;
import com.github.plastar.network.PNetworking;

import com.github.plastar.world.feature.PPlacedFeatures;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.biome.v1.BiomeModifications;
import net.fabricmc.fabric.api.biome.v1.BiomeSelectors;
import net.fabricmc.fabric.api.event.registry.DynamicRegistries;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricDefaultAttributeRegistry;

import net.fabricmc.fabric.api.tag.convention.v2.ConventionalBiomeTags;

import net.minecraft.core.registries.Registries;
import net.minecraft.network.syncher.EntityDataSerializers;

import com.kneelawk.knet.fabric.api.KNetRegistrarFabric;

import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.levelgen.GenerationStep;

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
        BiomeModifications.addFeature(BiomeSelectors.tag(ConventionalBiomeTags.IS_SAVANNA_TREE), GenerationStep.Decoration.VEGETAL_DECORATION, PPlacedFeatures.STORAX_TREES);
    }
}
