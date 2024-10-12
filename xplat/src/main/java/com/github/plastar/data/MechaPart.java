package com.github.plastar.data;

import com.mojang.serialization.Codec;

import com.mojang.serialization.codecs.RecordCodecBuilder;

import net.minecraft.world.entity.ai.attributes.AttributeModifier;

import java.util.List;

/**
 * An individual part of a model kit.
 * TODO: record instead of interface?
 */
public interface MechaPart {
    public MechaSection getSection();

    public List<AttributeModifier> getStats();

    public static final Codec<MechaPart> CODEC =
        RecordCodecBuilder.create(i -> i.group(MechaSection.CODEC.fieldOf("section").forGetter(MechaPart::getSection),
                AttributeModifier.CODEC.listOf().fieldOf("stats").forGetter(MechaPart::getStats))
            .apply(i, MechaPart::create));

    public static MechaPart create(MechaSection section, List<AttributeModifier> stats) {
        // We'll need to figure out how this gets populated, if we do use a record this'll be much easier
        return null;
    }
}
