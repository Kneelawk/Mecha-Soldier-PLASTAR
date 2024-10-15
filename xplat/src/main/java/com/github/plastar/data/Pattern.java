package com.github.plastar.data;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.ResourceLocation;

/**
 * A paint pattern that can be applied to a mecha part. Not all parts accept all patterns.
 * Loaded into a reloadable registry on the server and synced into {@link com.github.plastar.client.ClientPatternManager ClientPatternManager}.
 * @param texture The base id of the texture this pattern uses. The suffix from the palette is appended to this.
 */
public record Pattern(ResourceLocation texture) {
    public static final Codec<Pattern> CODEC = RecordCodecBuilder.create(instance -> instance.group(
        ResourceLocation.CODEC.fieldOf("texture").forGetter(Pattern::texture)
    ).apply(instance, Pattern::new));
    public static final StreamCodec<FriendlyByteBuf, Pattern> STREAM_CODEC = StreamCodec.composite(
        ResourceLocation.STREAM_CODEC, Pattern::texture,
        Pattern::new
    );
}
