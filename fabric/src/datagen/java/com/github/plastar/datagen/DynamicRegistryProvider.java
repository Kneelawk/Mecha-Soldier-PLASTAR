package com.github.plastar.datagen;

import java.util.concurrent.CompletableFuture;

import com.github.plastar.data.PRegistries;

import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricDynamicRegistryProvider;

import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;

public class DynamicRegistryProvider extends FabricDynamicRegistryProvider {
    public DynamicRegistryProvider(FabricDataOutput output, CompletableFuture<HolderLookup.Provider> registriesFuture) {
        super(output, registriesFuture);
    }

    @Override
    protected void configure(HolderLookup.Provider registries, Entries entries) {
        entries.addAll(registries.lookupOrThrow(PRegistries.PALETTE));
        entries.addAll(registries.lookupOrThrow(PRegistries.PATTERN));
        entries.addAll(registries.lookupOrThrow(PRegistries.PART));
        entries.addAll(registries.lookupOrThrow(PRegistries.ADDITIVE));
        entries.addAll(registries.lookupOrThrow(Registries.CONFIGURED_FEATURE));
        entries.addAll(registries.lookupOrThrow(Registries.PLACED_FEATURE));
    }

    @Override
    public String getName() {
        return "Dynamic Registries";
    }
}
