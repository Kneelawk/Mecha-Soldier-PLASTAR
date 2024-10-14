package com.github.plastar.mixin;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;

import com.github.plastar.Constants;
import com.github.plastar.data.Additive;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.llamalad7.mixinextras.sugar.Local;

import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

import com.mojang.serialization.Lifecycle;

import net.minecraft.core.MappedRegistry;
import net.minecraft.core.RegistrationInfo;
import net.minecraft.core.WritableRegistry;
import net.minecraft.resources.RegistryOps;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.ReloadableServerRegistries;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.SimpleJsonResourceReloadListener;

@Mixin(ReloadableServerRegistries.class)
public class ReloadableServerRegistriesMixin {
    @Shadow
    @Final
    private static Gson GSON;

    @ModifyArg(method = "reload", at = @At(value = "INVOKE",
        target = "Lnet/minecraft/Util;sequence(Ljava/util/List;)Ljava/util/concurrent/CompletableFuture;"))
    private static List<CompletableFuture<WritableRegistry<?>>> plastar$injectCustomReloadableRegistries(
        List<CompletableFuture<WritableRegistry<?>>> futures,
        @Local RegistryOps<JsonElement> registryOps,
        @Local(argsOnly = true) ResourceManager resourceManager,
        @Local(argsOnly = true) Executor backgroundExecutor) {

        var newFutures = new ArrayList<>(futures);
        newFutures.add(CompletableFuture.supplyAsync(() -> {
            var writableRegistry = new MappedRegistry<>(Additive.REGISTRY_KEY, Lifecycle.experimental());
            var files = new HashMap<ResourceLocation, JsonElement>();
            var path = Constants.MOD_ID + "/" + Additive.REGISTRY_KEY.location().getPath();
            SimpleJsonResourceReloadListener.scanDirectory(resourceManager, path, GSON, files);
            files.forEach(
                (resourceLocation, jsonElement) -> Additive.CODEC.parse(registryOps, jsonElement)
                    .ifSuccess(additive -> writableRegistry.register(ResourceKey.create(Additive.REGISTRY_KEY, resourceLocation),
                        additive,
                        RegistrationInfo.BUILT_IN))
            );
            return writableRegistry;
        }, backgroundExecutor));
        return newFutures;
    }
}
