package com.github.plastar.crafting;

import java.util.Optional;

import com.github.plastar.data.program.MechaProgram;
import com.github.plastar.item.PComponents;
import com.github.plastar.item.PItems;

import net.minecraft.core.HolderLookup;
import net.minecraft.core.NonNullList;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.CraftingBookCategory;
import net.minecraft.world.item.crafting.CraftingInput;
import net.minecraft.world.item.crafting.CustomRecipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.level.Level;

public class CardPunchingRecipe extends CustomRecipe {
    public CardPunchingRecipe(CraftingBookCategory category) {
        super(category);
    }

    @Override
    public boolean matches(CraftingInput input, Level level) {
        return input.items().stream().anyMatch(stack -> stack.is(PItems.PUNCH_CARD.get()));
    }

    @Override
    public ItemStack assemble(CraftingInput input, HolderLookup.Provider registries) {
        ItemStack punchCard = ItemStack.EMPTY;
        ItemStack zoningTool = ItemStack.EMPTY;

        for (ItemStack stack : input.items()) {
            if (stack.is(PItems.PUNCH_CARD.get())) {
                punchCard = stack.copyWithCount(1);
            }
            if (stack.is(PItems.ZONING_TOOL.get())) {
                zoningTool = stack.copy();
            }
        }

        MechaProgram program = MechaProgram.DEFAULT;

        if (!zoningTool.isEmpty() && zoningTool.has(PComponents.MECHA_2D_AREA.get())) {
            program = new MechaProgram(Optional.ofNullable(zoningTool.get(PComponents.MECHA_2D_AREA.get())));
        }

        punchCard.set(PComponents.MECHA_PROGRAM.get(), program);

        return punchCard;
    }

    @Override
    public NonNullList<ItemStack> getRemainingItems(CraftingInput input) {
        int size = input.size();
        NonNullList<ItemStack> remaining = NonNullList.withSize(size, ItemStack.EMPTY);

        for (int i = 0; i < size; i++) {
            ItemStack stack = input.getItem(i);
            if (!stack.is(PItems.PUNCH_CARD.get())) {
                remaining.set(i, stack.copy());
            }
        }

        return remaining;
    }

    @Override
    public boolean canCraftInDimensions(int width, int height) {
        return width >= 2 && height >= 2;
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return PRecipes.CARD_PUNCHING_SERIALIZER.get();
    }
}
