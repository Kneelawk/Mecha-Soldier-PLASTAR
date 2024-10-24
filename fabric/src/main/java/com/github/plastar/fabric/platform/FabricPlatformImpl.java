package com.github.plastar.fabric.platform;

import com.github.plastar.platform.Platform;
import com.mojang.serialization.MapCodec;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.levelgen.feature.treedecorators.TreeDecorator;
import net.minecraft.world.level.levelgen.feature.treedecorators.TreeDecoratorType;

public class FabricPlatformImpl implements Platform {
    @Override
    public <T extends TreeDecorator> TreeDecoratorType<T> treeDecoratorType(ResourceLocation rl, MapCodec<T> decorator) {
        return new TreeDecoratorType<>(decorator);
    }
}
