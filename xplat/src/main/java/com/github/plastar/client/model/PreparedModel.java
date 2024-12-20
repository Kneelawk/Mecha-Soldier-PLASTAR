package com.github.plastar.client.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import dev.engine_room.flywheel.api.model.Model;
import dev.engine_room.flywheel.lib.material.SimpleMaterial;
import dev.engine_room.flywheel.lib.memory.MemoryBlock;
import dev.engine_room.flywheel.lib.model.RetexturedMesh;
import dev.engine_room.flywheel.lib.model.SimpleModel;
import dev.engine_room.flywheel.lib.vertex.PosTexNormalVertexView;

import net.minecraft.client.resources.model.Material;

public final class PreparedModel {
    private final Map<Material, Model> modelCache = new HashMap<>();
    private final Map<Material, DirectModel> itemModelCache = new HashMap<>();

    private final List<BridgedMesh> meshes;
    private final ModelMetadata metadata;

    public PreparedModel(List<BridgedMesh> meshes, ModelMetadata metadata) {
        this.meshes = meshes;
        this.metadata = metadata;
    }

    public Model getFlywheelModel(Material material) {
        return modelCache.computeIfAbsent(material, this::configure);
    }

    public DirectModel getDirectModel(Material material) {
        return itemModelCache.computeIfAbsent(material, this::configureItem);
    }

    public ModelMetadata getMetadata() {
        return metadata;
    }

    private Model configure(Material mcMaterial) {
        var sprite = mcMaterial.sprite();
        var material = SimpleMaterial.builder()
            .backfaceCulling(false)
            .mipmap(false)
            .texture(sprite.atlasLocation())
            .build();

        var configured = new ArrayList<Model.ConfiguredMesh>(meshes.size());
        for (var mesh : meshes) {
            configured.add(new Model.ConfiguredMesh(material, new RetexturedMesh(mesh, sprite)));
        }
        return new SimpleModel(configured);
    }
    
    private DirectModel configureItem(Material mcMaterial) {
        var modelMeshes = new ArrayList<DirectModel.Mesh>();
        var sprite = mcMaterial.sprite();
        for (var mesh : meshes) {
            var vertexList = new PosTexNormalVertexView();
            var vertexBlock = MemoryBlock.mallocTracked((long) mesh.vertexCount() * PosTexNormalVertexView.STRIDE);
            vertexList.ptr(vertexBlock.ptr());
            vertexList.nativeMemoryOwner(vertexBlock);
            vertexList.vertexCount(mesh.vertexCount());

            new RetexturedMesh(mesh, sprite).write(vertexList);
            vertexList.ptr(vertexBlock.ptr());

            var indexBlock = MemoryBlock.mallocTracked((long) mesh.indexCount() * Integer.BYTES);
            mesh.indexSequence().fill(indexBlock.ptr(), mesh.indexCount());

            modelMeshes.add(new DirectModel.Mesh(indexBlock, mesh.indexCount(), vertexList, sprite.atlasLocation()));
        }
        return new DirectModel(modelMeshes);
    }
}
