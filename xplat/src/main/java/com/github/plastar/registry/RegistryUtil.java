package com.github.plastar.registry;

import net.minecraft.core.Holder;
import net.minecraft.core.HolderLookup;
import net.minecraft.resources.ResourceKey;

public class RegistryUtil {
    public static <T> Holder<T> getPreferred(ResourceKey<T> preferred, HolderLookup.RegistryLookup<T> lookup) {
        return lookup.get(preferred).orElseGet(() -> lookup.listElements().findFirst().orElseThrow());
    }
}
