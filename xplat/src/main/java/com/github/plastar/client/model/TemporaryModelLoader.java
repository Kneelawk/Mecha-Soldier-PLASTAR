package com.github.plastar.client.model;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Map;

import de.javagl.jgltf.model.GltfModels;
import de.javagl.jgltf.model.io.GltfAsset;
import de.javagl.jgltf.model.io.GltfAssetReader;
import de.javagl.jgltf.model.io.v2.GltfAssetV2;
import dev.engine_room.flywheel.api.model.Model;

import net.minecraft.resources.ResourceLocation;

public class TemporaryModelLoader {
    public static final Model MODEL;
    
    static {
        try (var in = TemporaryModelLoader.class.getClassLoader().getResourceAsStream("test model.gltf")) {
            if (in == null) throw new FileNotFoundException("Couldn't find test model");
            var gltfAsset = new GltfAssetReader().readWithoutReferences(in);
            blockbenchBugWorkaround(gltfAsset);
            MODEL = ModelConverter.convert(GltfModels.create(gltfAsset), Map.of(
                "texture a", ResourceLocation.withDefaultNamespace("textures/block/dirt.png"), 
                "texture b", ResourceLocation.withDefaultNamespace("textures/block/stone.png")));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static void blockbenchBugWorkaround(GltfAsset gltfAsset) {
        // Set the uri of images missing one.
        // Blockbench doesn't correctly set the uri, and we don't care about it,
        // but the model parser does, so we set a bogus value as a workaround
        if (gltfAsset instanceof GltfAssetV2 v2) {
            for (var texture : v2.getGltf().getTextures()) {
                var image = v2.getGltf().getImages().get(texture.getSource());
                if (image.getUri() == null && image.getBufferView() == null) {
                    image.setUri("img_fix_hack:" + texture.getName());
                }
            }
        }
    }
}
