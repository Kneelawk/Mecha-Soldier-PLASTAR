package com.github.plastar.menu;

import java.util.List;

import com.github.plastar.block.PBlocks;
import com.github.plastar.crafting.PRecipes;
import com.github.plastar.crafting.PrintingRecipe;
import com.github.plastar.crafting.PrintingRecipeInput;
import com.google.common.collect.Lists;

import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.Container;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.inventory.DataSlot;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.inventory.ResultContainer;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.level.Level;

//TODO: do I need to check for additive even though it's technically optional?
public class PrinterMenu extends AbstractContainerMenu {
    public static final int SAP_SLOT = 0;
    public static final int ADDITIVE_SLOT = 1;
    public static final int RESULT_SLOT = 2;
    private static final int INV_SLOT_START = 3;
    private static final int INV_SLOT_END = 30;
    private static final int USE_ROW_SLOT_START = 30;
    private static final int USE_ROW_SLOT_END = 39;

    private final ContainerLevelAccess access;
    private final DataSlot selectedRecipeIndex;
    private final Level level;
    private List<RecipeHolder<PrintingRecipe>> recipes;
    private ItemStack sap;
    private ItemStack additive;
    long lastSoundTime;
    final Slot sapSlot;
    final Slot additiveSlot;
    final Slot resultSlot;
    Runnable slotUpdateListener;
    public final Container container;
    final ResultContainer resultContainer;

    public PrinterMenu(int containerId, Inventory playerInventory) {
        this(containerId, playerInventory, ContainerLevelAccess.NULL);
    }

    public PrinterMenu(int containerId, Inventory playerInventory, final ContainerLevelAccess access) {
        super(PMenus.PRINTER.get(), containerId);
        this.selectedRecipeIndex = DataSlot.standalone();
        this.recipes = Lists.newArrayList();
        this.sap = ItemStack.EMPTY;
        this.additive = ItemStack.EMPTY;
        this.slotUpdateListener = () -> {
        };
        this.container = new SimpleContainer(2) {
            public void setChanged() {
                super.setChanged();
                PrinterMenu.this.slotsChanged(this);
                PrinterMenu.this.slotUpdateListener.run();
            }
        };
        this.resultContainer = new ResultContainer();
        this.access = access;
        this.level = playerInventory.player.level();
        this.sapSlot = this.addSlot(new Slot(this.container, 0, 12, 45));
        this.additiveSlot = this.addSlot(new Slot(this.container, 1, 32, 45));
        this.resultSlot = this.addSlot(new Slot(this.resultContainer, 2, 145, 33) {
            public boolean mayPlace(ItemStack stack) {
                return false;
            }

            public void onTake(Player player, ItemStack stack) {
                stack.onCraftedBy(player.level(), player, stack.getCount());
                PrinterMenu.this.resultContainer.awardUsedRecipes(player, this.getRelevantItems());
                ItemStack itemStack = PrinterMenu.this.sapSlot.remove(PrinterMenu.this.recipes.get(PrinterMenu.this.getSelectedRecipeIndex()).value().sapCount());
                if (!itemStack.isEmpty()) {
                    PrinterMenu.this.setupResultSlot();
                }

                access.execute((level, blockPos) -> {
                    long l = level.getGameTime();
                    if (PrinterMenu.this.lastSoundTime != l) {
                        //TODO: custom sound
                        level.playSound(null, blockPos, SoundEvents.UI_STONECUTTER_TAKE_RESULT, SoundSource.BLOCKS, 1.0F, 1.0F);
                        PrinterMenu.this.lastSoundTime = l;
                    }

                });
                slotsChanged(container);
                super.onTake(player, stack);
            }

            private List<ItemStack> getRelevantItems() {
                return List.of(PrinterMenu.this.sapSlot.getItem(), PrinterMenu.this.additiveSlot.getItem());
            }
        });

        int i;
        for(i = 0; i < 3; ++i) {
            for(int j = 0; j < 9; ++j) {
                this.addSlot(new Slot(playerInventory, j + i * 9 + 9, 8 + j * 18, 84 + i * 18));
            }
        }

        for(i = 0; i < 9; ++i) {
            this.addSlot(new Slot(playerInventory, i, 8 + i * 18, 142));
        }

        this.addDataSlot(this.selectedRecipeIndex);
    }

    public int getSelectedRecipeIndex() {
        return this.selectedRecipeIndex.get();
    }

    public List<RecipeHolder<PrintingRecipe>> getRecipes() {
        return this.recipes;
    }

    public int getNumRecipes() {
        return this.recipes.size();
    }

    public boolean hasInputItem() {
        return this.sapSlot.hasItem() && !this.recipes.isEmpty();
    }

    public boolean stillValid(Player player) {
        return stillValid(this.access, player, PBlocks.PRINTER.get());
    }

