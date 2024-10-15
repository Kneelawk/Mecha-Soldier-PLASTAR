package com.github.plastar.item;

import java.util.function.Supplier;

import com.github.plastar.PLASTARMod;
import com.github.plastar.data.MechaPart;
import com.github.plastar.data.PartMaterial;

import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.registries.Registries;

public class PComponents {
    public static Supplier<DataComponentType<MechaPart>> MECHA_PART = register("mecha_part", () -> DataComponentType.<MechaPart>builder().persistent(MechaPart.CODEC).networkSynchronized(MechaPart.STREAM_CODEC).build());
    public static Supplier<DataComponentType<PartMaterial>> PART_MATERIAL = register("part_material", () -> DataComponentType.<PartMaterial>builder().persistent(PartMaterial.CODEC).networkSynchronized(PartMaterial.STREAM_CODEC).build());

    public static void register() {

    }

    private static <T> Supplier<DataComponentType<T>> register(String key, Supplier<DataComponentType<T>> componentType) {
        return PLASTARMod.REGISTRARS.get(Registries.DATA_COMPONENT_TYPE).register(key, componentType);
    }
}
