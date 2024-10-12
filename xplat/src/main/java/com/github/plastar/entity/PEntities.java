package com.github.plastar.entity;

import com.github.plastar.PLASTARMod;

import net.minecraft.core.registries.Registries;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;

public class PEntities {
    public static EntityType<MechaEntity> MECHA_ENTITY = EntityType.Builder.of(MechaEntity::new, MobCategory.CREATURE).build("mecha");

    public static void register() {
        register("mecha", MECHA_ENTITY);
    }

    private static <T extends Entity> void register(String key, EntityType<T> entityType) {
        PLASTARMod.REGISTRARS.get(Registries.ENTITY_TYPE).register(key, () -> entityType);
    }
}
