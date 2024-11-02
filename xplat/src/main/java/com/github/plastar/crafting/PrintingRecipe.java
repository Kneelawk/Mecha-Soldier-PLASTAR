package com.github.plastar.crafting;

import java.util.function.Function;

import com.github.plastar.data.Additive;
import com.github.plastar.data.MechaPart;
import com.github.plastar.data.PRegistries;
import com.github.plastar.data.Palettes;
import com.github.plastar.data.PartDefinition;
import com.github.plastar.item.PComponents;
import com.github.plastar.item.PItems;
import com.github.plastar.registry.RegistryUtil;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import net.minecraft.core.Holder;
import net.minecraft.core.HolderLookup;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.ExtraCodecs;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;

public record PrintingRecipe(Ingredient sap, int sapCount, ResourceKey<PartDefinition> result) implements Recipe<PrintingRecipeInput> {
    private static final ItemStack PART_STACK = PItems.MECHA_PART.get().getDefaultInstance();

    @Override
    public boolean matches(PrintingRecipeInput input, Level level) {
        if (level instanceof ServerLevel server) {
            var isValidAdditive = Additive.isAdditive(input.additive(), server.getServer().reloadableRegistries().get());
            return sap.test(input.sap()) && input.sap().getCount() >= sapCount && (input.additive().isEmpty() || isValidAdditive);
        }
        return false;
    }

    @Override
    public ItemStack assemble(PrintingRecipeInput input, HolderLookup.Provider registries) {
        var partRegistry = registries.lookupOrThrow(PRegistries.PART);
        
        var part = RegistryUtil.getPreferred(result, partRegistry);
        var additive = Additive.getAdditive(input.additive(), registries);
        var palette = additive.map(Holder::value)
            .map(Additive::defaultPalette)
            .orElseGet(() -> RegistryUtil.getPreferred(Palettes.UNPAINTED, registries.lookupOrThrow(PRegistries.PALETTE)));

        var stack = PART_STACK.copy();
        stack.set(PComponents.MECHA_PART.get(), new MechaPart(part, additive.map(Function.identity()), part.value().defaultPattern(), palette));
        return stack;
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
        return PRecipes.PRINTING_SERIALIZER.get();
    }

    @Override
    public RecipeType<?> getType() {
        return PRecipes.PRINTING.get();
    }
    
    public static class Serializer implements RecipeSerializer<PrintingRecipe> {
        private static final MapCodec<PrintingRecipe> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
            Ingredient.CODEC.fieldOf("sap").forGetter(PrintingRecipe::sap),
            ExtraCodecs.POSITIVE_INT.fieldOf("sap_count").forGetter(PrintingRecipe::sapCount),
            ResourceKey.codec(PRegistries.PART).fieldOf("result").forGetter(PrintingRecipe::result)
        ).apply(instance, PrintingRecipe::new));
        private static final StreamCodec<RegistryFriendlyByteBuf, PrintingRecipe> STREAM_CODEC = StreamCodec.composite(
            Ingredient.CONTENTS_STREAM_CODEC, PrintingRecipe::sap,
            ByteBufCodecs.VAR_INT, PrintingRecipe::sapCount,
            ResourceKey.streamCodec(PRegistries.PART), PrintingRecipe::result,
            PrintingRecipe::new
        );
        
        @Override
        public MapCodec<PrintingRecipe> codec() {
            return CODEC;
        }

        @Override
        public StreamCodec<RegistryFriendlyByteBuf, PrintingRecipe> streamCodec() {
            return STREAM_CODEC;
        }
    }
}
