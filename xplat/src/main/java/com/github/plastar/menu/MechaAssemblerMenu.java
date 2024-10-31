package com.github.plastar.menu;

import com.github.plastar.data.PRegistries;
import com.github.plastar.item.PComponents;

import net.minecraft.world.Container;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;

public class MechaAssemblerMenu extends AbstractContainerMenu {
    private Inventory inventory;
    private Container container;

    public MechaAssemblerMenu(int containerId, Inventory inventory, Container container) {
        super(PMenus.MECHA_ASSEMBLER.get(), containerId);
        checkContainerSize(container, AssemblerSlotIds.SLOT_COUNT);
        this.inventory = inventory;
        this.container = container;
        
        addSlot(new Slot(container, AssemblerSlotIds.RESULT, 124, 17 + 18));
        
        addSlot(new Slot(container, AssemblerSlotIds.HEAD, 30 + 18, 17));
        addSlot(new Slot(container, AssemblerSlotIds.TORSO, 30 + 18, 17 + 18));
        addSlot(new Slot(container, AssemblerSlotIds.RIGHT_ARM, 30, 17 + 18));
        addSlot(new Slot(container, AssemblerSlotIds.LEFT_ARM, 30 + 18 * 2, 17 + 18));
        addSlot(new Slot(container, AssemblerSlotIds.RIGHT_LEG, 30 + 9, 17 + 18 * 2));
        addSlot(new Slot(container, AssemblerSlotIds.LEFT_LEG, 30 + 9 + 18, 17 + 18 * 2));
        addSlot(new Slot(container, AssemblerSlotIds.BACKPACK, 30 - 18, 17));

        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 9; j++) {
                this.addSlot(new Slot(inventory, j + i * 9 + 9, 8 + j * 18, 84 + i * 18));
            }
        }

        for (int i = 0; i < 9; i++) {
            this.addSlot(new Slot(inventory, i, 8 + i * 18, 142));
        }
    }

    public MechaAssemblerMenu(int containerId, Inventory inventory) {
        this(containerId, inventory, new SimpleContainer(AssemblerSlotIds.SLOT_COUNT));
    }

    @Override
    public ItemStack quickMoveStack(Player player, int index) {
        var slot = slots.get(index);
        if (!slot.hasItem()) return ItemStack.EMPTY;
        var stack = slot.getItem();
        var originalStack = stack.copy();
        
        moveToInput:
        if (index >= AssemblerSlotIds.SLOT_COUNT) {
            var part = stack.get(PComponents.MECHA_PART.get());
            if (part == null) break moveToInput;
            
            var definition = player.level().registryAccess().registryOrThrow(PRegistries.PART).get(part.definition());
            if (definition == null) break moveToInput;
            
            var targetSlot = switch (definition.section()) {
                case HEAD -> AssemblerSlotIds.HEAD;
                case TORSO -> AssemblerSlotIds.TORSO;
                case RIGHT_ARM -> AssemblerSlotIds.RIGHT_ARM;
                case LEFT_ARM -> AssemblerSlotIds.LEFT_ARM;
                case RIGHT_LEG -> AssemblerSlotIds.RIGHT_LEG;
                case LEFT_LEG -> AssemblerSlotIds.LEFT_LEG;
                case BACKPACK -> AssemblerSlotIds.BACKPACK;
                default -> -1;
            };
            if (targetSlot == -1) break moveToInput;

            if (!this.moveItemStackTo(stack, targetSlot, targetSlot + 1, false)) {
                return ItemStack.EMPTY;
            }
        } 
        
        if (index < AssemblerSlotIds.SLOT_COUNT) {
            if (!this.moveItemStackTo(stack, AssemblerSlotIds.SLOT_COUNT, AssemblerSlotIds.SLOT_COUNT + 36, false)) {
                return ItemStack.EMPTY;
            }
        }

        if (stack.isEmpty()) {
            slot.setByPlayer(ItemStack.EMPTY);
        } else {
            slot.setChanged();
        }
        if (stack.getCount() == originalStack.getCount()) {
            return ItemStack.EMPTY;
        }
        slot.onTake(player, originalStack);
        
        return originalStack;
    }

    @Override
    public boolean stillValid(Player player) {
        return container.stillValid(player);
    }
}
