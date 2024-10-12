package com.github.plastar.data;

import com.mojang.serialization.Codec;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import io.netty.buffer.ByteBuf;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.ComponentSerialization;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

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

    public static final Mecha DEFAULT = new Mecha(Component.empty(), Collections.emptyMap());
}
