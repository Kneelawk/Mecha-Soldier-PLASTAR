package com.github.plastar.registry;

import java.util.Map;
import java.util.function.Supplier;

import it.unimi.dsi.fastutil.objects.Object2ObjectLinkedOpenHashMap;

import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;

import org.jetbrains.annotations.Nullable;

import static com.github.plastar.Constants.rl;

public class Registrar<T> {
    private final ResourceKey<T> key;
    private final Map<ResourceLocation, Supplier<T>> stuff = new Object2ObjectLinkedOpenHashMap<>();

    public Registrar(ResourceKey<T> key) {this.key = key;}

    public Supplier<T> register(String path, Supplier<T> ctor) {
        ResourceLocation name = rl(path);
        if (stuff.containsKey(name)) throw new IllegalArgumentException("Tried to register " + name + " twice!");

        Supplier<T> memoized = new Supplier<>() {
            @Nullable
            private T value;

            @Override
            public T get() {
                T value = this.value;
                if (value == null) {
                    this.value = value = ctor.get();
                }
                return value;
            }
        };

        stuff.put(name, memoized);

        return memoized;
    }

    public void registerAll(Registry<? super T> registry) {
        if (key != registry.key()) return;

        for (var entry : stuff.entrySet()) {
            Registry.register(registry, entry.getKey(), entry.getValue().get());
        }
    }
}
