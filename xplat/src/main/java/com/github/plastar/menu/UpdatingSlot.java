package com.github.plastar.menu;

import net.minecraft.world.Container;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.Slot;

public class UpdatingSlot extends Slot {
    private final AbstractContainerMenu menu;

    public UpdatingSlot(Container container, int slot, int x, int y, AbstractContainerMenu menu) {
        super(container, slot, x, y);
        this.menu = menu;
    }

    @Override
    public void setChanged() {
        super.setChanged();
        menu.slotsChanged(container);
    }
}
