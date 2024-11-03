package com.github.plastar.item;

import java.util.function.Supplier;

import com.github.plastar.PLASTARMod;
import com.github.plastar.block.PBlocks;
import com.github.plastar.data.program.MechaProgram;
import com.github.plastar.registry.Registrar;

import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;

public class PItems {
    private static final Registrar<Item> REGISTRAR = PLASTARMod.REGISTRARS.get(Registries.ITEM);

    public static final Supplier<Item> MECHA = REGISTRAR.register("mecha", () -> new MechaItem(new Item.Properties()));
    public static final Supplier<Item> MECHA_PART = REGISTRAR.register("mecha_part", () -> new MechaPartItem(new Item.Properties().component(PComponents.PART_SNIPS.get(), 4)));

    public static final Supplier<Item> STYROL = REGISTRAR.register("styrol", () -> new Item(new Item.Properties()));
    public static final Supplier<Item> POLYSTYRENE = REGISTRAR.register("polystyrene", () -> new Item(new Item.Properties()));
    public static final Supplier<Item> TREE_TAP = REGISTRAR.register("tree_tap", () -> new TreeTapItem(new Item.Properties()));

    public static final Supplier<Item> NIPPERS = REGISTRAR.register("nippers", () -> new NippersItem(new Item.Properties().stacksTo(1)));
    
    public static final Supplier<Item> PUNCH_CARD = REGISTRAR.register("punch_card", () -> new PunchCardItem(new Item.Properties().component(PComponents.MECHA_PROGRAM.get(), MechaProgram.DEFAULT)));
    public static final Supplier<Item> CARTOGRAPHER = REGISTRAR.register("cartographer", () -> new CartographerItem(new Item.Properties()));

    /* Block Items */
    public static final Supplier<BlockItem> STORAX_ACACIA_LOG = REGISTRAR.register("storax_acacia_log", () -> new BlockItem(PBlocks.STORAX_ACACIA_LOG.get(), new Item.Properties()));
    public static final Supplier<BlockItem> MECHA_ASSEMBLER = REGISTRAR.register("mecha_assembler", () -> new BlockItem(PBlocks.MECHA_ASSEMBLER.get(), new Item.Properties()));

    public static void register() {
    }
}
