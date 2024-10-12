package com.github.plastar.client;

import com.github.plastar.entity.PEntities;

import dev.engine_room.flywheel.lib.visualization.SimpleEntityVisualizer;

public class PLASTARClient {
    public static void init() {
        SimpleEntityVisualizer.builder(PEntities.MECHA_ENTITY)
            .factory(MechaEntityVisual::new)
            .apply();
    }
}
