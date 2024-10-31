package com.github.plastar.datagen;

import java.util.concurrent.CompletableFuture;

import com.github.plastar.block.PBlocks;
import com.github.plastar.item.PItems;

import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricBlockLootTableProvider;

import net.minecraft.core.HolderLookup;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.functions.SetItemCountFunction;
import net.minecraft.world.level.storage.loot.providers.number.UniformGenerator;

public class LootTableProvider extends FabricBlockLootTableProvider {
    protected LootTableProvider(FabricDataOutput dataOutput, CompletableFuture<HolderLookup.Provider> registryLookup) {
        super(dataOutput, registryLookup);
    }

    @Override
    public void generate() {
        add(PBlocks.STORAX_ACACIA_LOG.get(),
            createSilkTouchDispatchTable(
                PBlocks.STORAX_ACACIA_LOG.get(),
                applyExplosionCondition(PBlocks.STORAX_ACACIA_LOG.get(), LootItem.lootTableItem(Items.ACACIA_LOG))
            ).withPool(LootPool.lootPool()
                .when(this.doesNotHaveSilkTouch())
                .add(applyExplosionDecay(PBlocks.STORAX_ACACIA_LOG.get(), LootItem.lootTableItem(PItems.STYROL.get())
                    .apply(SetItemCountFunction.setCount(UniformGenerator.between(1, 3)))))));
    }
}
