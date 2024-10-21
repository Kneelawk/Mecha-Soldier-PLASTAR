package com.github.plastar.datagen;

import com.github.plastar.item.PItems;

import net.minecraft.advancements.Advancement;
import net.minecraft.advancements.AdvancementHolder;
import net.minecraft.advancements.AdvancementRewards;
import net.minecraft.advancements.AdvancementType;
import net.minecraft.advancements.critereon.EnterBlockTrigger;
import net.minecraft.advancements.critereon.InventoryChangeTrigger;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Blocks;

import java.util.function.Consumer;

import static com.github.plastar.Constants.rl;

public class PAdvancements implements Consumer<Consumer<AdvancementHolder>> {

    @Override
    public void accept(Consumer<AdvancementHolder> advancementHolderConsumer) {
        AdvancementHolder obtainStyrolAdvancement = Advancement.Builder.advancement()
            .display(
                PItems.STYROL.get(),
                Component.translatable("advancement.plastar.sap_tapping.title"),
                Component.translatable("advancement.plastar.sap_tapping.description"),
                ResourceLocation.fromNamespaceAndPath("minecraft","textures/gui/advancements/backgrounds/adventure.png"), // Background image used
                AdvancementType.TASK,
                true,
                true,
                false
            )
            .addCriterion("obtain_styrol", InventoryChangeTrigger.TriggerInstance.hasItems(PItems.STYROL.get()))
            .rewards(AdvancementRewards.Builder.recipe(rl("polystyrene")))
            .save(advancementHolderConsumer, "plastar" + "/obtain_styrol");

        AdvancementHolder obtainPolystyreneAdvancement = Advancement.Builder.advancement().parent(obtainStyrolAdvancement)
            .display(
                PItems.POLYSTYRENE.get(),
                Component.translatable("advancement.plastar.polymerisation.title"),
                Component.translatable("advancement.plastar.polymerisation.description"),
                null,
                AdvancementType.TASK,
                true,
                true,
                false
            )
            .addCriterion("obtain_polystyrene", InventoryChangeTrigger.TriggerInstance.hasItems(PItems.POLYSTYRENE.get()))
            .save(advancementHolderConsumer, "plastar" + "/obtain_polystyrene");
    }
}
