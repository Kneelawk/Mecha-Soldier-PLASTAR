package com.github.plastar.world.feature;

import java.util.List;

import com.github.plastar.Constants;
import com.google.common.collect.ImmutableList;

import net.minecraft.core.Vec3i;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.data.worldgen.placement.PlacementUtils;
import net.minecraft.resources.ResourceKey;
import net.minecraft.util.random.SimpleWeightedRandomList;
import net.minecraft.util.valueproviders.ConstantInt;
import net.minecraft.util.valueproviders.IntProvider;
import net.minecraft.util.valueproviders.WeightedListInt;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.levelgen.blockpredicates.BlockPredicate;
import net.minecraft.world.level.levelgen.placement.BiomeFilter;
import net.minecraft.world.level.levelgen.placement.BlockPredicateFilter;
import net.minecraft.world.level.levelgen.placement.CountPlacement;
import net.minecraft.world.level.levelgen.placement.InSquarePlacement;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;
import net.minecraft.world.level.levelgen.placement.PlacementModifier;
import net.minecraft.world.level.levelgen.placement.SurfaceWaterDepthFilter;

public class PPlacedFeatures {
    public static final ResourceKey<PlacedFeature> STORAX_ACACIA =
        ResourceKey.create(Registries.PLACED_FEATURE, Constants.rl("storax_acacia"));
    public static final ResourceKey<PlacedFeature> STORAX_TREES =
        ResourceKey.create(Registries.PLACED_FEATURE, Constants.rl("storax_trees"));

    public static void bootstrap(BootstrapContext<PlacedFeature> context) {
        context.register(STORAX_ACACIA, new PlacedFeature(
            context.lookup(Registries.CONFIGURED_FEATURE).getOrThrow(PConfiguredFeatures.STORAX_ACACIA),
            List.of(
                BlockPredicateFilter.forPredicate(
                    BlockPredicate.allOf(
                        BlockPredicate.wouldSurvive(Blocks.ACACIA_SAPLING.defaultBlockState(), Vec3i.ZERO),
                        BlockPredicate.solid(new Vec3i(0, -1, 0)),
                        BlockPredicate.matchesBlocks(new Vec3i(0, -1, 0),
                            List.of(
                                Blocks.GRASS_BLOCK,
                                Blocks.DIRT,
                                Blocks.COARSE_DIRT,
                                Blocks.PODZOL,
                                Blocks.ROOTED_DIRT
                            ))
                    )
                )
            )));

        var tree_density = SimpleWeightedRandomList.<IntProvider>builder()
            .add(ConstantInt.of(0), 4)
            .add(ConstantInt.of(1), 1)
            .build();

        context.register(STORAX_TREES, new PlacedFeature(
            context.lookup(Registries.CONFIGURED_FEATURE).getOrThrow(PConfiguredFeatures.STORAX_ACACIA),
            ImmutableList.<PlacementModifier>builder()
                .add(CountPlacement.of(new WeightedListInt(tree_density)))
                .add(InSquarePlacement.spread())
                .add(SurfaceWaterDepthFilter.forMaxDepth(0))
                .add(PlacementUtils.HEIGHTMAP_OCEAN_FLOOR)
                .add(BiomeFilter.biome()).build()));
    }
}
