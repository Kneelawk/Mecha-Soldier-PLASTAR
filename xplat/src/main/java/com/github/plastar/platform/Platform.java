package com.github.plastar.platform;

import com.mojang.serialization.MapCodec;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.levelgen.feature.treedecorators.TreeDecorator;
import net.minecraft.world.level.levelgen.feature.treedecorators.TreeDecoratorType;

import java.util.ServiceLoader;

public interface Platform {
    Platform INSTANCE = ServiceLoader.load(Platform.class).findFirst()
        .orElseThrow(() -> new RuntimeException("No PLASTAR platform provided"));

    <T extends TreeDecorator> TreeDecoratorType<T> treeDecoratorType(ResourceLocation rl, MapCodec<T> decorator);
}
