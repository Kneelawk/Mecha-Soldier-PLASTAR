package com.github.plastar.client.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import dev.engine_room.flywheel.api.model.Mesh;
import dev.engine_room.flywheel.api.model.Model;
import dev.engine_room.flywheel.lib.material.SimpleMaterial;
import dev.engine_room.flywheel.lib.model.RetexturedMesh;
import dev.engine_room.flywheel.lib.model.SimpleModel;

import net.minecraft.client.resources.model.Material;

public final class PreparedModel {
    private final Map<Material, Model> modelCache = new HashMap<>();
    private final List<Mesh> meshes;

    public PreparedModel(List<Mesh> meshes) {
        this.meshes = meshes;
    }
    
    public Model getModel(Material material) {
        return modelCache.computeIfAbsent(material, this::configure);
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
}
