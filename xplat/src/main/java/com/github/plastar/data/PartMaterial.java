package com.github.plastar.data;

import java.util.Optional;
import java.util.function.BiConsumer;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import net.minecraft.core.Holder;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.ReloadableServerRegistries;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;

public record PartMaterial(Optional<ResourceKey<Additive>> additive) {
    //TODO: this is a regular codec now for use as a data component, should it be a map codec instead?
    public static final Codec<PartMaterial> CODEC =
        RecordCodecBuilder.create(i -> i.group(
            ResourceKey.codec(Additive.REGISTRY_KEY).optionalFieldOf("additive").forGetter(PartMaterial::additive)
        ).apply(i, PartMaterial::new));
    public static final StreamCodec<FriendlyByteBuf, PartMaterial> STREAM_CODEC = StreamCodec.composite(
        ByteBufCodecs.optional(ResourceKey.streamCodec(Additive.REGISTRY_KEY)), PartMaterial::additive,
        PartMaterial::new
    );
    
    public void forEachAttributeModifier(ReloadableServerRegistries.Holder registries, MechaSection section, BiConsumer<Holder<Attribute>, AttributeModifier> consumer) {
        registries.lookup()
            .lookup(Additive.REGISTRY_KEY)
            .flatMap(it -> this.additive.flatMap(it::get))
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
