package com.github.plastar.crafting;

import java.util.function.Supplier;

import com.github.plastar.PLASTARMod;
import com.github.plastar.registry.Registrar;

import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;

public abstract class PRecipes {
    private static final Registrar<RecipeType<?>> TYPE_REGISTRAR = PLASTARMod.REGISTRARS.get(Registries.RECIPE_TYPE);
    private static final Registrar<RecipeSerializer<?>> SERIALIZER_REGISTRAR = PLASTARMod.REGISTRARS.get(Registries.RECIPE_SERIALIZER);
    
    public static final Supplier<RecipeType<MoldingRecipe>> MOLDING = TYPE_REGISTRAR.register("molding", RecipeTypeImpl::new);
    public static final Supplier<RecipeSerializer<MoldingRecipe>> MOLDING_SERIALIZER = SERIALIZER_REGISTRAR.register("molding", MoldingRecipe.Serializer::new);
    
    public static void register() {
    }
    
    private static class RecipeTypeImpl<T extends Recipe<?>> implements RecipeType<T> {}
}
