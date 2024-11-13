package com.github.plastar.client;

import com.github.plastar.Constants;
import com.github.plastar.client.model.MechaModelManager;
import com.github.plastar.client.model.ModelMetadata;
import com.github.plastar.client.model.PreparedModel;
import com.github.plastar.data.MechaPart;
import com.github.plastar.data.MechaSection;
import com.github.plastar.data.Palette;
import com.github.plastar.data.Pattern;
import com.github.plastar.entity.MechaEntity;

import org.jetbrains.annotations.Nullable;

import org.joml.Matrix4f;

import net.minecraft.client.resources.model.Material;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;

public interface MechaEntityRenderUtils {
    static float getBodyRotation(float partialTick, MechaEntity entity) {
        return 180 - Mth.rotLerp(partialTick, entity.yBodyRotO, entity.yBodyRot);
    }

    static void applyBaseTransformation(float partialTick, Matrix4f matrixStack, MechaEntity entity) {
        matrixStack.rotateY(getBodyRotation(partialTick, entity) * Mth.DEG_TO_RAD);

        if (entity.deathTime > 0) {
            var fallProgress = ((float) entity.deathTime + partialTick - 1.0F) / 20.0F * 1.6F;
            fallProgress = Mth.sqrt(fallProgress);
            if (fallProgress > 1.0F) {
                fallProgress = 1.0F;
            }

            matrixStack.rotateZ(fallProgress * 90 * Mth.DEG_TO_RAD);
        }
    }

    static void applyPartTransform(MechaSection section, ModelMetadata metadata,
                                   float partialTick, Matrix4f matrixStack, MechaEntity entity) {
        var limbSwingAmount = entity.walkAnimation.speed(partialTick);
        var limbSwing = entity.walkAnimation.position(partialTick);

        var pivot = metadata.pivot().toVector3f().div(16);
        matrixStack.translate(pivot);
        switch (section) {
            case HEAD -> {
                var headRot = 180 - Mth.rotLerp(partialTick, entity.yHeadRotO, entity.yHeadRot);
                var netHeadRot = Mth.wrapDegrees(headRot -
                    getBodyRotation(partialTick, entity));
                matrixStack.rotateY(netHeadRot * Mth.DEG_TO_RAD);
                matrixStack.rotateX(-Mth.lerp(partialTick, entity.xRotO, entity.getXRot()) * Mth.DEG_TO_RAD);
            }
            case TORSO, BACKPACK -> {}
            case LEFT_ARM, SHIELD ->
                matrixStack.rotateX(Mth.cos(limbSwing * 0.6662F) * 2.0F * limbSwingAmount * 0.5F);
            case RIGHT_ARM, WEAPON ->
                matrixStack.rotateX(Mth.cos(limbSwing * 0.6662F + (float) Math.PI) * 2.0F * limbSwingAmount * 0.5F);
            case LEFT_LEG ->
                matrixStack.rotateX(Mth.cos(limbSwing * 0.6662F + (float) Math.PI) * 1.4F * limbSwingAmount);
            case RIGHT_LEG ->
                matrixStack.rotateX(Mth.cos(limbSwing * 0.6662F) * 1.4F * limbSwingAmount);
        }
        // Turn on to debug pivots
        // matrixStack.rotateY(Minecraft.getInstance().level.getGameTime() + ctx.partialTick());
        matrixStack.translate(-pivot.x, -pivot.y, -pivot.z);
    }

    static ResourceLocation getTexture(@Nullable Pattern pattern, @Nullable Palette palette) {
        if (pattern == null || palette == null) {
            return ResourceLocation.withDefaultNamespace("missingno");
        }
        return pattern.texture().withSuffix("_" + palette.textureSuffix());
    }

    static @Nullable ModelAndMaterial getModelAndMaterial(MechaPart part) {
        var model = part.definition().unwrapKey().flatMap(MechaModelManager.INSTANCE::getModel);
        if (model.isEmpty()) return null;

        var texture = MechaEntityRenderUtils.getTexture(part.pattern().value(), part.palette().value());
        var material = new Material(Constants.ATLAS_ID, texture);

        return new ModelAndMaterial(model.get(), material);
    }

    record ModelAndMaterial(PreparedModel model, Material material) {
    }
}
