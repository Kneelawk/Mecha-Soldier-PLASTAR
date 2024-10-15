package com.github.plastar.data;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import net.minecraft.core.HolderSet;
import net.minecraft.core.RegistryCodecs;

/**
 * The definition of a mecha part. Loaded exclusively on the server.
 * The id of the part definition is used as the model id on the client. 
 * @param supportedPatterns A holder set of supported patterns. Generally points to a tag.
 * @param section The section this part belongs to.
 */
public record PartDefinition(HolderSet<Pattern> supportedPatterns, MechaSection section) {
    public static final Codec<PartDefinition> CODEC = RecordCodecBuilder.create(instance -> instance.group(
        RegistryCodecs.homogeneousList(PRegistries.PATTERN).fieldOf("supported_patterns").forGetter(PartDefinition::supportedPatterns),
        MechaSection.CODEC.fieldOf("sections").forGetter(PartDefinition::section)
    ).apply(instance, PartDefinition::new));
}
