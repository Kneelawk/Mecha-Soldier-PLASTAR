package com.github.plastar.client;

import com.github.plastar.Constants;
import com.github.plastar.client.model.MechaModelManager;
import com.github.plastar.data.Mecha;
import com.github.plastar.data.PRegistries;

import com.mojang.blaze3d.vertex.PoseStack;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.resources.model.Material;
import net.minecraft.core.component.DataComponents;
import net.minecraft.nbt.NbtOps;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.CustomData;

public class MechaItemRenderer extends BlockEntityWithoutLevelRenderer {
    // The whole idea of using BEWLR here is :concern:, but it's the neoforge way
    // On fabric we don't get any of the context, and we don't actually need it, so we just pass null
    @SuppressWarnings("DataFlowIssue") 
    public MechaItemRenderer() {
        super(null, null);
    }

    @Override
    public void renderByItem(ItemStack stack, ItemDisplayContext displayContext, PoseStack poseStack,
                             MultiBufferSource bufferSource, int packedLight, int packedOverlay) {
        var level = Minecraft.getInstance().level;
        if (level == null) return;
        
        var registries = level.registryAccess();
        var patternRegistry = registries.registryOrThrow(PRegistries.PATTERN);
        var paletteRegistry = registries.registryOrThrow(PRegistries.PALETTE);

        var mecha = stack.getOrDefault(DataComponents.BUCKET_ENTITY_DATA, CustomData.EMPTY)
            .read(registries.createSerializationContext(NbtOps.INSTANCE), Mecha.CODEC.fieldOf("mecha"))
            .result()
            .orElse(Mecha.DEFAULT);
        
        for (var entry : mecha.parts().entrySet()) {
            var part = entry.getValue();

            var preparedModel = MechaModelManager.INSTANCE.getModel(part.definition().location());
            if (preparedModel == null) continue;

            var texture = MechaEntityVisual.getTexture(patternRegistry.get(part.pattern()), paletteRegistry.get(part.palette()));
            var material = new Material(Constants.ATLAS_ID, texture);

            preparedModel.getItemModel(material).render(poseStack, bufferSource, packedLight, packedOverlay);
        }
    }
}
