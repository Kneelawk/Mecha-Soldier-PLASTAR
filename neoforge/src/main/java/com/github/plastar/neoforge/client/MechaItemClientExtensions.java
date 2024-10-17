package com.github.plastar.neoforge.client;

import com.github.plastar.client.MechaItemRenderer;

import net.neoforged.neoforge.client.extensions.common.IClientItemExtensions;

import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;

public class MechaItemClientExtensions implements IClientItemExtensions {
    private final MechaItemRenderer renderer = new MechaItemRenderer();
    
    @Override
    public BlockEntityWithoutLevelRenderer getCustomRenderer() {
        return renderer;
    }
}
