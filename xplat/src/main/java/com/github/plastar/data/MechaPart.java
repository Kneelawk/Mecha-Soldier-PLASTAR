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
 * TODO: record instead of interface?
 */
public interface MechaPart {
    Codec<MechaPart> CODEC =
        RecordCodecBuilder.create(i -> i.group(
                MechaSection.CODEC.fieldOf("section").forGetter(MechaPart::getSection),
                AttributeModifier.CODEC.listOf().fieldOf("stats").forGetter(MechaPart::getStats))
            .apply(i, MechaPart::create));

    StreamCodec<FriendlyByteBuf, MechaPart> STREAM_CODEC = StreamCodec.composite(
        MechaSection.STREAM_CODEC, MechaPart::getSection,
        AttributeModifier.STREAM_CODEC.apply(ByteBufCodecs.list()), MechaPart::getStats,
        MechaPart::create);

    MechaSection getSection();

    List<AttributeModifier> getStats();

    static MechaPart create(MechaSection section, List<AttributeModifier> stats) {
        // We'll need to figure out how this gets populated, if we do use a record this'll be much easier
        return null;
    }
}
