package com.github.plastar.client;

import com.github.plastar.entity.MechaEntity;

import dev.engine_room.flywheel.api.visualization.VisualizationContext;
import dev.engine_room.flywheel.lib.visual.ComponentEntityVisual;

public class MechaEntityVisual extends ComponentEntityVisual<MechaEntity> {
    public MechaEntityVisual(VisualizationContext ctx, MechaEntity entity, float partialTick) {
        super(ctx, entity, partialTick);
    }

    @Override
    protected void _delete() {
    }
}
