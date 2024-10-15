package com.github.plastar.client;

import java.util.EnumMap;
import java.util.Map;

import com.github.plastar.Constants;
import com.github.plastar.client.model.MechaModelManager;
import com.github.plastar.data.Mecha;
import com.github.plastar.data.MechaSection;
import com.github.plastar.entity.MechaEntity;

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

import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.client.resources.model.Material;
import net.minecraft.util.Mth;

public class MechaEntityVisual extends ComponentEntityVisual<MechaEntity> implements SimpleTickableVisual {
    private final Matrix4fStack matrixStack = new Matrix4fStack(2);
    private final Map<MechaSection, TransformedInstance> instances = new EnumMap<>(MechaSection.class);
    private Mecha mecha;

    public MechaEntityVisual(VisualizationContext ctx, MechaEntity entity, float partialTick) {
        super(ctx, entity, partialTick);

        mecha = entity.getMecha();
        rebuildInstances();

        addComponent(new ShadowComponent(visualizationContext, entity).radius(0.7f));
        addComponent(new FireComponent(visualizationContext, entity));
        addComponent(new HitboxComponent(visualizationContext, entity));
    }

    private void rebuildInstances() {
        instances.values().forEach(TransformedInstance::delete);
        instances.clear();
        for (var entry : mecha.parts().entrySet()) {
            var section = entry.getKey();
            var part = entry.getValue();
            
            var model = MechaModelManager.INSTANCE.getModel(part.definition().location());
            if (model == null) continue;
            var texture = ClientPatternManager.INSTANCE.getTexture(part.pattern(), part.palette());
            var material = new Material(Constants.ATLAS_ID, texture);
            instances.put(section, instancerProvider().instancer(InstanceTypes.TRANSFORMED, model.getModel(material)).createInstance());
        }
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
            matrixStack.pushMatrix();
            switch (entry.getKey()) {
                case HEAD -> {
                    var headRot = 180 - Mth.rotLerp(partialTick, entity.yHeadRotO, entity.yHeadRot);
                    var netHeadRot = Mth.wrapDegrees(headRot - bodyRot);
                    matrixStack.rotateY(netHeadRot * Mth.DEG_TO_RAD);
                    matrixStack.translate(0, 13 / 16f, 0);
                    matrixStack.rotateX(-Mth.lerp(partialTick, entity.xRotO, entity.getXRot()) * Mth.DEG_TO_RAD);
                    matrixStack.translate(0, -13 / 16f, 0);
                }
                case TORSO -> {}
                case LEFT_ARM -> {
                    matrixStack.translate(5 / 16f, 12 / 16f, 0);
                    matrixStack.rotateX(Mth.cos(limbSwing * 0.6662F) * 2.0F * limbSwingAmount * 0.5F);
                    matrixStack.translate(-5 / 16f, -12 / 16f, 0);
                }
                case RIGHT_ARM -> {
                    matrixStack.translate(-5 / 16f, 12 / 16f, 0);
                    matrixStack.rotateX(Mth.cos(limbSwing * 0.6662F + (float) Math.PI) * 2.0F * limbSwingAmount * 0.5F);
                    matrixStack.translate(5 / 16f, -12 / 16f, 0);
                }
                case LEFT_LEG -> {
                    matrixStack.translate(2 / 16f, 3 / 16f, 0);
                    matrixStack.rotateX(Mth.cos(limbSwing * 0.6662F + (float) Math.PI) * 1.4F * limbSwingAmount);
                    matrixStack.translate(-2 / 16f, -3 / 16f, 0);
                }
                case RIGHT_LEG -> {
                    matrixStack.translate(-2 / 16f, 3 / 16f, 0);
                    matrixStack.rotateX(Mth.cos(limbSwing * 0.6662F) * 1.4F * limbSwingAmount);
                    matrixStack.translate(2 / 16f, -3 / 16f, 0);
                }
                case BACKPACK -> {}
                case WEAPON -> {}
                case SHIELD -> {}
            }
            
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
