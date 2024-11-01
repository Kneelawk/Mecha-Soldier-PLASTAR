package com.github.plastar.world.feature;

import java.util.List;

import com.github.plastar.Constants;

import com.github.plastar.block.PBlocks;

import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.data.worldgen.placement.TreePlacements;
import net.minecraft.resources.ResourceKey;
import net.minecraft.util.valueproviders.ConstantInt;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.WeightedPlacedFeature;
import net.minecraft.world.level.levelgen.feature.configurations.RandomFeatureConfiguration;
import net.minecraft.world.level.levelgen.feature.configurations.TreeConfiguration;
import net.minecraft.world.level.levelgen.feature.featuresize.TwoLayersFeatureSize;
import net.minecraft.world.level.levelgen.feature.foliageplacers.AcaciaFoliagePlacer;
import net.minecraft.world.level.levelgen.feature.stateproviders.BlockStateProvider;
import net.minecraft.world.level.levelgen.feature.trunkplacers.ForkingTrunkPlacer;

public class PConfiguredFeatures {
    public static final ResourceKey<ConfiguredFeature<?, ?>> STORAX_ACACIA =
        ResourceKey.create(Registries.CONFIGURED_FEATURE, Constants.rl("storax_acacia"));
    public static final ResourceKey<ConfiguredFeature<?, ?>> STORAX_TREES =
        ResourceKey.create(Registries.CONFIGURED_FEATURE, Constants.rl("storax_trees"));

    public static void bootstrap(BootstrapContext<ConfiguredFeature<?, ?>> context) {
        context.register(STORAX_ACACIA,
            new ConfiguredFeature<>(Feature.TREE, new TreeConfiguration.TreeConfigurationBuilder(
                BlockStateProvider.simple(Blocks.ACACIA_LOG),
                new ForkingTrunkPlacer(5, 2, 2),
                BlockStateProvider.simple(Blocks.ACACIA_LEAVES),
                new AcaciaFoliagePlacer(ConstantInt.of(2), ConstantInt.of(0)),
                new TwoLayersFeatureSize(1, 0, 2))
                .decorators(List.of(new StoraxLogTreeDecorator()))
                .ignoreVines()
                .build()));

        // Wish there was a way to pass directly through to another placed feature but there isn't, so I'm using this cheap hack
        context.register(STORAX_TREES, new ConfiguredFeature<>(Feature.RANDOM_SELECTOR, new RandomFeatureConfiguration(
            List.of(new WeightedPlacedFeature(
                context.lookup(Registries.PLACED_FEATURE).getOrThrow(PPlacedFeatures.STORAX_ACACIA), 1F)),
            context.lookup(Registries.PLACED_FEATURE).getOrThrow(TreePlacements.ACACIA_CHECKED))));
    }
}
