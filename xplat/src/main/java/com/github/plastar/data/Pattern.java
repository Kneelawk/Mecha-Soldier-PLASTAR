package com.github.plastar.data;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Stream;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import net.minecraft.core.Holder;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.ReloadableServerRegistries;

public record Pattern(ResourceLocation texture, Set<MechaSection> sections) {
    public static final Codec<Pattern> CODEC = RecordCodecBuilder.create(instance -> instance.group(
        ResourceLocation.CODEC.fieldOf("texture").forGetter(Pattern::texture),
        MechaSection.CODEC.listOf().xmap(Set::copyOf, List::copyOf).fieldOf("sections").forGetter(Pattern::sections)
    ).apply(instance, Pattern::new));
    public static final StreamCodec<FriendlyByteBuf, Pattern> STREAM_CODEC = StreamCodec.composite(
        ResourceLocation.STREAM_CODEC, Pattern::texture,
        MechaSection.STREAM_CODEC.apply(ByteBufCodecs.collection(HashSet::new)), Pattern::sections,
        Pattern::new
    );

    public Pattern {
        sections = Collections.unmodifiableSet(sections);
    }
    
    public static Stream<Holder.Reference<Pattern>> findPatterns(MechaSection section, ReloadableServerRegistries.Holder registries) {
        return registries.get()
            .registryOrThrow(PRegistries.PATTERN)
            .holders()
            .filter(holder -> holder.value().sections().contains(section));
    }
}
