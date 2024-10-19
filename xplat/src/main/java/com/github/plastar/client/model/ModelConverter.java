package com.github.plastar.client.model;

import java.util.ArrayList;

import de.javagl.jgltf.model.GltfModel;

import org.joml.Matrix4f;

public class ModelConverter {
    public static PreparedModel convert(GltfModel model, ModelMetadata metadata) {
        var meshes = new ArrayList<BridgedMesh>();
        for (var nodeModel : model.getNodeModels()) {
            var meshModels = nodeModel.getMeshModels();
            if (meshModels.isEmpty()) continue;
            var transformValues = nodeModel.computeGlobalTransform(new float[16]);
            var transform = new Matrix4f().set(transformValues);

            for (var meshModel : meshModels) {
                for (var primitiveModel : meshModel.getMeshPrimitiveModels()) {
                    meshes.add(new BridgedMesh(primitiveModel, transform));
                }
            }
        }
        
        return new PreparedModel(meshes, metadata);
    }
}
