package com.github.plastar.crafting;

import com.github.plastar.data.Additive;

import com.github.plastar.data.PComponents;

import com.github.plastar.data.PartMaterial;

import net.minecraft.core.HolderLookup;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;

//TODO: recipe type and serializer
public record MoldingRecipe(Ingredient sap, ItemStack result) implements Recipe<MoldingRecipeInput> {

    public MoldingRecipe(Ingredient sap, ItemStack result) {
        this.sap = sap;
        this.result = result;
    }

    @Override
    public boolean matches(MoldingRecipeInput input, Level level) {
        if (level instanceof ServerLevel server) {
            return sap.test(input.sap()) && Additive.isAdditive(input.additive(), server.getServer().reloadableRegistries().get());
        }
        return false;
    }

    @Override
    public ItemStack assemble(MoldingRecipeInput input, HolderLookup.Provider registries) {
        ItemStack ret = result.copy();
        var additive = Additive.getAdditive(input.additive(), registries);
		additive.ifPresent(additiveReference -> ret.set(PComponents.PART_MATERIAL.get(), new PartMaterial(additiveReference.unwrapKey())));
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
        return null;
    }

    @Override
    public RecipeType<?> getType() {
        return null;
    }
}
