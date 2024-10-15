package com.github.plastar.data;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.ResourceKey;

/**
 * A part of a mecha, consisting of its type, material and paintjob
 * @param definition The resource key to the definition of this part
 * @param material The material this part is made of
 * @param pattern The resource key to the paint pattern applied to this part
 * @param palette The resource key to the color palette of this part
 */
public record MechaPart(ResourceKey<PartDefinition> definition, PartMaterial material, ResourceKey<Pattern> pattern, ResourceKey<Palette> palette) {
    public static final Codec<MechaPart> CODEC =
        RecordCodecBuilder.create(i -> i.group(
            ResourceKey.codec(PRegistries.PART).fieldOf("part").forGetter(MechaPart::definition),
            PartMaterial.CODEC.fieldOf("material").forGetter(MechaPart::material),
            ResourceKey.codec(PRegistries.PATTERN).fieldOf("pattern").forGetter(MechaPart::pattern),
            ResourceKey.codec(PRegistries.PALETTE).fieldOf("palette").forGetter(MechaPart::palette)
        ).apply(i, MechaPart::new));

    public static final StreamCodec<FriendlyByteBuf, MechaPart> STREAM_CODEC = StreamCodec.composite(
        ResourceKey.streamCodec(PRegistries.PART), MechaPart::definition,
        PartMaterial.STREAM_CODEC, MechaPart::material,
        ResourceKey.streamCodec(PRegistries.PATTERN), MechaPart::pattern,
        ResourceKey.streamCodec(PRegistries.PALETTE), MechaPart::palette,
        MechaPart::new);
}
