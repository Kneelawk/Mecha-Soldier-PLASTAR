package com.github.plastar.data;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.function.BiConsumer;

import com.github.plastar.registry.RegistryUtil;
import com.google.common.collect.ImmutableMap;

import org.jetbrains.annotations.Nullable;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import net.minecraft.core.Holder;
import net.minecraft.core.HolderLookup;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.ResourceKey;
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
    
    public static Mecha getDefault(HolderLookup.Provider registries) {
        var partRegistry = registries.lookupOrThrow(PRegistries.PART);
        var palette = RegistryUtil.getPreferred(Palettes.UNPAINTED, registries.lookupOrThrow(PRegistries.PALETTE));
        
        var preferredParts = Map.of(
            MechaSection.HEAD, Parts.EMMA_HEAD,
            MechaSection.TORSO, Parts.EMMA_TORSO,
            MechaSection.LEFT_ARM, Parts.EMMA_LEFT_ARM,
            MechaSection.RIGHT_ARM, Parts.EMMA_RIGHT_ARM,
            MechaSection.LEFT_LEG, Parts.EMMA_LEFT_LEG,
            MechaSection.RIGHT_LEG, Parts.EMMA_RIGHT_LEG
        );
        var builder = ImmutableMap.<MechaSection, MechaPart>builder();
        preferredParts.forEach((section, preferred) -> {
            var definition = getPart(section, preferred, partRegistry);
            // We don't want to do this, but if there are literally no parts, then we just have to leave the slot empty
            if (definition == null) return;
            var pattern = definition.value().defaultPattern();
            builder.put(section, new MechaPart(definition, Optional.empty(), pattern, palette));
        });
        return new Mecha(builder.build());
    }
    
    @Nullable
    private static Holder<PartDefinition> getPart(MechaSection section, ResourceKey<PartDefinition> preferred, HolderLookup.RegistryLookup<PartDefinition> registry) {
        var entry = registry.get(preferred).filter(holder -> holder.value().section() == section);
        if (entry.isPresent()) {
            return entry.get();
        }
        
        for (var holder : registry.listElements().toList()) {
            if (holder.value().section() == section) {
                return holder;
            }
        }
        
        return null;
    }

    public void forEachAttributeModifier(BiConsumer<Holder<Attribute>, AttributeModifier> consumer) {
        parts.forEach((section, part) -> part.forEachAttributeModifier(consumer));
    }
}
