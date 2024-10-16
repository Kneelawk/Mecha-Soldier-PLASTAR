package com.github.plastar.item;

import java.util.function.Supplier;

import com.github.plastar.PLASTARMod;
import com.github.plastar.block.PBlocks;
import com.github.plastar.block.StoraxAcaciaLogBlock;
import com.github.plastar.registry.Registrar;

import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;

public class PItems {
    private static final Registrar<Item> REGISTRAR = PLASTARMod.REGISTRARS.get(Registries.ITEM);

    public static final Supplier<Item> MECHA = REGISTRAR.register("mecha", () -> new MechaItem(new Item.Properties()));

    public static final Supplier<Item> STYROL = REGISTRAR.register("styrol", () -> new StyrolItem(new Item.Properties()));
    public static final Supplier<Item> POLYSTYRENE = REGISTRAR.register("polystyrene", () -> new PolystyreneItem(new Item.Properties()));
    public static final Supplier<Item> TREE_TAP = REGISTRAR.register("tree_tap", () -> new TreeTapItem(new Item.Properties()));

    /* Block Items TODO: Did I register the BlockItem correctly here? - June */
    public static final Supplier<BlockItem> STORAX_ACACIA_LOG = REGISTRAR.register("storax_acacia_log", () -> new BlockItem(PBlocks.STORAX_ACACIA_LOG_BLOCK.get(), new Item.Properties()));

    public static void register() {
    }
}
