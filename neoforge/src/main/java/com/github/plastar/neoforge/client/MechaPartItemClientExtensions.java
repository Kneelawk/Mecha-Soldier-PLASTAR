package com.github.plastar.neoforge.client;

import com.github.plastar.client.MechaPartItemRenderer;

import net.neoforged.neoforge.client.extensions.common.IClientItemExtensions;

import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;

public class MechaPartItemClientExtensions implements IClientItemExtensions {
    private final MechaPartItemRenderer renderer = new MechaPartItemRenderer();
    
    @Override
    public BlockEntityWithoutLevelRenderer getCustomRenderer() {
        return renderer;
    }
}
