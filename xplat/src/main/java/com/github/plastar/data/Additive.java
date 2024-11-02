package com.github.plastar.data;

import java.util.List;
import java.util.Optional;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import net.minecraft.core.Holder;
import net.minecraft.core.HolderLookup;
import net.minecraft.resources.RegistryFixedCodec;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;

/**
 * A ingredient that can be mixed into the plastic of a part
 *
 * @param ingredient The recipe ingredient that leads to this additive
 * @param modifiers  A list of attribute modifiers this additive causes
 */
public record Additive(Ingredient ingredient, List<SpecializedAttributeModifier> modifiers, Holder<Palette> defaultPalette) {
    public static final Codec<Additive> CODEC = RecordCodecBuilder.create(instance -> instance.group(
        Ingredient.CODEC_NONEMPTY.fieldOf("ingredient").forGetter(Additive::ingredient),
        SpecializedAttributeModifier.CODEC.listOf().fieldOf("modifiers").forGetter(Additive::modifiers),
        RegistryFixedCodec.create(PRegistries.PALETTE).fieldOf("default_palette").forGetter(Additive::defaultPalette)
    ).apply(instance, Additive::new));

    public static boolean isAdditive(ItemStack stack, HolderLookup.Provider lookup) {
        HolderLookup.RegistryLookup<Additive> registry = lookup.lookupOrThrow(PRegistries.ADDITIVE);
        return registry.listElements().map(Holder.Reference::value).anyMatch(a -> a.ingredient.test(stack));
    }

    public static Optional<Holder.Reference<Additive>> getAdditive(ItemStack stack, HolderLookup.Provider lookup) {
        HolderLookup.RegistryLookup<Additive> registry = lookup.lookupOrThrow(PRegistries.ADDITIVE);
        return registry.listElements().filter(holder -> holder.value().ingredient.test(stack)).findFirst();
    }
}
