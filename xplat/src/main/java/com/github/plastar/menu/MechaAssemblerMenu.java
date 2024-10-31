package com.github.plastar.menu;

import java.util.Collections;
import java.util.EnumMap;

import com.github.plastar.data.Mecha;
import com.github.plastar.data.MechaPart;
import com.github.plastar.data.MechaSection;
import com.github.plastar.data.PRegistries;
import com.github.plastar.data.PartDefinition;
import com.github.plastar.item.PComponents;
import com.github.plastar.item.PItems;

import net.minecraft.core.Registry;
import net.minecraft.core.component.DataComponents;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtOps;
import net.minecraft.world.Container;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.CustomData;

public class MechaAssemblerMenu extends AbstractContainerMenu {
    private static final int RESULT_SLOT = AssemblerSlotIds.SLOT_COUNT;
    
    private final Inventory inventory;
    private final Container container;
    private final Container resultContainer = new SimpleContainer(1);

    public MechaAssemblerMenu(int containerId, Inventory inventory, Container container) {
        super(PMenus.MECHA_ASSEMBLER.get(), containerId);
        checkContainerSize(container, AssemblerSlotIds.SLOT_COUNT);
        this.inventory = inventory;
        this.container = container;
        
        addSlot(new UpdatingSlot(container, AssemblerSlotIds.HEAD, 35 + 18, 17, this));
        addSlot(new UpdatingSlot(container, AssemblerSlotIds.TORSO, 35 + 18, 17 + 18, this));
        addSlot(new UpdatingSlot(container, AssemblerSlotIds.RIGHT_ARM, 35, 17 + 18, this));
        addSlot(new UpdatingSlot(container, AssemblerSlotIds.LEFT_ARM, 35 + 18 * 2, 17 + 18, this));
        addSlot(new UpdatingSlot(container, AssemblerSlotIds.RIGHT_LEG, 35 + 9, 17 + 18 * 2, this));
        addSlot(new UpdatingSlot(container, AssemblerSlotIds.LEFT_LEG, 35 + 9 + 18, 17 + 18 * 2, this));
        addSlot(new UpdatingSlot(container, AssemblerSlotIds.BACKPACK, 35 - 18, 17, this));
        
        addSlot(new AssemblerResultSlot(container, resultContainer, 0, 132, 17 + 18, this));

        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 9; j++) {
                this.addSlot(new Slot(inventory, j + i * 9 + 9, 8 + j * 18, 84 + i * 18));
            }
        }

        for (int i = 0; i < 9; i++) {
            this.addSlot(new Slot(inventory, i, 8 + i * 18, 142));
        }
        updateResult();
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
    public void slotsChanged(Container container) {
        super.slotsChanged(container);
        updateResult();
    }

    private void updateResult() {
        var registries = inventory.player.level().registryAccess();
        var partDefs = registries.registryOrThrow(PRegistries.PART);
        
        var validMecha = true;
        for (var section : AssemblerSlotIds.REQUIRED_SECTIONS) {
            var stack = container.getItem(AssemblerSlotIds.getSlot(section));
            if (stack.isEmpty()) {
                validMecha = false;
                break;
            }
        }

        for (var slot = 0; slot < container.getContainerSize(); slot++) {
            var section = AssemblerSlotIds.getSection(slot);
            if (section == null) throw new IllegalStateException();
            
            var stack = container.getItem(slot);
            if (!stack.isEmpty() && !isValidPartForSection(section, stack, partDefs)) {
                validMecha = false;
                break;
            }
        }
        
        if (!validMecha) {
            resultContainer.setItem(0, ItemStack.EMPTY);
            return;
        }
        
        var parts = new EnumMap<MechaSection, MechaPart>(MechaSection.class);
        for (var slot = 0; slot < container.getContainerSize(); slot++) {
            var stack = container.getItem(slot);
            if (stack.isEmpty()) continue;
            
            var part = stack.get(PComponents.MECHA_PART.get());
            if (part == null) continue;
            
            var partDef = partDefs.get(part.definition());
            if (partDef == null) continue;
            
            parts.put(partDef.section(), part);
        }
        var mecha = new Mecha(Collections.unmodifiableMap(parts));
        var resultStack = PItems.MECHA.get().getDefaultInstance();
        var data = CustomData.of((CompoundTag) Mecha.CODEC.fieldOf("mecha")
            .codec()
            .encodeStart(registries.createSerializationContext(NbtOps.INSTANCE), mecha).getOrThrow());
        resultStack.set(DataComponents.BUCKET_ENTITY_DATA, data);
        
        resultContainer.setItem(0, resultStack);
    }

    private static boolean isValidPartForSection(MechaSection section, ItemStack stack,
                                                 Registry<PartDefinition> partDefs) {
        var part = stack.get(PComponents.MECHA_PART.get());
        if (stack.isEmpty()) return false;
        if (part == null) return false;
        var partDef = partDefs.get(part.definition());
        if (partDef == null) return false;

        return partDef.section() == section;
    }

    @Override
    public boolean stillValid(Player player) {
        return container.stillValid(player);
    }

    @Override
    public void removed(Player player) {
        super.removed(player);
        resultContainer.removeItemNoUpdate(0);
    }
}
