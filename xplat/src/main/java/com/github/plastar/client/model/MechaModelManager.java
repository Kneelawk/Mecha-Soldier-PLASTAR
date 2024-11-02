package com.github.plastar.client.model;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import com.github.plastar.data.PartDefinition;
import com.google.gson.JsonParseException;

import de.javagl.jgltf.model.GltfModels;
import de.javagl.jgltf.model.io.GltfAsset;
import de.javagl.jgltf.model.io.GltfAssetReader;
import de.javagl.jgltf.model.io.v2.GltfAssetV2;

import net.minecraft.resources.FileToIdConverter;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.SimplePreparableReloadListener;
import net.minecraft.util.profiling.ProfilerFiller;

public class MechaModelManager extends SimplePreparableReloadListener<Map<ResourceLocation, PreparedModel>> {
    private static final FileToIdConverter FILE_TO_ID_CONVERTER = new FileToIdConverter("plastar_models", ".gltf");
    private static final GltfAssetReader ASSET_READER = new GltfAssetReader();
    public static final MechaModelManager INSTANCE = new MechaModelManager();
    
    static {
        ASSET_READER.setJsonErrorConsumer(error -> {
            throw new JsonParseException("Json error in plastar model: " + error.getMessage() + " (at " + error.getJsonPathString() + ")", error.getThrowable());
        });
    }
    
    private Map<ResourceLocation, PreparedModel> models = Map.of();

    private MechaModelManager() {
    }
    
    @Override
    protected Map<ResourceLocation, PreparedModel> prepare(ResourceManager resourceManager, ProfilerFiller profiler) {
        var result = new HashMap<ResourceLocation, PreparedModel>();
        
        for (var entry : FILE_TO_ID_CONVERTER.listMatchingResources(resourceManager).entrySet()) {
            try (var in = entry.getValue().open()) {
                var asset = ASSET_READER.readWithoutReferences(in);
                blockbenchBugWorkaround(asset);
                var metadata = entry.getValue()
                    .metadata()
                    .getSection(ModelMetadata.Serializer.INSTANCE)
                    .orElse(ModelMetadata.DEFAULT);
                var prepared = ModelConverter.convert(GltfModels.create(asset), metadata);
                result.put(FILE_TO_ID_CONVERTER.fileToId(entry.getKey()), prepared);
            } catch (IOException | JsonParseException e) {
                throw new RuntimeException("Failed to load plastar model at " + entry.getKey(), e);
            }
        }
        
        return result;
    }

    @Override
    protected void apply(Map<ResourceLocation, PreparedModel> object, ResourceManager resourceManager,
                         ProfilerFiller profiler) {
        models = object;
    }

    public Optional<PreparedModel> getModel(ResourceKey<PartDefinition> location) {
        return Optional.ofNullable(models.get(location.location()));
    }

    private static void blockbenchBugWorkaround(GltfAsset gltfAsset) {
        // Set the uri of images missing one.
        // Blockbench doesn't correctly set the uri, and we don't care about it,
        // but the model parser does, so we set a bogus value as a workaround
        if (gltfAsset instanceof GltfAssetV2 v2) {
            var textures = v2.getGltf().getTextures();
            if (textures == null) return;
            for (var texture : textures) {
                var image = v2.getGltf().getImages().get(texture.getSource());
                if (image.getUri() == null && image.getBufferView() == null) {
                    image.setUri("img_fix_hack:" + texture.getName());
                }
            }
        }
    }
}
