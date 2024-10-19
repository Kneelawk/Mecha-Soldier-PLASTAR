package com.github.plastar.block;

import java.util.function.Supplier;

import com.github.plastar.PLASTARMod;
import com.github.plastar.registry.Registrar;

import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.block.Block;

public class PBlocks {
    private static final Registrar<Block> REGISTRAR_BLOCK = PLASTARMod.REGISTRARS.get(Registries.BLOCK);

    public static final Supplier<Block> STORAX_ACACIA_LOG_BLOCK = REGISTRAR_BLOCK.register("storax_acacia_log", StoraxAcaciaLogBlock::new);

    public static void register() {
    }
}
