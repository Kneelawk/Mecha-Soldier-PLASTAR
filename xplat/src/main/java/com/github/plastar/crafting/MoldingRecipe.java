package com.github.plastar.crafting;

import java.util.Optional;

import com.github.plastar.data.Additive;
import com.github.plastar.data.MechaPart;
import com.github.plastar.data.PRegistries;
import com.github.plastar.data.Palettes;
import com.github.plastar.data.PartDefinition;
import com.github.plastar.data.Patterns;
import com.github.plastar.item.PComponents;
import com.github.plastar.item.PItems;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import net.minecraft.core.HolderLookup;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;

public record MoldingRecipe(Ingredient sap, ResourceKey<PartDefinition> result) implements Recipe<MoldingRecipeInput> {
    private static final ItemStack PART_STACK = PItems.MECHA_PART.get().getDefaultInstance();

    @Override
    public boolean matches(MoldingRecipeInput input, Level level) {
        if (level instanceof ServerLevel server) {
            var isValidAdditive = Additive.isAdditive(input.additive(), server.getServer().reloadableRegistries().get());
            return sap.test(input.sap()) && (input.additive().isEmpty() || isValidAdditive);
        }
        return false;
    }

    @Override
    public ItemStack assemble(MoldingRecipeInput input, HolderLookup.Provider registries) {
        ItemStack ret = PART_STACK.copy();
        var additive = Additive.getAdditive(input.additive(), registries);
        if (additive.isPresent()) {
            Additive real = additive.get().value();
            ret.set(PComponents.MECHA_PART.get(), new MechaPart(result, additive.get().unwrapKey(), real.defaultPattern(), real.defaultPalette()));
        } else {
            //TODO: change to monochrome palette when we have the real one
            ret.set(PComponents.MECHA_PART.get(), new MechaPart(result, Optional.empty(), Patterns.CORE, Palettes.A));
        }
        return ret;
    }

    @Override
    public boolean canCraftInDimensions(int width, int height) {
        return width * height >= 2;
    }

    @Override
    public ItemStack getResultItem(HolderLookup.Provider registries) {
        return PART_STACK;
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return PRecipes.MOLDING_SERIALIZER.get();
    }

    @Override
    public RecipeType<?> getType() {
        return PRecipes.MOLDING.get();
    }
    
    public static class Serializer implements RecipeSerializer<MoldingRecipe> {
        private static final MapCodec<MoldingRecipe> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
            Ingredient.CODEC.fieldOf("sap").forGetter(MoldingRecipe::sap),
            ResourceKey.codec(PRegistries.PART).fieldOf("result").forGetter(MoldingRecipe::result)
        ).apply(instance, MoldingRecipe::new));
        private static final StreamCodec<RegistryFriendlyByteBuf, MoldingRecipe> STREAM_CODEC = StreamCodec.composite(
            Ingredient.CONTENTS_STREAM_CODEC, MoldingRecipe::sap,
            ResourceKey.streamCodec(PRegistries.PART), MoldingRecipe::result,
            MoldingRecipe::new
        );
        
        @Override
        public MapCodec<MoldingRecipe> codec() {
            return CODEC;
        }

        @Override
        public StreamCodec<RegistryFriendlyByteBuf, MoldingRecipe> streamCodec() {
            return STREAM_CODEC;
        }
    }
}
