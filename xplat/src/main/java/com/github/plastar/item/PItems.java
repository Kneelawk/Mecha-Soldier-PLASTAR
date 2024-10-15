package com.github.plastar.item;

import java.util.function.Supplier;

import com.github.plastar.PLASTARMod;
import com.github.plastar.registry.Registrar;

import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.Item;

public class PItems {
    private static final Registrar<Item> REGISTRAR = PLASTARMod.REGISTRARS.get(Registries.ITEM);
    public static final Supplier<Item> MECHA = REGISTRAR.register("mecha", () -> new MechaItem(new Item.Properties()));

    public static final Supplier<Item> STYROL = REGISTRAR.register("styrol", () -> new StyrolItem(new Item.Properties()));
    public static final Supplier<Item> POLYSTYRENE = REGISTRAR.register("polystyrene", () -> new PolystyreneItem(new Item.Properties()));
    public static final Supplier<Item> STORAX_TREE_TAP = REGISTRAR.register("storax_tree_tap", () -> new StoraxTreeTapItem(new Item.Properties()));

    public static void register() {
    }
}
