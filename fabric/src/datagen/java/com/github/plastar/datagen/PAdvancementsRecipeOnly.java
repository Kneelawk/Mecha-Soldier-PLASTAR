package com.github.plastar.datagen;

import com.github.plastar.item.PItems;

import net.minecraft.advancements.Advancement;
import net.minecraft.advancements.AdvancementHolder;
import net.minecraft.advancements.AdvancementRewards;
import net.minecraft.advancements.AdvancementType;
import net.minecraft.advancements.critereon.InventoryChangeTrigger;
import net.minecraft.advancements.critereon.ItemPredicate;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.ItemTags;

import java.util.function.Consumer;

import static com.github.plastar.Constants.rl;
import static net.minecraft.data.recipes.RecipeBuilder.ROOT_RECIPE_ADVANCEMENT;

public class PAdvancementsRecipeOnly implements Consumer<Consumer<AdvancementHolder>> {

    @Override
    public void accept(Consumer<AdvancementHolder> advancementHolderConsumer) {
        AdvancementHolder treeTapRecipe = Advancement.Builder.advancement()
            .parent(ROOT_RECIPE_ADVANCEMENT)
            .display(
                PItems.TREE_TAP.get(),
                Component.literal("recipe unlock"),
                Component.literal("recipe unlock"),
                null,
                AdvancementType.TASK,
                false,
                false,
                true
            )
            .addCriterion("recipe_obtain_tree_tap", InventoryChangeTrigger.TriggerInstance.hasItems(ItemPredicate.Builder.item().of(ItemTags.PLANKS)))
            .rewards(AdvancementRewards.Builder.recipe(rl("tree_tap")))
            .save(advancementHolderConsumer, "plastar" + "/recipe_obtain_tree_tap");
    }
}
