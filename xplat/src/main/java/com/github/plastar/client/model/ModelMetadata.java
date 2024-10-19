package com.github.plastar.client.model;

import com.google.gson.JsonObject;

import com.mojang.serialization.Codec;
import com.mojang.serialization.JsonOps;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import net.minecraft.server.packs.metadata.MetadataSectionSerializer;
import net.minecraft.world.phys.Vec3;

public record ModelMetadata(Vec3 pivot) {
    private static final Codec<ModelMetadata> CODEC = RecordCodecBuilder.create(instance -> instance.group(
        Vec3.CODEC.optionalFieldOf("pivot", Vec3.ZERO).forGetter(ModelMetadata::pivot)
    ).apply(instance, ModelMetadata::new));
    public static final ModelMetadata DEFAULT = new ModelMetadata(Vec3.ZERO);
    
    public static final class Serializer implements MetadataSectionSerializer<ModelMetadata> {
        public static final Serializer INSTANCE = new Serializer();
        
        @Override
        public String getMetadataSectionName() {
            return "model_metadata";
        }

        @Override
        public ModelMetadata fromJson(JsonObject json) {
            return CODEC.parse(JsonOps.INSTANCE, json).getOrThrow();
        }
    }
}
