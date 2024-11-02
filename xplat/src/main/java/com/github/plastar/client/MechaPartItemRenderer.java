package com.github.plastar.client;

import com.github.plastar.Constants;
import com.github.plastar.client.model.MechaModelManager;
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

        var mechaPart = stack.get(PComponents.MECHA_PART.get());
        if (mechaPart == null) return;
        
        var preparedModel = mechaPart.definition().unwrapKey().flatMap(MechaModelManager.INSTANCE::getModel);
        if (preparedModel.isEmpty()) return;

        var texture = MechaEntityVisual.getTexture(mechaPart.pattern().value(), mechaPart.palette().value());
        var material = new Material(Constants.ATLAS_ID, texture);

        poseStack.pushPose();
        var metadata = preparedModel.get().getMetadata();
        var offset = metadata.itemModelOffset();
        poseStack.mulPose(new Matrix4f().scaleAround(metadata.itemModelScale(), 0.5f, 0.5f, 0.5f));
        poseStack.translate(offset.x / 16f, offset.y / 16f, offset.z / 16f);
        
        preparedModel.get().getItemModel(material).render(poseStack, bufferSource, packedLight, packedOverlay);
        
        poseStack.popPose();
    }
}
