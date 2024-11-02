package com.github.plastar.data;

import java.util.Optional;
import java.util.function.BiConsumer;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import net.minecraft.core.Holder;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.RegistryFixedCodec;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;

/**
 * A part of a mecha, consisting of its type, material and paintjob
 * @param definition The resource key to the definition of this part
 * @param material The material this part is made of
 * @param pattern The resource key to the paint pattern applied to this part
 * @param palette The resource key to the color palette of this part
 */
public record MechaPart(Holder<PartDefinition> definition, Optional<Holder<Additive>> material, Holder<Pattern> pattern, Holder<Palette> palette) {
    public static final Codec<MechaPart> CODEC =
        RecordCodecBuilder.create(i -> i.group(
            RegistryFixedCodec.create(PRegistries.PART).fieldOf("part").forGetter(MechaPart::definition),
            RegistryFixedCodec.create(PRegistries.ADDITIVE).optionalFieldOf("material").forGetter(MechaPart::material),
            RegistryFixedCodec.create(PRegistries.PATTERN).fieldOf("pattern").forGetter(MechaPart::pattern),
            RegistryFixedCodec.create(PRegistries.PALETTE).fieldOf("palette").forGetter(MechaPart::palette)
        ).apply(i, MechaPart::new));

    public static final StreamCodec<RegistryFriendlyByteBuf, MechaPart> STREAM_CODEC = StreamCodec.composite(
        ByteBufCodecs.holderRegistry(PRegistries.PART), MechaPart::definition,
        ByteBufCodecs.optional(ByteBufCodecs.holderRegistry(PRegistries.ADDITIVE)), MechaPart::material,
        ByteBufCodecs.holderRegistry(PRegistries.PATTERN), MechaPart::pattern,
        ByteBufCodecs.holderRegistry(PRegistries.PALETTE), MechaPart::palette,
        MechaPart::new);

    public void forEachAttributeModifier(BiConsumer<Holder<Attribute>, AttributeModifier> consumer) {
        definition.value().modifiers().forEach(modifier -> consumer.accept(modifier.attribute(), modifier.modifier()));
        material.ifPresent(material -> {
            material.value().modifiers().forEach(modifier -> {
                var newModifier = new AttributeModifier(modifier.modifier().id().withSuffix("_" + definition.value().section().getSerializedName()),
                    modifier.modifier().amount(),
                    modifier.modifier().operation());
                consumer.accept(modifier.attribute(), newModifier);
            });
        });
    }
}
