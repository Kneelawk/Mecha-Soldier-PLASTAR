package com.github.plastar.client.screen;

import com.github.plastar.Constants;

import com.github.plastar.crafting.PrintingRecipe;
import com.github.plastar.menu.PrinterMenu;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.resources.sounds.SimpleSoundInstance;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.crafting.RecipeHolder;

import java.util.List;

public class PrinterScreen extends AbstractContainerScreen<PrinterMenu> {
    private static final ResourceLocation BG_LOCATION = Constants.rl("textures/gui/container/printer.png");
    private static final ResourceLocation SCROLLER_SPRITE = ResourceLocation.withDefaultNamespace("container/stonecutter/scroller");
    private static final ResourceLocation SCROLLER_DISABLED_SPRITE = ResourceLocation.withDefaultNamespace("container/stonecutter/scroller_disabled");
    private static final ResourceLocation RECIPE_SELECTED_SPRITE = ResourceLocation.withDefaultNamespace("container/stonecutter/recipe_selected");
    private static final ResourceLocation RECIPE_HIGHLIGHTED_SPRITE = ResourceLocation.withDefaultNamespace("container/stonecutter/recipe_highlighted");
    private static final ResourceLocation RECIPE_SPRITE = ResourceLocation.withDefaultNamespace("container/stonecutter/recipe");

    private static final int SCROLLER_WIDTH = 12;
    private static final int SCROLLER_HEIGHT = 15;
    private static final int RECIPES_COLUMNS = 4;
    private static final int RECIPES_ROWS = 3;
    private static final int RECIPES_IMAGE_SIZE_WIDTH = 16;
    private static final int RECIPES_IMAGE_SIZE_HEIGHT = 18;
    private static final int SCROLLER_FULL_HEIGHT = 54;
    private static final int RECIPES_X = 52;
    private static final int RECIPES_Y = 14;

    private float scrollOffset;
    private boolean scrolling;
    private int startIndex;
    private boolean displayRecipes;

    public PrinterScreen(PrinterMenu menu, Inventory playerInventory, Component title) {
        super(menu, playerInventory, title);
        menu.registerUpdateListener(this::resetScroll);
        titleLabelY -= 1;
    }
    
