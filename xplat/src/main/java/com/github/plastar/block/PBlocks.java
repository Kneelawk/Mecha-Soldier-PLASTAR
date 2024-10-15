package com.github.plastar.block;

import com.github.plastar.PLASTARMod;
import com.github.plastar.item.MechaItem;
import com.github.plastar.item.PolystyreneItem;
import com.github.plastar.item.StyrolItem;
import com.github.plastar.item.TreeTapItem;
import com.github.plastar.registry.Registrar;

import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;

import java.util.function.Supplier;

public class PBlocks {
    private static final Registrar<Block> REGISTRAR_BLOCK = PLASTARMod.REGISTRARS.get(Registries.BLOCK);

    public static final Supplier<Block> STORAX_ACACIA_LOG_BLOCK = REGISTRAR_BLOCK.register("storax_acacia_log", StoraxAcaciaLogBlock::new);

    public static void register() {
    }
}
