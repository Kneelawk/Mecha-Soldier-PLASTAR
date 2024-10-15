package com.github.plastar.data;

import java.util.HashMap;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;

import com.github.plastar.Constants;
import com.github.plastar.network.PatternSyncPayload;
import com.google.common.collect.ImmutableMap;
import com.google.gson.Gson;
import com.google.gson.JsonElement;

import com.mojang.serialization.Codec;
import com.mojang.serialization.Lifecycle;

import net.minecraft.core.MappedRegistry;
import net.minecraft.core.RegistrationInfo;
import net.minecraft.core.Registry;
import net.minecraft.core.WritableRegistry;
import net.minecraft.resources.RegistryOps;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.SimpleJsonResourceReloadListener;

public class PRegistries {
    private static final Gson GSON = new Gson();
    public static final ResourceKey<Registry<Additive>> ADDITIVE = ResourceKey.createRegistryKey(Constants.rl("additive"));
    public static final ResourceKey<Registry<Pattern>> PATTERN = ResourceKey.createRegistryKey(Constants.rl("pattern"));
    public static final ResourceKey<Registry<Palette>> PALETTE = ResourceKey.createRegistryKey(Constants.rl("palette"));
    public static final ResourceKey<Registry<PartDefinition>> PART = ResourceKey.createRegistryKey(Constants.rl("part"));
    
    public static List<CompletableFuture<WritableRegistry<?>>> getModRegistryFutures(RegistryOps<JsonElement> registryOps,
                                                                              ResourceManager resourceManager,
                                                                              Executor backgroundExecutor) {
        var additiveFuture = buildRegistryFuture(registryOps, resourceManager, backgroundExecutor, ADDITIVE, Additive.CODEC);
        var patternFuture = buildRegistryFuture(registryOps, resourceManager, backgroundExecutor, PATTERN, Pattern.CODEC);
        var paletteFuture = buildRegistryFuture(registryOps, resourceManager, backgroundExecutor, PALETTE, Palette.CODEC);
        var partFuture = buildRegistryFuture(registryOps, resourceManager, backgroundExecutor, PART, PartDefinition.CODEC, patternFuture);
        return List.of(additiveFuture, patternFuture, paletteFuture, partFuture);
    }
    
    public static void syncData(ServerPlayer player) {
        var registries = player.server.reloadableRegistries().get();
        PatternSyncPayload.CHANNEL.send(player, new PatternSyncPayload(
            ImmutableMap.copyOf(registries.registryOrThrow(PATTERN).entrySet()),
            ImmutableMap.copyOf(registries.registryOrThrow(PALETTE).entrySet())
        )); 
    }
    
    private static <T> CompletableFuture<WritableRegistry<?>> buildRegistryFuture(RegistryOps<JsonElement> registryOps,
                                                                                  ResourceManager resourceManager,
                                                                                  Executor backgroundExecutor,
                                                                                  ResourceKey<Registry<T>> registryKey,
                                                                                  Codec<T> codec,
                                                                                  CompletableFuture<?>... dependencies) {
        return CompletableFuture.allOf(dependencies).thenApplyAsync(__ -> {
            var writableRegistry = new MappedRegistry<>(registryKey, Lifecycle.stable());
            var files = new HashMap<ResourceLocation, JsonElement>();
            var path = Constants.MOD_ID + "/" + registryKey.location().getPath();
            SimpleJsonResourceReloadListener.scanDirectory(resourceManager, path, GSON, files);
            
            files.forEach(
                (resourceLocation, jsonElement) -> codec.parse(registryOps, jsonElement)
                    .ifSuccess(object -> writableRegistry.register(
                        ResourceKey.create(registryKey, resourceLocation),
                        object,
                        RegistrationInfo.BUILT_IN))
            );
            return writableRegistry;
        }, backgroundExecutor);
    }
}
