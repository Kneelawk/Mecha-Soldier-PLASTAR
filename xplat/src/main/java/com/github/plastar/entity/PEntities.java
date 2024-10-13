package com.github.plastar.entity;

import java.util.function.BiConsumer;
import java.util.function.Supplier;

import com.github.plastar.Constants;
import com.github.plastar.PLASTARMod;
import com.github.plastar.data.Mecha;

import net.minecraft.core.registries.Registries;
import net.minecraft.network.syncher.EntityDataSerializer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;

public class PEntities {
    public static final Supplier<EntityType<MechaEntity>> MECHA_ENTITY = register("mecha", () ->
        EntityType.Builder.of(MechaEntity::new, MobCategory.CREATURE).build("mecha"));

    public static final EntityDataSerializer<Mecha> MECHA_DATA_SERIALIZER =
        EntityDataSerializer.forValueType(Mecha.STREAM_CODEC);

    public static void register() {
    }
    
    public static void registerSerializers(BiConsumer<ResourceLocation, EntityDataSerializer<?>> consumer) {
        consumer.accept(Constants.rl("mecha_data"), MECHA_DATA_SERIALIZER);
    }

    private static <T extends Entity> Supplier<EntityType<T>> register(String key, Supplier<EntityType<T>> entityType) {
        return PLASTARMod.REGISTRARS.get(Registries.ENTITY_TYPE).register(key, entityType);
    }
}
