package com.github.plastar.data;

import com.github.plastar.Constants;

import com.mojang.serialization.Codec;

import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;

public class PRegistries {
    public static final ResourceKey<Registry<Additive>> ADDITIVE = ResourceKey.createRegistryKey(Constants.rl("additive"));
    public static final ResourceKey<Registry<Pattern>> PATTERN = ResourceKey.createRegistryKey(Constants.rl("pattern"));
    public static final ResourceKey<Registry<Palette>> PALETTE = ResourceKey.createRegistryKey(Constants.rl("palette"));
    public static final ResourceKey<Registry<PartDefinition>> PART = ResourceKey.createRegistryKey(Constants.rl("part"));
    
    public static void registerCustomDynamicRegistries(RegistryConsumer consumer) {
        consumer.accept(ADDITIVE, Additive.CODEC);
        consumer.accept(PATTERN, Pattern.CODEC);
        consumer.accept(PALETTE, Palette.CODEC);
        consumer.accept(PART, PartDefinition.CODEC);
    }
    
    @FunctionalInterface
    public interface RegistryConsumer {
        <T> void accept(ResourceKey<Registry<T>> key, Codec<T> codec);
    }
}
