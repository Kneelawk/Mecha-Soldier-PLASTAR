package com.github.plastar.world.feature;

import com.github.plastar.PLASTARMod;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.levelgen.feature.treedecorators.TreeDecorator;
import net.minecraft.world.level.levelgen.feature.treedecorators.TreeDecoratorType;

import java.util.function.Supplier;

public class PTreeDecoratorTypes {
    public static final Supplier<TreeDecoratorType<StoraxLogTreeDecorator>> STORAX_LOG = register("storax_log", () -> StoraxLogTreeDecorator.TYPE);

    public static void register() {

    }

    private static <T extends TreeDecorator> Supplier<TreeDecoratorType<T>> register(String key, Supplier<TreeDecoratorType<T>> treeDecoratorType) {
        return PLASTARMod.REGISTRARS.get(Registries.TREE_DECORATOR_TYPE).register(key, treeDecoratorType);
    }
}
