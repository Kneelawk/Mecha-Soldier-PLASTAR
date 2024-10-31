package com.github.plastar.block.entity;

import java.util.function.Supplier;

import com.github.plastar.PLASTARMod;
import com.github.plastar.block.PBlocks;
import com.github.plastar.registry.Registrar;

import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;

public class PBlockEntities {
    private static final Registrar<BlockEntityType<?>> REGISTRAR = PLASTARMod.REGISTRARS.get(Registries.BLOCK_ENTITY_TYPE);
    
    public static final Supplier<BlockEntityType<MechaAssemblerBlockEntity>> MECHA_ASSEMBLER = 
        REGISTRAR.register("mecha_assembler", () -> makeType(MechaAssemblerBlockEntity::new,
            PBlocks.MECHA_ASSEMBLER.get()));

    private static <T extends BlockEntity> BlockEntityType<T> makeType(BlockEntityType.BlockEntitySupplier<T> factory, Block... blocks) {
        // We can actually safely pass null here
        //noinspection DataFlowIssue
        return BlockEntityType.Builder.of(factory, blocks).build(null);
    }

    public static void register() {
    }
}
