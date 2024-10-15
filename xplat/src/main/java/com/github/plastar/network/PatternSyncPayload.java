package com.github.plastar.network;

import java.util.HashMap;
import java.util.Map;

import com.github.plastar.Constants;
import com.github.plastar.client.ClientPatternManager;
import com.github.plastar.data.PRegistries;
import com.github.plastar.data.Palette;
import com.github.plastar.data.Pattern;

import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceKey;

import com.kneelawk.knet.api.channel.NoContextPlayChannel;

public record PatternSyncPayload(Map<ResourceKey<Pattern>, Pattern> patterns, Map<ResourceKey<Palette>, Palette> palettes) 
    implements CustomPacketPayload {
    public static final Type<PatternSyncPayload> TYPE = new Type<>(Constants.rl("pattern_sync"));
    public static final NoContextPlayChannel<PatternSyncPayload> CHANNEL = NoContextPlayChannel.ofRegistryCodec(TYPE, StreamCodec.composite(
        ByteBufCodecs.map(HashMap::new, ResourceKey.streamCodec(PRegistries.PATTERN), Pattern.STREAM_CODEC), PatternSyncPayload::patterns,
        ByteBufCodecs.map(HashMap::new, ResourceKey.streamCodec(PRegistries.PALETTE), Palette.STREAM_CODEC), PatternSyncPayload::palettes,
        PatternSyncPayload::new
    )).recvClient((payload, ctx) -> ClientPatternManager.INSTANCE.reload(payload.patterns, payload.palettes));
    
    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
