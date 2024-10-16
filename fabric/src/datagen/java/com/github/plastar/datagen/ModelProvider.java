package com.github.plastar.datagen;

import com.github.plastar.block.PBlocks;
import com.github.plastar.block.StoraxAcaciaLogBlock;
import com.github.plastar.item.PItems;

import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricModelProvider;

import net.minecraft.data.models.BlockModelGenerators;
import net.minecraft.data.models.ItemModelGenerators;
import net.minecraft.data.models.blockstates.MultiVariantGenerator;
import net.minecraft.data.models.blockstates.PropertyDispatch;
import net.minecraft.data.models.blockstates.Variant;
import net.minecraft.data.models.blockstates.VariantProperties;
import net.minecraft.data.models.model.ModelLocationUtils;
import net.minecraft.data.models.model.ModelTemplates;
import net.minecraft.data.models.model.TexturedModel;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;

public class ModelProvider extends FabricModelProvider {
    public ModelProvider(FabricDataOutput output) {
        super(output);
    }

    @Override
    public void generateBlockStateModels(BlockModelGenerators generators) {
        generateSappyLog(generators, PBlocks.STORAX_ACACIA_LOG_BLOCK.get(), Blocks.ACACIA_LOG);
    }

    private static void generateSappyLog(BlockModelGenerators generators, Block block, Block baseBlock) {
        var sappyModel = TexturedModel.COLUMN.create(block, generators.modelOutput);
        var horizontalSappyModel = TexturedModel.COLUMN_HORIZONTAL.create(block, generators.modelOutput);
        var model = ModelLocationUtils.getModelLocation(baseBlock);
        var horizontalModel = ModelLocationUtils.getModelLocation(baseBlock, "_horizontal");

        generators.blockStateOutput.accept(MultiVariantGenerator.multiVariant(block)
            .with(PropertyDispatch.properties(BlockStateProperties.AXIS, StoraxAcaciaLogBlock.SAPPY)
                .generate((axis, sappy) -> switch (axis) {
                    case Y -> Variant.variant().with(VariantProperties.MODEL, sappy ? sappyModel : model);
                    case Z -> Variant.variant().with(VariantProperties.MODEL, sappy ? horizontalSappyModel : horizontalModel)
                        .with(VariantProperties.X_ROT, VariantProperties.Rotation.R90);
                    case X -> Variant.variant()
                        .with(VariantProperties.MODEL, sappy ? horizontalSappyModel : horizontalModel)
                        .with(VariantProperties.X_ROT, VariantProperties.Rotation.R90)
                        .with(VariantProperties.Y_ROT, VariantProperties.Rotation.R90);
                })));
    }

    @Override
    public void generateItemModels(ItemModelGenerators generators) {
        generators.generateFlatItem(PItems.POLYSTYRENE.get(), ModelTemplates.FLAT_ITEM);
        generators.generateFlatItem(PItems.STYROL.get(), ModelTemplates.FLAT_ITEM);
        generators.generateFlatItem(PItems.TREE_TAP.get(), ModelTemplates.FLAT_ITEM);
    }
}