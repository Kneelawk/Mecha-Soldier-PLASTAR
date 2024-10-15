package com.github.plastar.data;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.function.BiConsumer;

import com.github.plastar.Constants;
import com.google.common.collect.ImmutableMap;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import net.minecraft.Util;
import net.minecraft.core.Holder;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.ReloadableServerRegistries;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;

/**
 * A record holding all the data for a specific built instance of a Mecha Soldier kit.
 *
 * @param parts The parts used to build this mecha.
 */
public record Mecha(Map<MechaSection, MechaPart> parts) {
    public static final Codec<Mecha> CODEC =
        RecordCodecBuilder.create(i -> i.group(
                Codec.unboundedMap(MechaSection.CODEC, MechaPart.CODEC).fieldOf("parts").forGetter(Mecha::parts))
            .apply(i, Mecha::new));

    public static final StreamCodec<RegistryFriendlyByteBuf, Mecha> STREAM_CODEC = StreamCodec.composite(
        ByteBufCodecs.map(HashMap::new, MechaSection.STREAM_CODEC, MechaPart.STREAM_CODEC), Mecha::parts,
        Mecha::new);

    public static final Mecha DEFAULT = new Mecha(Util.make(ImmutableMap.<MechaSection, MechaPart>builder(), builder -> {
        var sections = Arrays.asList(MechaSection.HEAD, MechaSection.TORSO, MechaSection.LEFT_ARM, MechaSection.RIGHT_ARM, MechaSection.LEFT_LEG, MechaSection.RIGHT_LEG);
        for (var section : sections) {
            builder.put(section, new MechaPart(
                ResourceKey.create(PRegistries.PART, Constants.rl(section.getSerializedName())), 
                new PartMaterial(Optional.empty()), 
                ResourceKey.create(PRegistries.PATTERN, Constants.rl("striped")), 
                ResourceKey.create(PRegistries.PALETTE, Constants.rl("a"))));
        }
    }).build());

    public void forEachAttributeModifier(ReloadableServerRegistries.Holder registries, BiConsumer<Holder<Attribute>, AttributeModifier> consumer) {
        parts.forEach((section, part) -> part.forEachAttributeModifier(registries, consumer));
    }
}
