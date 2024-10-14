package com.github.plastar.data;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;

/**
 * An individual part of a model kit.
 */
public record MechaPart(MechaSection section, PartMaterial material, String pattern, String palette) {
    public static final Codec<MechaPart> CODEC =
        RecordCodecBuilder.create(i -> i.group(
                MechaSection.CODEC.fieldOf("section").forGetter(MechaPart::section),
                PartMaterial.CODEC.fieldOf("material").forGetter(MechaPart::material),
                Codec.STRING.fieldOf("pattern").forGetter(MechaPart::pattern),
                Codec.STRING.fieldOf("palette").forGetter(MechaPart::palette)
            ).apply(i, MechaPart::new));

    public static final StreamCodec<FriendlyByteBuf, MechaPart> STREAM_CODEC = StreamCodec.composite(
        MechaSection.STREAM_CODEC, MechaPart::section,
        PartMaterial.STREAM_CODEC, MechaPart::material,
        ByteBufCodecs.STRING_UTF8, MechaPart::pattern,
        ByteBufCodecs.STRING_UTF8, MechaPart::palette,
        MechaPart::new);
}
