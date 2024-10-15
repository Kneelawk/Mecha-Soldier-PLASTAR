package com.github.plastar.mixin;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;

import com.github.plastar.data.PRegistries;
import com.google.gson.JsonElement;
import com.llamalad7.mixinextras.sugar.Local;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

import net.minecraft.core.WritableRegistry;
import net.minecraft.resources.RegistryOps;
import net.minecraft.server.ReloadableServerRegistries;
import net.minecraft.server.packs.resources.ResourceManager;

@Mixin(ReloadableServerRegistries.class)
public class ReloadableServerRegistriesMixin {
    @ModifyArg(method = "reload", at = @At(value = "INVOKE",
        target = "Lnet/minecraft/Util;sequence(Ljava/util/List;)Ljava/util/concurrent/CompletableFuture;"))
    private static List<CompletableFuture<WritableRegistry<?>>> plastar$injectCustomReloadableRegistries(
        List<CompletableFuture<WritableRegistry<?>>> futures,
        @Local RegistryOps<JsonElement> registryOps,
        @Local(argsOnly = true) ResourceManager resourceManager,
        @Local(argsOnly = true) Executor backgroundExecutor) {

        var newFutures = new ArrayList<>(futures);
        newFutures.addAll(PRegistries.getModRegistryFutures(registryOps, resourceManager, backgroundExecutor));
        return newFutures;
    }

}
