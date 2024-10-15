package com.github.plastar.crafting;

import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeInput;

public record MoldingRecipeInput(ItemStack sap, ItemStack additive) implements RecipeInput {
    @Override
    public ItemStack getItem(int index) {
        return index == 0 ? sap : additive;
    }

    @Override
    public int size() {
        return 2;
    }

    @Override
    public boolean isEmpty() {
        return sap.isEmpty() && additive().isEmpty();
    }
}
