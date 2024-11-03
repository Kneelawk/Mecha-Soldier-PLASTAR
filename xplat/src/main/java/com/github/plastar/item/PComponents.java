package com.github.plastar.item;

import java.util.function.Supplier;

import com.github.plastar.PLASTARMod;
import com.github.plastar.data.MechaPart;
import com.github.plastar.data.program.Mecha2dArea;
import com.github.plastar.data.program.MechaProgram;

import com.mojang.serialization.Codec;

import net.minecraft.core.BlockPos;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.codec.ByteBufCodecs;

public class PComponents {
    public static Supplier<DataComponentType<MechaPart>> MECHA_PART = register("mecha_part", () -> DataComponentType.<MechaPart>builder().persistent(MechaPart.CODEC).networkSynchronized(MechaPart.STREAM_CODEC).build());
    public static Supplier<DataComponentType<Integer>> PART_SNIPS = register("part_snips", () -> DataComponentType.<Integer>builder().persistent(Codec.intRange(1, 8)).networkSynchronized(ByteBufCodecs.fromCodec(Codec.intRange(1, 8))).build());
    public static Supplier<DataComponentType<MechaProgram>> MECHA_PROGRAM = register("mecha_program", () -> DataComponentType.<MechaProgram>builder().persistent(MechaProgram.CODEC).networkSynchronized(MechaProgram.STREAM_CODEC).build());
    public static Supplier<DataComponentType<Mecha2dArea>> MECHA_2D_AREA = register("mecha_area", () -> DataComponentType.<Mecha2dArea>builder().persistent(Mecha2dArea.CODEC).networkSynchronized(Mecha2dArea.STREAM_CODEC).build());
    public static Supplier<DataComponentType<BlockPos>> SELECTED_POSITION = register("selected_position", () -> DataComponentType.<BlockPos>builder().persistent(BlockPos.CODEC).networkSynchronized(BlockPos.STREAM_CODEC).build());

    public static void register() {

    }

    private static <T> Supplier<DataComponentType<T>> register(String key, Supplier<DataComponentType<T>> componentType) {
        return PLASTARMod.REGISTRARS.get(Registries.DATA_COMPONENT_TYPE).register(key, componentType);
    }
}
