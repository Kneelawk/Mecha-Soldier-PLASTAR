package com.github.plastar.block.entity;

import com.github.plastar.block.PBlocks;
import com.github.plastar.menu.AssemblerSlotIds;
import com.github.plastar.menu.MechaAssemblerMenu;

import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.ContainerHelper;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BaseContainerBlockEntity;
import net.minecraft.world.level.block.state.BlockState;

public class MechaAssemblerBlockEntity extends BaseContainerBlockEntity {
    private NonNullList<ItemStack> items = NonNullList.withSize(AssemblerSlotIds.SLOT_COUNT, ItemStack.EMPTY);
    
    public MechaAssemblerBlockEntity(BlockPos pos, BlockState blockState) {
        super(PBlockEntities.MECHA_ASSEMBLER.get(), pos, blockState);
    }

    @Override
    protected Component getDefaultName() {
        return PBlocks.MECHA_ASSEMBLER.get().getName();
    }

    @Override
    protected NonNullList<ItemStack> getItems() {
        return items;
    }

    @Override
    protected void setItems(NonNullList<ItemStack> items) {
        this.items = items;
    }

    @Override
    protected AbstractContainerMenu createMenu(int containerId, Inventory inventory) {
        return new MechaAssemblerMenu(containerId, inventory, this);
    }

    @Override
    public int getContainerSize() {
        return AssemblerSlotIds.SLOT_COUNT;
    }

    @Override
    protected void loadAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.loadAdditional(tag, registries);
        ContainerHelper.loadAllItems(tag, items, registries);
    }

    @Override
    protected void saveAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.saveAdditional(tag, registries);
        ContainerHelper.saveAllItems(tag, items, registries);
    }
}