    @Override
    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        super.render(guiGraphics, mouseX, mouseY, partialTick);
        renderTooltip(guiGraphics, mouseX, mouseY);
    }

    @Override
    protected void renderBg(GuiGraphics guiGraphics, float partialTick, int mouseX, int mouseY) {
        guiGraphics.blit(BG_LOCATION, leftPos, topPos, 0, 0, imageWidth, imageHeight);

        int recipesLeft = this.leftPos + 58;
        int recipesTop = this.topPos + 14;
        int scrollbarPos = (int) (41 * scrollOffset);
        ResourceLocation resourceLocation = this.isScrollBarActive() ? SCROLLER_SPRITE : SCROLLER_DISABLED_SPRITE;
        guiGraphics.blitSprite(resourceLocation, recipesLeft + 119, recipesTop + 15 + scrollbarPos, 12, 15);
        int elementIdx = this.startIndex + 12;
        renderButtons(guiGraphics, mouseX, mouseY, recipesLeft, recipesTop, elementIdx);
        renderRecipes(guiGraphics, recipesLeft, recipesTop, elementIdx);
    }

    private void renderButtons(GuiGraphics guiGraphics, int mouseX, int mouseY, int x, int y, int lastVisibleElementIndex) {
        for (int i = this.startIndex; i < lastVisibleElementIndex && i < this.menu.getNumRecipes(); i++) {
            int j = i - this.startIndex;
            int buttonX = x + j % 4 * 16;
            int buttonRow = j / 4;
            int buttonY = y + buttonRow * 18 + 2;
            ResourceLocation resourceLocation;
            if (i == this.menu.getSelectedRecipeIndex()) {
                resourceLocation = RECIPE_SELECTED_SPRITE;
            } else if (mouseX >= buttonX && mouseY >= buttonY && mouseX < buttonX + 16 && mouseY < buttonY + 18) {
                resourceLocation = RECIPE_HIGHLIGHTED_SPRITE;
            } else {
                resourceLocation = RECIPE_SPRITE;
            }

            guiGraphics.blitSprite(resourceLocation, buttonX, buttonY - 1, 16, 18);
        }
    }

    private void renderRecipes(GuiGraphics guiGraphics, int x, int y, int startIndex) {
        List<RecipeHolder<PrintingRecipe>> list = this.menu.getRecipes();

        for (int i = this.startIndex; i < startIndex && i < this.menu.getNumRecipes(); i++) {
            int j = i - this.startIndex;
            int buttonX = x + j % 4 * 16;
            int buttonRow = j / 4;
            int buttonY = y + buttonRow * 18 + 2;
            guiGraphics.renderItem(list.get(i).value().getResultItem(this.minecraft.level.registryAccess()), buttonX, buttonY);
        }
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        this.scrolling = false;
        if (this.displayRecipes) {
            int recipesLeft = this.leftPos + 52;
            int recipesTop = this.topPos + 14;
            int recipesIdx = this.startIndex + 12;

            for (int l = this.startIndex; l < recipesIdx; l++) {
                int m = l - this.startIndex;
                double d = mouseX - (double)(recipesLeft + m % 4 * 16);
                double e = mouseY - (double)(recipesTop + m / 4 * 18);
                if (d >= 0.0 && e >= 0.0 && d < 16.0 && e < 18.0 && this.menu.clickMenuButton(this.minecraft.player, l)) {
                    Minecraft.getInstance().getSoundManager().play(
                        SimpleSoundInstance.forUI(SoundEvents.UI_STONECUTTER_SELECT_RECIPE, 1.0F));
                    this.minecraft.gameMode.handleInventoryButtonClick(this.menu.containerId, l);
                    return true;
                }
            }

            recipesLeft = this.leftPos + 119;
            recipesTop = this.topPos + 9;
            if (mouseX >= (double)recipesLeft && mouseX < (double)(recipesLeft + 12) && mouseY >= (double)recipesTop && mouseY < (double)(recipesTop + 54)) {
                this.scrolling = true;
            }
        }

        return super.mouseClicked(mouseX, mouseY, button);
    }

    @Override
    public boolean mouseDragged(double mouseX, double mouseY, int button, double dragX, double dragY) {
        if (this.scrolling && this.isScrollBarActive()) {
            int i = this.topPos + 14;
            int j = i + 54;
            this.scrollOffset = ((float)mouseY - (float)i - 7.5F) / ((float)(j - i) - 15.0F);
            this.scrollOffset = Mth.clamp(this.scrollOffset, 0.0F, 1.0F);
            this.startIndex = (int)((double)(this.scrollOffset * (float)this.getOffscreenRows()) + 0.5) * 4;
            return true;
        } else {
            return super.mouseDragged(mouseX, mouseY, button, dragX, dragY);
        }
    }

    @Override
    public boolean mouseScrolled(double mouseX, double mouseY, double scrollX, double scrollY) {
        if (this.isScrollBarActive()) {
            int offRows = this.getOffscreenRows();
            float scroll = (float)scrollY / (float)offRows;
            this.scrollOffset = Mth.clamp(this.scrollOffset - scroll, 0.0F, 1.0F);
            this.startIndex = (int)((double)(this.scrollOffset * (float)offRows) + 0.5) * 4;
        }

        return true;
    }

    private boolean isScrollBarActive() {
        return this.displayRecipes && this.menu.getNumRecipes() > 12;
    }

    protected int getOffscreenRows() {
        return (this.menu.getNumRecipes() + 4 - 1) / 4 - 3;
    }

    private void resetScroll() {
        this.displayRecipes = this.menu.hasInputItem();
        if (!this.displayRecipes) {
            this.scrollOffset = 0.0F;
            this.startIndex = 0;
        }
    }
}
