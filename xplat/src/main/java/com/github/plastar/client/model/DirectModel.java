package com.github.plastar.client.model;

import java.util.List;

import com.github.plastar.client.PRenderTypes;

import org.lwjgl.system.MemoryUtil;

import dev.engine_room.flywheel.lib.memory.MemoryBlock;
import dev.engine_room.flywheel.lib.vertex.DefaultVertexList;

import com.mojang.blaze3d.vertex.PoseStack;

import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.resources.ResourceLocation;

public class DirectModel {
    private final List<Mesh> meshes;

    public DirectModel(List<Mesh> meshes) {
        this.meshes = meshes;
    }
    
    public void render(PoseStack poseStack, MultiBufferSource buffer, int packedLight, int packedOverlay) {
        poseStack.pushPose();
        for (var mesh : meshes) {
            var vertexConsumer = buffer.getBuffer(PRenderTypes.MECHA_ENTITY.apply(mesh.texture));
            var indices = mesh.indices;
            var vertices = mesh.vertices;
            
            for (int i = 0; i < mesh.indexCount(); i++) {
                var index = MemoryUtil.memGetInt(indices.ptr() + (long) i * Integer.BYTES);

                vertexConsumer.addVertex(poseStack.last(), vertices.x(index), vertices.y(index), vertices.z(index))
                    .setColor(0xffffffff)
                    .setUv(vertices.u(index), vertices.v(index))
                    .setLight(packedLight)
                    .setOverlay(packedOverlay)
                    .setNormal(poseStack.last(), vertices.normalX(index), vertices.normalY(index), vertices.normalZ(index));
            }
        }
        poseStack.popPose();
    }

    public record Mesh(MemoryBlock indices, int indexCount, DefaultVertexList vertices, ResourceLocation texture) {
    }
}
