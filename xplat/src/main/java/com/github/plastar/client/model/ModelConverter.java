package com.github.plastar.client.model;

import java.util.ArrayList;

import de.javagl.jgltf.model.GltfModel;
import dev.engine_room.flywheel.api.model.Mesh;

import org.joml.Matrix4f;

public class ModelConverter {
    public static PreparedModel convert(GltfModel model) {
        var configuredMeshes = new ArrayList<Mesh>();
        for (var nodeModel : model.getNodeModels()) {
            var meshModels = nodeModel.getMeshModels();
            if (meshModels.isEmpty()) continue;
            var transformValues = nodeModel.computeGlobalTransform(new float[16]);
            var transform = new Matrix4f().set(transformValues);

            for (var meshModel : meshModels) {
                for (var primitiveModel : meshModel.getMeshPrimitiveModels()) {
                    configuredMeshes.add(new BridgedMesh(primitiveModel, transform));
                }
            }
        }
        
        return new PreparedModel(configuredMeshes);
    }
}
