package com.github.plastar.item;

import java.util.Optional;

import com.github.plastar.block.PBlocks;
import com.github.plastar.block.StoraxAcaciaLogBlock;

import org.jetbrains.annotations.Nullable;

import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.RotatedPillarBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gameevent.GameEvent;

public class TreeTapItem extends Item {
    private static final Properties treeTapItemProperties = new Properties().stacksTo(1).durability(64);
    public TreeTapItem(Properties properties) {
        super(treeTapItemProperties);
    }

    @Override
    public InteractionResult useOn(UseOnContext context) {
        Level level = context.getLevel();
        BlockPos blockPos = context.getClickedPos();
        Player player = context.getPlayer();
        BlockState blockState = level.getBlockState(blockPos);

        if (player == null) return InteractionResult.PASS;
        if (!blockState.is(PBlocks.STORAX_ACACIA_LOG.get())) return InteractionResult.PASS;
        if (playerHasShieldUseIntent(context)) return InteractionResult.PASS;
        if (!blockState.getValue(StoraxAcaciaLogBlock.SAPPY)) return InteractionResult.PASS;

        Optional<BlockState> newBlockState =
            this.evaluateNewBlockState(level, blockPos, player, level.getBlockState(blockPos));
        if (newBlockState.isEmpty()) {
            return InteractionResult.PASS;
        } else {
            ItemStack itemStack = context.getItemInHand();
            if (player instanceof ServerPlayer) {
                CriteriaTriggers.ITEM_USED_ON_BLOCK.trigger((ServerPlayer) player, blockPos, itemStack);
            }

            ItemStack styrolStack = new ItemStack(PItems.STYROL.get());
            styrolStack.setCount(level.getRandom().nextIntBetweenInclusive(3,6));
            ItemEntity styrolStackEntity =
                new ItemEntity(level, player.getX(), player.getY() + 1.5, player.getZ(), styrolStack);

            level.addFreshEntity(styrolStackEntity);
            level.setBlock(blockPos, newBlockState.get(), 11);
            level.gameEvent(GameEvent.BLOCK_CHANGE, blockPos, GameEvent.Context.of(player, newBlockState.get()));
            itemStack.hurtAndBreak(1, player, LivingEntity.getSlotForHand(context.getHand()));
            return InteractionResult.sidedSuccess(level.isClientSide);
        }
    }

    private static boolean playerHasShieldUseIntent(UseOnContext context) {
        Player player = context.getPlayer();
        if (!context.getHand().equals(InteractionHand.MAIN_HAND)) return false;
        return (player != null && player.getOffhandItem().is(Items.SHIELD)) && !player.isSecondaryUseActive();
    }

    private Optional<BlockState> evaluateNewBlockState(Level level, BlockPos pos, @Nullable Player player,
                                                       BlockState state) {
        Optional<BlockState> optional = this.getTapped(state);
        if (optional.isPresent()) {
            level.playSound(player, pos, SoundEvents.AXE_STRIP, SoundSource.BLOCKS, 1.0F, 1.0F);
            Optional<BlockState> newState = Optional.of(optional.get().setValue(StoraxAcaciaLogBlock.SAPPY, false));
            return newState;
        } else return Optional.empty();
    }

    private Optional<BlockState> getTapped(BlockState untappedState) {
        if (untappedState.getBlock().defaultBlockState().is(PBlocks.STORAX_ACACIA_LOG.get())) {
            return Optional.of(PBlocks.STORAX_ACACIA_LOG.get().defaultBlockState()
                .setValue(RotatedPillarBlock.AXIS, untappedState.getValue(RotatedPillarBlock.AXIS)));
        } else {
            return Optional.empty();
        }
    }
}
