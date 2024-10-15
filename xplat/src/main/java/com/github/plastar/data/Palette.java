package com.github.plastar.data;

import java.util.stream.Stream;

import com.mojang.serialization.Codec;

import net.minecraft.core.Holder;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.server.ReloadableServerRegistries;

/**
 * A color palette that can be applied to any part. 
 * Loaded into a reloadable registry on the server and synced into {@link com.github.plastar.client.ClientPatternManager ClientPatternManager}.
 * @param textureSuffix The suffix this palette applies to the pattern texture id
 */
public record Palette(String textureSuffix) {
    public static final Codec<Palette> CODEC = Codec.string(1, 32)
        .fieldOf("texture_suffix")
        .codec()
        .xmap(Palette::new, Palette::textureSuffix);
    public static final StreamCodec<FriendlyByteBuf, Palette> STREAM_CODEC = StreamCodec.composite(
        ByteBufCodecs.stringUtf8(32), Palette::textureSuffix,
        Palette::new
    );

    public static Stream<Holder.Reference<Palette>> stream(ReloadableServerRegistries.Holder registries) {
        return registries.get()
            .registryOrThrow(PRegistries.PALETTE)
            .holders();
    }
}
