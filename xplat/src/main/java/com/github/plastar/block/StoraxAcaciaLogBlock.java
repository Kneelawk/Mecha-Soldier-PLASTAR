package com.github.plastar.block;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.RotatedPillarBlock;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.NoteBlockInstrument;
import net.minecraft.world.level.material.MapColor;

/**
 * @Summary Acacia Log filled with Storax (tree sap) used to create Styrol & Polystyrene
 * @Loot_table This block's loot table drops a normal acacia log and 1-3 styrol by default.
 * If silk touched, this storax-filled acacia block will drop, but no styrol
 * @Interaction When right-clicked with a tree tap, this block drops 3-6 styrol and changes SAPPY state to false.
 * It becomes SAPPY again over time.
 **/
public class StoraxAcaciaLogBlock extends RotatedPillarBlock {
    public static final Properties PROPERTIES = Properties.of().mapColor((state) ->
            state.getValue(RotatedPillarBlock.AXIS) == Direction.Axis.Y ? MapColor.STONE : MapColor.COLOR_ORANGE)
        .instrument(NoteBlockInstrument.BASS).strength(2.0F).sound(SoundType.WOOD).ignitedByLava().randomTicks();

    public StoraxAcaciaLogBlock() {
        super(PROPERTIES);
    }

    /* Only ticks if not sappy */
    @Override
    protected boolean isRandomlyTicking(BlockState state) {
        return !state.getValue(SAPPY);
    }

    /* Regenerate to sappy state over time */
    @Override
    protected void randomTick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
        if (!level.isLoaded(pos)) return;
        if (random.nextIntBetweenInclusive(1,4) != 1) return;
        level.setBlockAndUpdate(pos, PBlocks.STORAX_ACACIA_LOG_BLOCK.get().defaultBlockState());
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(AXIS, SAPPY);
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        return this.defaultBlockState().setValue(AXIS, context.getClickedFace().getAxis()).setValue(SAPPY, true);
    }

    public static final BooleanProperty SAPPY = BooleanProperty.create("sappy");
}
