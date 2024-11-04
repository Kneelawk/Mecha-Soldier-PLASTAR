package com.github.plastar.data.program;

import io.netty.buffer.ByteBuf;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Vec3i;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.util.Mth;
import net.minecraft.world.phys.Vec3;

import static java.lang.Math.max;
import static java.lang.Math.min;

/**
 * Specifies an area for mechas to operate within.
 *
 * @param x0 inclusive x min.
 * @param z0 inclusive z min.
 * @param x1 inclusive z max.
 * @param z1 inclusive z max.
 */
public record Mecha2dArea(int x0, int z0, int x1, int z1) {
    public static final Codec<Mecha2dArea> CODEC = RecordCodecBuilder.create(instance -> instance.group(
        Codec.INT.fieldOf("x0").forGetter(Mecha2dArea::x0),
        Codec.INT.fieldOf("z0").forGetter(Mecha2dArea::z0),
        Codec.INT.fieldOf("x1").forGetter(Mecha2dArea::x1),
        Codec.INT.fieldOf("z1").forGetter(Mecha2dArea::z1)
    ).apply(instance, Mecha2dArea::sorted));
    public static final StreamCodec<ByteBuf, Mecha2dArea> STREAM_CODEC = StreamCodec.composite(
        ByteBufCodecs.VAR_INT, Mecha2dArea::x0,
        ByteBufCodecs.VAR_INT, Mecha2dArea::z0,
        ByteBufCodecs.VAR_INT, Mecha2dArea::x1,
        ByteBufCodecs.VAR_INT, Mecha2dArea::z1,
        Mecha2dArea::new
    );

    public static Mecha2dArea sorted(int x0, int z0, int x1, int z1) {
        return new Mecha2dArea(min(x0, x1), min(z0, z1), max(x0, x1), max(z0, z1));
    }

    public boolean isWithin(Vec3 vec) {
        return isWithin(Mth.floor(vec.x), Mth.floor(vec.z));
    }

    public boolean isWithin(Vec3i vec) {
        return isWithin(vec.getX(), vec.getZ());
    }

    public boolean isWithin(int x, int z) {
        return x >= x0 && x <= x1 && z >= z0 && z <= z1;
    }

    public Vec3 nearest(Vec3 current) {
        return new Vec3(Math.min(Math.max(current.x, x0), x1), current.y, Math.min(Math.max(current.z, z0), z1));
    }

    public BlockPos nearest(Vec3i current) {
        return new BlockPos(Math.min(Math.max(current.getX(), x0), x1), current.getY(),
            Math.min(Math.max(current.getZ(), z0), z1));
    }
}
