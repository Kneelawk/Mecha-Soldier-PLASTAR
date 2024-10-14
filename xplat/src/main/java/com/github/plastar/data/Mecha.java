package com.github.plastar.data;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.function.BiConsumer;

import com.google.common.collect.ImmutableMap;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import net.minecraft.Util;
import net.minecraft.core.Holder;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.ComponentSerialization;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.server.ReloadableServerRegistries;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;

/**
 * A record holding all the data for a specific built instance of a Mecha Soldier kit.
 * TODO: color data
 *
 * @param name  The name of this mecha, determined by the player.
 * @param parts The parts used to build this mecha.
 */
public record Mecha(Component name, Map<MechaSection, MechaPart> parts) {
    public static final Codec<Mecha> CODEC =
        RecordCodecBuilder.create(i -> i.group(
                ComponentSerialization.CODEC.fieldOf("name").forGetter(Mecha::name),
                Codec.unboundedMap(MechaSection.CODEC, MechaPart.CODEC).fieldOf("parts").forGetter(Mecha::parts))
            .apply(i, Mecha::new));

    public static final StreamCodec<RegistryFriendlyByteBuf, Mecha> STREAM_CODEC = StreamCodec.composite(
        ComponentSerialization.STREAM_CODEC, Mecha::name,
        ByteBufCodecs.map(HashMap::new, MechaSection.STREAM_CODEC, MechaPart.STREAM_CODEC), Mecha::parts,
        Mecha::new);

    public static final Mecha DEFAULT = new Mecha(Component.empty(), Util.make(ImmutableMap.<MechaSection, MechaPart>builder(), builder -> {
        var sections = Arrays.asList(MechaSection.HEAD, MechaSection.TORSO, MechaSection.LEFT_ARM, MechaSection.RIGHT_ARM, MechaSection.LEFT_LEG, MechaSection.RIGHT_LEG);
        for (var section : sections) {
            builder.put(section, new MechaPart(section, new PartMaterial(Optional.empty()), "striped", "a"));
        }
    }).build());

    public void forEachAttributeModifier(ReloadableServerRegistries.Holder registries, BiConsumer<Holder<Attribute>, AttributeModifier> consumer) {
        parts.forEach((section, part) -> part.material().forEachAttributeModifier(registries, section, consumer));
    }
}
