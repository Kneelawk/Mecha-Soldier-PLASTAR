package com.github.plastar.client;

import com.github.plastar.client.model.TemporaryModelLoader;
import com.github.plastar.entity.MechaEntity;

import dev.engine_room.flywheel.api.visualization.VisualizationContext;
import dev.engine_room.flywheel.lib.instance.InstanceTypes;
import dev.engine_room.flywheel.lib.instance.TransformedInstance;
import dev.engine_room.flywheel.lib.visual.ComponentEntityVisual;
import dev.engine_room.flywheel.lib.visual.component.FireComponent;
import dev.engine_room.flywheel.lib.visual.component.HitboxComponent;
import dev.engine_room.flywheel.lib.visual.component.ShadowComponent;

public class MechaEntityVisual extends ComponentEntityVisual<MechaEntity> {
    private final TransformedInstance instance;

    public MechaEntityVisual(VisualizationContext ctx, MechaEntity entity, float partialTick) {
        super(ctx, entity, partialTick);

        instance =
            instancerProvider().instancer(InstanceTypes.TRANSFORMED, TemporaryModelLoader.MODEL).createInstance();

        addComponent(new ShadowComponent(visualizationContext, entity).radius(0.7f));
        addComponent(new FireComponent(visualizationContext, entity));
        addComponent(new HitboxComponent(visualizationContext, entity));
        
        instance.setChanged();
    }

    @Override
    public void beginFrame(Context ctx) {
        super.beginFrame(ctx);
        instance.setIdentityTransform()
            .translate(getVisualPosition(ctx.partialTick()))
            .setChanged();
    }

    @Override
    protected void _delete() {
        instance.delete();
    }
}
