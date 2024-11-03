package com.github.plastar.block;

import java.util.function.Supplier;

import com.github.plastar.PLASTARMod;
import com.github.plastar.registry.Registrar;

import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;

public class PBlocks {
    private static final Registrar<Block> REGISTRAR = PLASTARMod.REGISTRARS.get(Registries.BLOCK);

    public static final Supplier<Block> STORAX_ACACIA_LOG = REGISTRAR.register("storax_acacia_log", StoraxAcaciaLogBlock::new);
    public static final Supplier<Block> MECHA_ASSEMBLER = REGISTRAR.register("mecha_assembler", () -> 
        new MechaAssemblerBlock(BlockBehaviour.Properties.of()));
    public static final Supplier<Block> PRINTER = REGISTRAR.register("printer", () ->
        new PrinterBlock(BlockBehaviour.Properties.of()));

    public static void register() {
    }
}
