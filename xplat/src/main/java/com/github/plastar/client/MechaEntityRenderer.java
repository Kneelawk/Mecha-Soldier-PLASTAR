package com.github.plastar.client;

import com.github.plastar.Constants;
import com.github.plastar.entity.MechaEntity;

import org.joml.Matrix4f;

import com.mojang.blaze3d.vertex.PoseStack;

import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.resources.ResourceLocation;

public class MechaEntityRenderer extends EntityRenderer<MechaEntity> {
    private final Matrix4f sharedMatrix = new Matrix4f();

    public MechaEntityRenderer(EntityRendererProvider.Context context) {
        super(context);
    }

    @Override
    public void render(MechaEntity entity, float entityYaw, float partialTick, PoseStack poseStack,
                       MultiBufferSource bufferSource, int packedLight) {
        super.render(entity, entityYaw, partialTick, poseStack, bufferSource, packedLight);

        var mecha = entity.getMecha();
        poseStack.pushPose();
        sharedMatrix.identity();
        MechaEntityRenderUtils.applyBaseTransformation(partialTick, sharedMatrix, entity);
        poseStack.mulPose(sharedMatrix);

        for (var entry : mecha.parts().entrySet()) {
            if (MechaEntityRenderUtils.getModelAndMaterial(entry.getValue()) instanceof MechaEntityRenderUtils.ModelAndMaterial(
                var model, var material
            )) {
                poseStack.pushPose();
                sharedMatrix.identity();
                MechaEntityRenderUtils.applyPartTransform(entry.getKey(), model.getMetadata(), partialTick, sharedMatrix, entity);
                poseStack.mulPose(sharedMatrix);

                model.getDirectModel(material).render(poseStack, bufferSource, packedLight, LivingEntityRenderer.getOverlayCoords(entity, 0));

                poseStack.popPose();
            }
        }
        poseStack.popPose();
    }

    @Override
    public ResourceLocation getTextureLocation(MechaEntity entity) {
        // This isn't actually used anywhere unless we want to, so we can just give some bogus answer
        return Constants.ATLAS_ID;
    }
}
