package com.github.plastar.world.feature;

import com.github.plastar.Constants;

import net.minecraft.core.Vec3i;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.levelgen.blockpredicates.BlockPredicate;
import net.minecraft.world.level.levelgen.placement.BlockPredicateFilter;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;

import java.util.List;

public class PPlacedFeatures {
    public static final ResourceKey<PlacedFeature> STORAX_ACACIA = ResourceKey.create(Registries.PLACED_FEATURE, Constants.rl("storax_acacia"));

    public static void bootstrap(BootstrapContext<PlacedFeature> context) {
        context.register(STORAX_ACACIA, new PlacedFeature(
            context.lookup(Registries.CONFIGURED_FEATURE).getOrThrow(PConfiguredFeatures.STORAX_ACACIA),
            List.of(
                BlockPredicateFilter.forPredicate(BlockPredicate.wouldSurvive(Blocks.ACACIA_SAPLING.defaultBlockState(), Vec3i.ZERO))
            )));
    }
}
