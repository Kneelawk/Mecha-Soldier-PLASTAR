package com.github.plastar.data;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import net.minecraft.core.HolderSet;
import net.minecraft.core.RegistryCodecs;

public record PartDefinition(HolderSet<Pattern> supportedPatterns, MechaSection section) {
    public static final Codec<PartDefinition> CODEC = RecordCodecBuilder.create(instance -> instance.group(
        RegistryCodecs.homogeneousList(PRegistries.PATTERN).fieldOf("supported_patterns").forGetter(PartDefinition::supportedPatterns),
        MechaSection.CODEC.fieldOf("sections").forGetter(PartDefinition::section)
    ).apply(instance, PartDefinition::new));
}
