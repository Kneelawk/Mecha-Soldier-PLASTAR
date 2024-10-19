package com.github.plastar.client;

import com.github.plastar.Constants;
import com.github.plastar.client.model.MechaModelManager;
import com.github.plastar.data.PRegistries;
import com.github.plastar.item.PComponents;

import org.joml.Matrix4f;

import com.mojang.blaze3d.vertex.PoseStack;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.resources.model.Material;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;

public class MechaPartItemRenderer extends BlockEntityWithoutLevelRenderer {
    // The whole idea of using BEWLR here is :concern:, but it's the neoforge way
    // On fabric we don't get any of the context, and we don't actually need it, so we just pass null
    @SuppressWarnings("DataFlowIssue") 
    public MechaPartItemRenderer() {
        super(null, null);
    }

    @Override
    public void renderByItem(ItemStack stack, ItemDisplayContext displayContext, PoseStack poseStack,
                             MultiBufferSource bufferSource, int packedLight, int packedOverlay) {
        var level = Minecraft.getInstance().level;
        if (level == null) return;
        
        var registries = level.registryAccess();
        var patternRegistry = registries.registryOrThrow(PRegistries.PATTERN);
        var paletteRegistry = registries.registryOrThrow(PRegistries.PALETTE);

        var mechaPart = stack.get(PComponents.MECHA_PART.get());
        if (mechaPart == null) return;
        
        var preparedModel = MechaModelManager.INSTANCE.getModel(mechaPart.definition().location());
        if (preparedModel == null) return;

        var texture = MechaEntityVisual.getTexture(patternRegistry.get(mechaPart.pattern()), paletteRegistry.get(mechaPart.palette()));
        var material = new Material(Constants.ATLAS_ID, texture);

        poseStack.pushPose();
        var metadata = preparedModel.getMetadata();
        var offset = metadata.itemModelOffset();
        poseStack.mulPose(new Matrix4f().scaleAround(metadata.itemModelScale(), 0.5f, 0.5f, 0.5f));
        poseStack.translate(offset.x / 16f, offset.y / 16f, offset.z / 16f);
        
        preparedModel.getItemModel(material).render(poseStack, bufferSource, packedLight, packedOverlay);
        
        poseStack.popPose();
    }
}
