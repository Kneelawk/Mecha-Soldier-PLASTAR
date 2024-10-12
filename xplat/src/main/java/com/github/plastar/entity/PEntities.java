package com.github.plastar.entity;

import com.github.plastar.PLASTARMod;

import com.github.plastar.data.Mecha;

import net.minecraft.core.registries.Registries;
import net.minecraft.network.syncher.EntityDataSerializer;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;

public class PEntities {
    public static EntityType<MechaEntity> MECHA_ENTITY =
        EntityType.Builder.of(MechaEntity::new, MobCategory.CREATURE).build("mecha");

    public static final EntityDataSerializer<Mecha> MECHA_DATA_SERIALIZER =
        EntityDataSerializer.forValueType(Mecha.STREAM_CODEC);

    static {
        EntityDataSerializers.registerSerializer(MECHA_DATA_SERIALIZER);
    }

    public static void register() {
        register("mecha", MECHA_ENTITY);
    }

    private static <T extends Entity> void register(String key, EntityType<T> entityType) {
        PLASTARMod.REGISTRARS.get(Registries.ENTITY_TYPE).register(key, () -> entityType);
    }
}
