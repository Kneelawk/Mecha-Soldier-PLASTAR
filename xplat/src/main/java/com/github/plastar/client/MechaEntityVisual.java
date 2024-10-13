package com.github.plastar.client;

import java.util.List;

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
import net.minecraft.world.inventory.InventoryMenu;

public class MechaEntityVisual extends ComponentEntityVisual<MechaEntity> {
    private static final Material TEMP_MATERIAL = new Material(InventoryMenu.BLOCK_ATLAS, ResourceLocation.withDefaultNamespace("block/dirt"));
    private final TransformedInstance instance;

    public MechaEntityVisual(VisualizationContext ctx, MechaEntity entity, float partialTick) {
        super(ctx, entity, partialTick);

        var model = MechaModelManager.INSTANCE.getModel(Constants.rl("test_model"));
        if (model == null) {
            Log.LOG.warn("Failed to load model for mecha");
            instance = instancerProvider().instancer(InstanceTypes.TRANSFORMED, new SimpleModel(List.of())).createInstance();
        } else {
            instance = instancerProvider().instancer(InstanceTypes.TRANSFORMED, model.getModel(TEMP_MATERIAL)).createInstance();
        }

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
            .light(computePackedLight(ctx.partialTick()))
            .setChanged();
    }

    @Override
    protected void _delete() {
        instance.delete();
    }
}
