package com.github.plastar.data;

import java.util.Optional;
import java.util.function.BiConsumer;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import net.minecraft.core.Holder;
import net.minecraft.core.HolderLookup;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;

/**
 * A part of a mecha, consisting of its type, material and paintjob
 * @param definition The resource key to the definition of this part
 * @param material The material this part is made of
 * @param pattern The resource key to the paint pattern applied to this part
 * @param palette The resource key to the color palette of this part
 */
public record MechaPart(ResourceKey<PartDefinition> definition, Optional<ResourceKey<Additive>> material, ResourceKey<Pattern> pattern, ResourceKey<Palette> palette) {
    public static final Codec<MechaPart> CODEC =
        RecordCodecBuilder.create(i -> i.group(
            ResourceKey.codec(PRegistries.PART).fieldOf("part").forGetter(MechaPart::definition),
            ResourceKey.codec(PRegistries.ADDITIVE).optionalFieldOf("material").forGetter(MechaPart::material),
            ResourceKey.codec(PRegistries.PATTERN).fieldOf("pattern").forGetter(MechaPart::pattern),
            ResourceKey.codec(PRegistries.PALETTE).fieldOf("palette").forGetter(MechaPart::palette)
        ).apply(i, MechaPart::new));

    public static final StreamCodec<FriendlyByteBuf, MechaPart> STREAM_CODEC = StreamCodec.composite(
        ResourceKey.streamCodec(PRegistries.PART), MechaPart::definition,
        ByteBufCodecs.optional(ResourceKey.streamCodec(PRegistries.ADDITIVE)), MechaPart::material,
        ResourceKey.streamCodec(PRegistries.PATTERN), MechaPart::pattern,
        ResourceKey.streamCodec(PRegistries.PALETTE), MechaPart::palette,
        MechaPart::new);

    public void forEachAttributeModifier(HolderLookup.Provider registries, BiConsumer<Holder<Attribute>, AttributeModifier> consumer) {
        registries.lookup(PRegistries.PART)
            .flatMap(registry -> registry.get(definition))
            .map(Holder.Reference::value)
            .ifPresent(definition -> {
                definition.modifiers().forEach(modifier -> consumer.accept(modifier.attribute(), modifier.modifier()));
                forEachAttributeModifier(material, registries, definition.section(), consumer);
            });
    }

    @SuppressWarnings("OptionalUsedAsFieldOrParameterType")
    public void forEachAttributeModifier(Optional<ResourceKey<Additive>> material, HolderLookup.Provider registries, MechaSection section, BiConsumer<Holder<Attribute>, AttributeModifier> consumer) {
        registries.lookup(PRegistries.ADDITIVE)
            .flatMap(it -> material.flatMap(it::get))
            .filter(Holder::isBound)
            .map(Holder::value)
            .ifPresent(additive -> {
                for (var modifier : additive.modifiers()) {
                    var newModifier = new AttributeModifier(modifier.modifier().id().withSuffix("_" + section.getSerializedName()),
                        modifier.modifier().amount(),
                        modifier.modifier().operation());
                    consumer.accept(modifier.attribute(), newModifier);
                }
            });
    }
}
