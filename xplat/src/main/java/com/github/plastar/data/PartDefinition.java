package com.github.plastar.data;

import java.util.List;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import net.minecraft.core.Holder;
import net.minecraft.core.HolderSet;
import net.minecraft.core.RegistryCodecs;
import net.minecraft.resources.RegistryFixedCodec;

/**
 * The definition of a mecha part.
 * @param supportedPatterns A holder set of supported patterns. Generally points to a tag.
 * @param defaultPattern The default pattern applied to this part.
 * @param section The section this part belongs to.
 * @param modifiers A list of attribute modifiers this part adds.
 */
public record PartDefinition(HolderSet<Pattern> supportedPatterns, Holder<Pattern> defaultPattern, MechaSection section, List<SpecializedAttributeModifier> modifiers) {
    public static final Codec<PartDefinition> CODEC = RecordCodecBuilder.create(instance -> instance.group(
        RegistryCodecs.homogeneousList(PRegistries.PATTERN).fieldOf("supported_patterns").forGetter(PartDefinition::supportedPatterns),
        RegistryFixedCodec.create(PRegistries.PATTERN).fieldOf("default_pattern").forGetter(PartDefinition::defaultPattern),
        MechaSection.CODEC.fieldOf("section").forGetter(PartDefinition::section),
        SpecializedAttributeModifier.CODEC.listOf().optionalFieldOf("modifiers", List.of()).forGetter(PartDefinition::modifiers)
    ).apply(instance, PartDefinition::new));
}
