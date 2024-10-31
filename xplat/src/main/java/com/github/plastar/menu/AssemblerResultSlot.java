package com.github.plastar.menu;

import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;

public class AssemblerResultSlot extends UpdatingSlot {
    private final Container inputContainer;

    public AssemblerResultSlot(Container inputContainer, Container container, int slot, int x, int y, AbstractContainerMenu menu) {
        super(container, slot, x, y, menu);
        this.inputContainer = inputContainer;
    }

    @Override
    public boolean mayPlace(ItemStack stack) {
        return false;
    }

    @Override
    public void onTake(Player player, ItemStack stack) {
        super.onTake(player, stack);
        for (int slot = 0; slot < inputContainer.getContainerSize(); slot++) {
            if (!inputContainer.getItem(slot).isEmpty()) {
                inputContainer.removeItem(slot, 1);
            }
        }
    }

    @Override
    public boolean isFake() {
        return true;
    }
}
