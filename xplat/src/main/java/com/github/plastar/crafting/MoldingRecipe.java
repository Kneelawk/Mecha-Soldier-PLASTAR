package com.github.plastar.crafting;

import com.github.plastar.data.Additive;
import com.github.plastar.data.PartMaterial;
import com.github.plastar.item.PComponents;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import net.minecraft.core.Holder;
import net.minecraft.core.HolderLookup;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;

public record MoldingRecipe(Ingredient sap, ItemStack result) implements Recipe<MoldingRecipeInput> {
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
        ItemStack ret = result.copy();
        var additive = Additive.getAdditive(input.additive(), registries);
        ret.set(PComponents.PART_MATERIAL.get(), new PartMaterial(additive.flatMap(Holder::unwrapKey)));
        return ret;
    }

    @Override
    public boolean canCraftInDimensions(int width, int height) {
        return width * height >= 2;
    }

    @Override
    public ItemStack getResultItem(HolderLookup.Provider registries) {
        return result;
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
            ItemStack.CODEC.fieldOf("result").forGetter(MoldingRecipe::result)
        ).apply(instance, MoldingRecipe::new));
        private static final StreamCodec<RegistryFriendlyByteBuf, MoldingRecipe> STREAM_CODEC = StreamCodec.composite(
            Ingredient.CONTENTS_STREAM_CODEC, MoldingRecipe::sap,
            ItemStack.STREAM_CODEC, MoldingRecipe::result,
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
