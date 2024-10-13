package com.github.plastar.data;

import java.util.List;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;

/**
 * An individual part of a model kit.
 */
public record MechaPart(MechaSection section, List<AttributeModifier> stats, String pattern, String palette) {
    public static final Codec<MechaPart> CODEC =
        RecordCodecBuilder.create(i -> i.group(
                MechaSection.CODEC.fieldOf("section").forGetter(MechaPart::section),
                AttributeModifier.CODEC.listOf().fieldOf("stats").forGetter(MechaPart::stats),
                Codec.STRING.fieldOf("pattern").forGetter(MechaPart::pattern),
                Codec.STRING.fieldOf("palette").forGetter(MechaPart::palette)
            ).apply(i, MechaPart::new));

    public static final StreamCodec<FriendlyByteBuf, MechaPart> STREAM_CODEC = StreamCodec.composite(
        MechaSection.STREAM_CODEC, MechaPart::section,
        AttributeModifier.STREAM_CODEC.apply(ByteBufCodecs.list()), MechaPart::stats,
        ByteBufCodecs.STRING_UTF8, MechaPart::pattern,
        ByteBufCodecs.STRING_UTF8, MechaPart::palette,
        MechaPart::new);
}
