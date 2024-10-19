package com.github.plastar.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import dev.engine_room.flywheel.api.material.LightShader;
import dev.engine_room.flywheel.api.material.Material;
import dev.engine_room.flywheel.lib.material.SimpleMaterial;

// Workaround for https://github.com/Engine-Room/Flywheel/issues/266
@Mixin(SimpleMaterial.Builder.class)
public class SimpleMaterialBuilderMixin {
    @Shadow(remap = false)
    protected LightShader light;

    @Inject(method = "copyFrom", at = @At("TAIL"), remap = false)
    private void plastar$correctlyCopyLightShader(Material material, CallbackInfoReturnable<SimpleMaterial.Builder> cir) {
        light = material.light();
    }
}
