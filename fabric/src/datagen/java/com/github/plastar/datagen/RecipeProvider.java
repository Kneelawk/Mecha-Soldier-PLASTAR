package com.github.plastar.datagen;

import java.util.concurrent.CompletableFuture;

import com.github.plastar.block.PBlocks;
import com.github.plastar.crafting.PrintingRecipe;
import com.github.plastar.data.Parts;
import com.github.plastar.item.PItems;

import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricRecipeProvider;

import net.minecraft.core.HolderLookup;
import net.minecraft.data.recipes.RecipeBuilder;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.data.recipes.ShapedRecipeBuilder;
import net.minecraft.data.recipes.SimpleCookingRecipeBuilder;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;

import static com.github.plastar.Constants.rl;

public class RecipeProvider extends FabricRecipeProvider {
    public RecipeProvider(FabricDataOutput output, CompletableFuture<HolderLookup.Provider> registriesFuture) {
        super(output, registriesFuture);
    }

    @Override
    public void buildRecipes(RecipeOutput output) {
        ShapedRecipeBuilder.shaped(RecipeCategory.TOOLS, PItems.NIPPERS.get())
            .pattern(" i ")
            .pattern("//i")
            .pattern(" / ")
            .define('i', Items.IRON_NUGGET)
            .define('/', Items.STICK)
            .unlockedBy("has_nippers", has(PItems.NIPPERS.get())) // TODO: runners should probably unlock this
            .save(output);
        
        ShapedRecipeBuilder.shaped(RecipeCategory.DECORATIONS, PBlocks.MECHA_ASSEMBLER.get())
            .pattern("#L#")
            .pattern("GRG")
            .pattern("iCi")
            .define('#', PItems.POLYSTYRENE.get())
            .define('L', Items.LIGHTNING_ROD)
            .define('G', Items.GLASS)
            .define('R', Items.REDSTONE_BLOCK)
            .define('i', Items.IRON_INGOT)
            .define('C', Items.CRAFTING_TABLE)
            .unlockedBy("has_part", has(PItems.MECHA_PART.get()))
            .save(output);
        
        ShapedRecipeBuilder.shaped(RecipeCategory.TOOLS, PItems.TREE_TAP.get())
            .pattern(" / ")
            .pattern("#O#")
            .pattern("   ")
            .define('/', Items.STICK)
            .define('#', ItemTags.STONE_TOOL_MATERIALS)
            .define('O', ItemTags.PLANKS)
            .unlockedBy("has_planks", has(ItemTags.PLANKS))
            .save(output);
        
        SimpleCookingRecipeBuilder.smelting(
            Ingredient.of(PItems.STYROL.get()),
            RecipeCategory.MISC,
            PItems.POLYSTYRENE.get(),
            1.0f,
            200)
            .unlockedBy("has_storol", has(PItems.STYROL.get()))
            .save(output);

        output.accept(rl("left_emma_leg"), new PrintingRecipe(Ingredient.of(PItems.POLYSTYRENE.get()), 4, Parts.EMMA_LEFT_LEG), null);
    }
}
