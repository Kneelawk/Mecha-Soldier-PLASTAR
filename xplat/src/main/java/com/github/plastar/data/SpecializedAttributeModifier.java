package com.github.plastar.data;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import net.minecraft.core.Holder;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;

/**
 * A modifier to a specific attribute
 *
 * @param attribute The attribute to modify
 * @param modifier  The modifier to apply
 */
public record SpecializedAttributeModifier(Holder<Attribute> attribute, AttributeModifier modifier) {
    public static final Codec<SpecializedAttributeModifier> CODEC =
        RecordCodecBuilder.create(instance -> instance.group(
            Attribute.CODEC.fieldOf("attribute").forGetter(SpecializedAttributeModifier::attribute),
            AttributeModifier.MAP_CODEC.forGetter(SpecializedAttributeModifier::modifier)
        ).apply(instance, SpecializedAttributeModifier::new));
}