    public boolean clickMenuButton(Player player, int id) {
        if (this.isValidRecipeIndex(id)) {
            this.selectedRecipeIndex.set(id);
            this.setupResultSlot();
        }

        return true;
    }

    private boolean isValidRecipeIndex(int recipeIndex) {
        return recipeIndex >= 0 && recipeIndex < this.recipes.size();
    }

    public void slotsChanged(Container container) {
        ItemStack sapStack = this.sapSlot.getItem();
        ItemStack additiveStack = this.sapSlot.getItem();
        if (!ItemStack.isSameItemSameComponents(sapStack, this.sap) || sapStack.getCount() != this.sap.getCount() || !ItemStack.isSameItemSameComponents(additiveStack, this.additive) || additiveStack.getCount() != this.additive.getCount() ) {
            this.sap = sapStack.copy();
            this.additive = additiveStack.copy();
            this.setupRecipeList(container, sapStack);
        }
    }

    public static PrintingRecipeInput createRecipeInput(Container container) {
        return new PrintingRecipeInput(container.getItem(0), container.getItem(1));
    }

    private void setupRecipeList(Container container, ItemStack sapStack) {
        this.recipes.clear();
        this.selectedRecipeIndex.set(-1);
        this.resultSlot.set(ItemStack.EMPTY);
        if (!sapStack.isEmpty()) {
            this.recipes = this.level.getRecipeManager().getRecipesFor(PRecipes.PRINTING.get(), createRecipeInput(container), this.level);
        }
    }

    void setupResultSlot() {
        if (!this.recipes.isEmpty() && this.isValidRecipeIndex(this.selectedRecipeIndex.get())) {
            RecipeHolder<PrintingRecipe> recipeHolder = this.recipes.get(this.selectedRecipeIndex.get());
            ItemStack itemStack = recipeHolder.value().assemble(createRecipeInput(this.container), this.level.registryAccess());
            if (itemStack.isItemEnabled(this.level.enabledFeatures())) {
                this.resultContainer.setRecipeUsed(recipeHolder);
                this.resultSlot.set(itemStack);
            } else {
                this.resultSlot.set(ItemStack.EMPTY);
            }
        } else {
            this.resultSlot.set(ItemStack.EMPTY);
        }

        this.broadcastChanges();
    }

    @Override
    public MenuType<?> getType() {
        return PMenus.PRINTER.get();
    }

    public void registerUpdateListener(Runnable listener) {
        this.slotUpdateListener = listener;
    }

    public boolean canTakeItemForPickAll(ItemStack stack, Slot slot) {
        return slot.container != this.resultContainer && super.canTakeItemForPickAll(stack, slot);
    }

    public ItemStack quickMoveStack(Player player, int index) {
        ItemStack itemStack = ItemStack.EMPTY;
        Slot slot = this.slots.get(index);
        if (slot != null && slot.hasItem()) {
            ItemStack slotStack = slot.getItem();
            Item item = slotStack.getItem();
            itemStack = slotStack.copy();
            if (index == RESULT_SLOT) {
                item.onCraftedBy(slotStack, player.level(), player);
                if (!this.moveItemStackTo(slotStack, INV_SLOT_START, USE_ROW_SLOT_END, true)) {
                    return ItemStack.EMPTY;
                }

                slot.onQuickCraft(slotStack, itemStack);
            } else if (index == SAP_SLOT || index == ADDITIVE_SLOT) {
                if (!this.moveItemStackTo(slotStack, INV_SLOT_START, USE_ROW_SLOT_END, false)) {
                    return ItemStack.EMPTY;
                }
            } else if (this.level.getRecipeManager().getRecipeFor(PRecipes.PRINTING.get(), new PrintingRecipeInput(slotStack, ItemStack.EMPTY), this.level).isPresent()) {
                if (!this.moveItemStackTo(slotStack, SAP_SLOT, RESULT_SLOT, false)) {
                    return ItemStack.EMPTY;
                }
            } else if (index >= INV_SLOT_START && index < INV_SLOT_END) {
                if (!this.moveItemStackTo(slotStack, USE_ROW_SLOT_START, USE_ROW_SLOT_END, false)) {
                    return ItemStack.EMPTY;
                }
            } else if (index >= USE_ROW_SLOT_START && index < USE_ROW_SLOT_END && !this.moveItemStackTo(slotStack, INV_SLOT_START, INV_SLOT_END, false)) {
                return ItemStack.EMPTY;
            }

            if (slotStack.isEmpty()) {
                slot.setByPlayer(ItemStack.EMPTY);
            }

            slot.setChanged();
            if (slotStack.getCount() == itemStack.getCount()) {
                return ItemStack.EMPTY;
            }

            slot.onTake(player, slotStack);
            this.broadcastChanges();
        }

        return itemStack;
    }

    public void removed(Player player) {
        super.removed(player);
        this.resultContainer.removeItemNoUpdate(2);
        this.access.execute((level, blockPos) -> this.clearContainer(player, this.container));
    }
}
