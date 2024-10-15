package com.github.plastar.data;

import com.github.plastar.PLASTARMod;

import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.registries.Registries;

import java.util.function.Supplier;

public class PComponents {
    public static Supplier<DataComponentType<PartMaterial>> PART_MATERIAL = register("part_material", () -> DataComponentType.<PartMaterial>builder().persistent(PartMaterial.CODEC).networkSynchronized(PartMaterial.STREAM_CODEC).build());

    public static void register() {

    }

    private static <T> Supplier<DataComponentType<T>> register(String key, Supplier<DataComponentType<T>> componentType) {
        return PLASTARMod.REGISTRARS.get(Registries.DATA_COMPONENT_TYPE).register(key, componentType);
    }
}
