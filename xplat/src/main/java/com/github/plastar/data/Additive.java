package com.github.plastar.data;

import java.util.List;
import java.util.Optional;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import net.minecraft.core.Holder;
import net.minecraft.core.HolderLookup;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;

public record Additive(Ingredient ingredient, List<AdditiveAttributeModifier> modifiers) {
    public static final Codec<Additive> CODEC = RecordCodecBuilder.create(instance -> instance.group(
        Ingredient.CODEC_NONEMPTY.fieldOf("ingredient").forGetter(Additive::ingredient),
        AdditiveAttributeModifier.CODEC.listOf().fieldOf("modifiers").forGetter(Additive::modifiers)
    ).apply(instance, Additive::new));

    public record AdditiveAttributeModifier(Holder<Attribute> attribute, AttributeModifier modifier) {
        public static final Codec<AdditiveAttributeModifier> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            Attribute.CODEC.fieldOf("attribute").forGetter(AdditiveAttributeModifier::attribute),
            AttributeModifier.MAP_CODEC.forGetter(AdditiveAttributeModifier::modifier)
        ).apply(instance, AdditiveAttributeModifier::new));
    }

    public static boolean isAdditive(ItemStack stack, HolderLookup.Provider lookup) {
        HolderLookup.RegistryLookup<Additive> registry = lookup.lookupOrThrow(PRegistries.ADDITIVE);
        return registry.listElements().map(Holder.Reference::value).anyMatch(a -> a.ingredient.test(stack));
    }

    public static Optional<Holder.Reference<Additive>> getAdditive(ItemStack stack, HolderLookup.Provider lookup) {
        HolderLookup.RegistryLookup<Additive> registry = lookup.lookupOrThrow(PRegistries.ADDITIVE);
        return registry.listElements().filter(holder -> holder.value().ingredient.test(stack)).findFirst();
    }
}
