package com.github.plastar.data.program;

import java.util.Optional;

import io.netty.buffer.ByteBuf;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;

public record MechaProgram(Optional<Mecha2dArea> area) {
    public static final Codec<MechaProgram> CODEC = RecordCodecBuilder.create(instance -> instance.group(
        Mecha2dArea.CODEC.optionalFieldOf("area").forGetter(MechaProgram::area)
    ).apply(instance, MechaProgram::new));
    public static final StreamCodec<ByteBuf, MechaProgram> STREAM_CODEC = StreamCodec.composite(
        Mecha2dArea.STREAM_CODEC.apply(ByteBufCodecs::optional), MechaProgram::area,
        MechaProgram::new
    );
    public static final StreamCodec<ByteBuf, Optional<MechaProgram>> OPTIONAL_STREAM_CODEC =
        STREAM_CODEC.apply(ByteBufCodecs::optional);
    public static final MechaProgram DEFAULT = new MechaProgram(Optional.empty());
}
