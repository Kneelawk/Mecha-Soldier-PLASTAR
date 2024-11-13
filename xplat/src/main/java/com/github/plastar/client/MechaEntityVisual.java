package com.github.plastar.client;

import java.util.EnumMap;
import java.util.Map;

import com.github.plastar.client.model.ModelMetadata;
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

public class MechaEntityVisual extends ComponentEntityVisual<MechaEntity> implements SimpleTickableVisual {
    private final Matrix4fStack matrixStack = new Matrix4fStack(2);
    private final Map<MechaSection, TransformedInstance> instances = new EnumMap<>(MechaSection.class);
    private final Map<MechaSection, ModelMetadata> partMetadata = new EnumMap<>(MechaSection.class);
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
        partMetadata.clear();
        
        for (var entry : mecha.parts().entrySet()) {
            var section = entry.getKey();
            var part = entry.getValue();

            if (MechaEntityRenderUtils.getModelAndMaterial(part) instanceof MechaEntityRenderUtils.ModelAndMaterial(
                var model, var material
            )) {
                instances.put(section, instancerProvider().instancer(InstanceTypes.TRANSFORMED, model.getFlywheelModel(material)).createInstance());
                partMetadata.put(section, model.getMetadata());
            }
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
        MechaEntityRenderUtils.applyBaseTransformation(partialTick, matrixStack, entity);

        for (var entry : instances.entrySet()) {
            var instance = entry.getValue();
            var metadata = partMetadata.get(entry.getKey());
            matrixStack.pushMatrix();
            MechaEntityRenderUtils.applyPartTransform(entry.getKey(), metadata, partialTick, matrixStack, entity);
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
