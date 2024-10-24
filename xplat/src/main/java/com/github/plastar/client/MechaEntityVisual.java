package com.github.plastar.client;

import java.util.EnumMap;
import java.util.Map;

import com.github.plastar.Constants;
import com.github.plastar.client.model.MechaModelManager;
import com.github.plastar.data.Mecha;
import com.github.plastar.data.MechaSection;
import com.github.plastar.data.PRegistries;
import com.github.plastar.data.Palette;
import com.github.plastar.data.Pattern;
import com.github.plastar.entity.MechaEntity;

import net.minecraft.client.Minecraft;

import org.jetbrains.annotations.Nullable;

import dev.engine_room.flywheel.api.visual.DynamicVisual;
import dev.engine_room.flywheel.api.visual.TickableVisual;
import dev.engine_room.flywheel.api.visualization.VisualizationContext;
import dev.engine_room.flywheel.lib.instance.InstanceTypes;
import dev.engine_room.flywheel.lib.instance.TransformedInstance;
import dev.engine_room.flywheel.lib.visual.ComponentEntityVisual;
import dev.engine_room.flywheel.lib.visual.SimpleTickableVisual;
import dev.engine_room.flywheel.lib.visual.component.FireComponent;
import dev.engine_room.flywheel.lib.visual.component.HitboxComponent;
import dev.engine_room.flywheel.lib.visual.component.ShadowComponent;

import org.joml.Matrix4fStack;
import org.joml.Vector3f;

import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.client.resources.model.Material;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;

public class MechaEntityVisual extends ComponentEntityVisual<MechaEntity> implements SimpleTickableVisual {
    private final Matrix4fStack matrixStack = new Matrix4fStack(2);
    private final Map<MechaSection, TransformedInstance> instances = new EnumMap<>(MechaSection.class);
    private final Map<MechaSection, Vector3f> pivots = new EnumMap<>(MechaSection.class);
    private Mecha mecha;

    public MechaEntityVisual(VisualizationContext ctx, MechaEntity entity, float partialTick) {
        super(ctx, entity, partialTick);

        mecha = entity.getMecha();
        rebuildInstances();

        addComponent(new ShadowComponent(visualizationContext, entity).radius(0.5f));
        addComponent(new FireComponent(visualizationContext, entity));
        addComponent(new HitboxComponent(visualizationContext, entity));
    }

    private void rebuildInstances() {
        instances.values().forEach(TransformedInstance::delete);
        instances.clear();
        pivots.clear();
        
        var patternRegistry = entity.level().registryAccess().registryOrThrow(PRegistries.PATTERN);
        var paletteRegistry = entity.level().registryAccess().registryOrThrow(PRegistries.PALETTE);
        
        for (var entry : mecha.parts().entrySet()) {
            var section = entry.getKey();
            var part = entry.getValue();
            
            var model = MechaModelManager.INSTANCE.getModel(part.definition().location());
            if (model == null) continue;
            var texture = getTexture(patternRegistry.get(part.pattern()), paletteRegistry.get(part.palette()));
            var material = new Material(Constants.ATLAS_ID, texture);
            instances.put(section, instancerProvider().instancer(InstanceTypes.TRANSFORMED, model.getModel(material)).createInstance());
            pivots.put(section, model.getMetadata().pivot().toVector3f().div(16f));
        }
    }
    
    public static ResourceLocation getTexture(@Nullable Pattern pattern, @Nullable Palette palette) {
        if (pattern == null || palette == null) {
            return ResourceLocation.withDefaultNamespace("missingno");
        }
        return pattern.texture().withSuffix("_" + palette.textureSuffix());
    }

    @Override
    public void tick(TickableVisual.Context context) {
        if (mecha != entity.getMecha()) {
            mecha = entity.getMecha();
            rebuildInstances();
        }
    }

    @Override
    public void beginFrame(DynamicVisual.Context ctx) {
        super.beginFrame(ctx);
        var partialTick = ctx.partialTick();
        var overlay = LivingEntityRenderer.getOverlayCoords(entity, 0);
        
        matrixStack.identity();
        matrixStack.translate(getVisualPosition(partialTick));
        var bodyRot = 180 - Mth.rotLerp(partialTick, entity.yBodyRotO, entity.yBodyRot);
        matrixStack.rotateY(bodyRot * Mth.DEG_TO_RAD);
        
        if (entity.deathTime > 0) {
            var fallProgress = ((float)entity.deathTime + partialTick - 1.0F) / 20.0F * 1.6F;
            fallProgress = Mth.sqrt(fallProgress);
            if (fallProgress > 1.0F) {
                fallProgress = 1.0F;
            }

            matrixStack.rotateZ(fallProgress * 90 * Mth.DEG_TO_RAD);
        }
        
        var limbSwingAmount = entity.walkAnimation.speed(partialTick);
        var limbSwing = entity.walkAnimation.position(partialTick);

        for (var entry : instances.entrySet()) {
            var instance = entry.getValue();
            var pivot = pivots.get(entry.getKey());
            matrixStack.pushMatrix();
            matrixStack.translate(pivot);
            switch (entry.getKey()) {
                case HEAD -> {
                    var headRot = 180 - Mth.rotLerp(partialTick, entity.yHeadRotO, entity.yHeadRot);
                    var netHeadRot = Mth.wrapDegrees(headRot - bodyRot);
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
            
            instance.setIdentityTransform()
                .setTransform(matrixStack)
                .light(computePackedLight(partialTick))
                .overlay(overlay)
                .setChanged();
            matrixStack.popMatrix();
        }
    }

    @Override
    protected void _delete() {
        super._delete();
        instances.values().forEach(TransformedInstance::delete);
    }
}
