package com.github.plastar.registry;

import java.util.Map;

import com.github.plastar.Log;

import it.unimi.dsi.fastutil.objects.Object2ObjectLinkedOpenHashMap;

import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceKey;

public class RegistrarSet {
    private final Map<ResourceKey<? extends Registry<?>>, Registrar<?>> registrars =
        new Object2ObjectLinkedOpenHashMap<>();

    @SuppressWarnings("unchecked")
    public <T> Registrar<T> get(ResourceKey<? extends Registry<? super T>> key) {
        return (Registrar<T>) registrars.computeIfAbsent(key, Registrar::new);
    }

    @SuppressWarnings("unchecked")
    public void register(Registry<?> registry) {
        Registrar<?> registrar = registrars.get(registry.key());
        if (registrar == null) return;

        registrar.registerAll((Registry<? super Object>) registry);
    }

    @SuppressWarnings("unchecked")
    public void registerAll() {
        for (var entry : registrars.entrySet()) {
            Registry<Object> registry = ((Registry<Registry<Object>>) BuiltInRegistries.REGISTRY).get(
                (ResourceKey<Registry<Object>>) entry.getKey());
            if (registry == null) {
                Log.LOG.error("Attempted to register items for {} but there is no registry with that key",
                    entry.getKey());
                continue;
            }
            entry.getValue().registerAll(registry);
        }
    }
}
