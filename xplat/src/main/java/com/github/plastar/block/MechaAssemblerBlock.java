package com.github.plastar.block;

import com.github.plastar.block.entity.MechaAssemblerBlockEntity;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

import org.jetbrains.annotations.Nullable;

public class MechaAssemblerBlock extends Block implements EntityBlock {
    public MechaAssemblerBlock(Properties properties) {
        super(properties);
    }

    @Override
    public @Nullable BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new MechaAssemblerBlockEntity(pos, state);
    }
}
