package com.github.plastar.client.model;

import java.util.List;

import com.github.plastar.client.PRenderTypes;

import org.lwjgl.system.MemoryUtil;

import dev.engine_room.flywheel.lib.memory.MemoryBlock;
import dev.engine_room.flywheel.lib.vertex.DefaultVertexList;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.resources.ResourceLocation;

public class DirectModel {
    private final List<Mesh> meshes;

    public DirectModel(List<Mesh> meshes) {
        this.meshes = meshes;
    }
    
    @SuppressWarnings("deprecation") // We need to use the deprecated hack to force disable fabulous rendering
    public void render(PoseStack poseStack, MultiBufferSource buffer, int packedLight, int packedOverlay,
                       boolean useFabulousHack) {
        poseStack.pushPose();

        if (useFabulousHack && Minecraft.useShaderTransparency()) {
            RenderSystem.runAsFancy(() -> {
                for (var mesh : meshes) {
                    var vertexConsumer = buffer.getBuffer(PRenderTypes.MECHA_ENTITY.apply(mesh.texture));
                    renderMesh(poseStack, packedLight, packedOverlay, mesh, vertexConsumer);

                    if (buffer instanceof MultiBufferSource.BufferSource bufferSource) {
                        bufferSource.endLastBatch();
                    }
                }
            });
        }

        for (var mesh : meshes) {
            var vertexConsumer = buffer.getBuffer(PRenderTypes.MECHA_ENTITY.apply(mesh.texture));
            renderMesh(poseStack, packedLight, packedOverlay, mesh, vertexConsumer);
        }
        poseStack.popPose();
    }

    private static void renderMesh(PoseStack poseStack, int packedLight, int packedOverlay, Mesh mesh,
                                  VertexConsumer vertexConsumer) {
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

    public record Mesh(MemoryBlock indices, int indexCount, DefaultVertexList vertices, ResourceLocation texture) {
    }
}
