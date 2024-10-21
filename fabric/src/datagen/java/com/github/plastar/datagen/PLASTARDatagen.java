package com.github.plastar.datagen;

import com.github.plastar.data.Additives;
import com.github.plastar.data.PRegistries;
import com.github.plastar.data.Palettes;
import com.github.plastar.data.Parts;
import com.github.plastar.data.Patterns;

import org.jetbrains.annotations.Nullable;

import net.fabricmc.fabric.api.datagen.v1.DataGeneratorEntrypoint;
import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator;

import net.minecraft.core.RegistrySetBuilder;

public class PLASTARDatagen implements DataGeneratorEntrypoint {
    @Override
    public void onInitializeDataGenerator(FabricDataGenerator generator) {
        var pack = generator.createPack();
        pack.addProvider(LootTableProvider::new);
        pack.addProvider(DynamicRegistryProvider::new);
        pack.addProvider(PatternTagProvider::new);
        pack.addProvider(BlockTagsProvider::new);
        pack.addProvider(ModelProvider::new);
        pack.addProvider(AdvancementsProvider::new);
    }

    @Override
    public void buildRegistry(RegistrySetBuilder registryBuilder) {
        registryBuilder.add(PRegistries.PALETTE, Palettes::bootstrap);
        registryBuilder.add(PRegistries.PATTERN, Patterns::bootstrap);
        registryBuilder.add(PRegistries.PART, Parts::bootstrap);
        registryBuilder.add(PRegistries.ADDITIVE, Additives::bootstrap);
    }

    @Override
    public @Nullable String getEffectiveModId() {
        return "plastar";
    }
}
