package com.github.plastar.mixin;

import java.util.HashMap;
import java.util.Map;

import com.github.plastar.Constants;

import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;

import net.minecraft.client.resources.model.ModelManager;
import net.minecraft.resources.ResourceLocation;

@Mixin(ModelManager.class)
public class ModelManagerMixin {
    @Shadow
    @Final
    @Mutable
    private static Map<ResourceLocation, ResourceLocation> VANILLA_ATLASES;

    static {
        VANILLA_ATLASES = new HashMap<>(VANILLA_ATLASES);
        VANILLA_ATLASES.put(Constants.ATLAS_ID, Constants.rl("mecha_parts"));
    }
}
