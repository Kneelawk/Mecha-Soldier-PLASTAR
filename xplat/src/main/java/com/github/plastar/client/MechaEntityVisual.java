package com.github.plastar.client;

import java.util.List;
import java.util.Random;
import java.util.UUID;

import com.github.plastar.Constants;
import com.github.plastar.Log;
import com.github.plastar.client.model.MechaModelManager;
import com.github.plastar.entity.MechaEntity;

import dev.engine_room.flywheel.api.visualization.VisualizationContext;
import dev.engine_room.flywheel.lib.instance.InstanceTypes;
import dev.engine_room.flywheel.lib.instance.TransformedInstance;
import dev.engine_room.flywheel.lib.model.SimpleModel;
import dev.engine_room.flywheel.lib.visual.ComponentEntityVisual;
import dev.engine_room.flywheel.lib.visual.component.FireComponent;
import dev.engine_room.flywheel.lib.visual.component.HitboxComponent;
import dev.engine_room.flywheel.lib.visual.component.ShadowComponent;

import net.minecraft.client.resources.model.Material;
import net.minecraft.resources.ResourceLocation;

public class MechaEntityVisual extends ComponentEntityVisual<MechaEntity> {
    private final TransformedInstance instance;

    public MechaEntityVisual(VisualizationContext ctx, MechaEntity entity, float partialTick) {
        super(ctx, entity, partialTick);

        var model = MechaModelManager.INSTANCE.getModel(Constants.rl("test_model"));
        if (model == null) {
            Log.LOG.warn("Failed to load model for mecha");
            instance = instancerProvider().instancer(InstanceTypes.TRANSFORMED, new SimpleModel(List.of())).createInstance();
        } else {
            var material = new Material(Constants.ATLAS_ID, pickTexture(entity.getUUID()));
            instance = instancerProvider().instancer(InstanceTypes.TRANSFORMED, model.getModel(material)).createInstance();
        }

        addComponent(new ShadowComponent(visualizationContext, entity).radius(0.7f));
        addComponent(new FireComponent(visualizationContext, entity));
        addComponent(new HitboxComponent(visualizationContext, entity));
        
        instance.setChanged();
    }
    
    private ResourceLocation pickTexture(UUID uuid) {
        var random = new Random(uuid.hashCode());
        var pattern = switch (random.nextInt(3)) {
            case 0 -> "checker";
            case 1 -> "striped";
            case 2 -> "core";
            default -> throw new IllegalStateException();
        };
        var palette = switch (random.nextInt(3)) {
            case 0 -> "a";
            case 1 -> "b";
            case 2 -> "c";
            default -> throw new IllegalStateException();
        };
        return Constants.rl("mecha/test_part/" + pattern + "_" + palette);
    }

    @Override
    public void beginFrame(Context ctx) {
        super.beginFrame(ctx);
        instance.setIdentityTransform()
            .translate(getVisualPosition(ctx.partialTick()))
            .light(computePackedLight(ctx.partialTick()))
            .setChanged();
    }

    @Override
    protected void _delete() {
        instance.delete();
    }
}
