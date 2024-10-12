package com.github.plastar.client.model;

import java.util.ArrayList;
import java.util.Map;

import de.javagl.jgltf.model.GltfModel;
import de.javagl.jgltf.model.v2.MaterialModelV2;
import dev.engine_room.flywheel.api.model.Model;
import dev.engine_room.flywheel.lib.material.SimpleMaterial;
import dev.engine_room.flywheel.lib.model.SimpleModel;

import org.joml.Matrix4f;

import net.minecraft.resources.ResourceLocation;

public class ModelConverter {
    /**
     * Converts a GLTF model to a flywheel model
     * @param model The model to convert
     * @param textureMap A map of texture names in the model to their locations on our atlas
     * @return The converted model
     */
    public static Model convert(GltfModel model, Map<String, ResourceLocation> textureMap) {
        var configuredMeshes = new ArrayList<Model.ConfiguredMesh>();
        for (var nodeModel : model.getNodeModels()) {
            var meshModels = nodeModel.getMeshModels();
            if (meshModels.isEmpty()) continue;
            var transformValues = nodeModel.computeGlobalTransform(new float[16]);
            var transform = new Matrix4f().set(transformValues);

            for (var meshModel : meshModels) {
                for (var primitiveModel : meshModel.getMeshPrimitiveModels()) {
                    var material = SimpleMaterial.builder()
                        .backfaceCulling(false)
                        .mipmap(false)
                        .texture(textureMap.get(((MaterialModelV2)primitiveModel.getMaterialModel()).getBaseColorTexture().getName()))
                        .build();
                    configuredMeshes.add(new Model.ConfiguredMesh(material, new BridgedMesh(primitiveModel, transform)));
                }
            }
        }
        
        return new SimpleModel(configuredMeshes);
    }
}
