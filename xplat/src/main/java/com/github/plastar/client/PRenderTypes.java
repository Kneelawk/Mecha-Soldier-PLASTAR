package com.github.plastar.client;

import java.util.function.Function;

import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.VertexFormat;

import net.minecraft.Util;
import net.minecraft.client.renderer.RenderStateShard;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.resources.ResourceLocation;

public class PRenderTypes {
    public static final Function<ResourceLocation, RenderType> MECHA_ENTITY = Util.memoize(id -> {
            RenderType.CompositeState compositeState = RenderType.CompositeState.builder()
                .setShaderState(RenderStateShard.RENDERTYPE_ITEM_ENTITY_TRANSLUCENT_CULL_SHADER)
                .setTextureState(new RenderStateShard.TextureStateShard(id, false, false))
                .setTransparencyState(RenderStateShard.TRANSLUCENT_TRANSPARENCY)
                .setOutputState(RenderStateShard.ITEM_ENTITY_TARGET)
                .setLightmapState(RenderStateShard.LIGHTMAP)
                .setOverlayState(RenderStateShard.OVERLAY)
                .setWriteMaskState(RenderStateShard.COLOR_DEPTH_WRITE)
                .createCompositeState(true);
            return RenderType.create("plastar:mecha_entity", DefaultVertexFormat.NEW_ENTITY, VertexFormat.Mode.TRIANGLES, RenderType.TRANSIENT_BUFFER_SIZE, true, true, compositeState);
        }
    );
}
