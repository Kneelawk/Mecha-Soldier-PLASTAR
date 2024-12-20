package com.github.plastar.client.screen;

import com.github.plastar.Constants;
import com.github.plastar.menu.MechaAssemblerMenu;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;

public class MechaAssemblerScreen extends AbstractContainerScreen<MechaAssemblerMenu> {
    private static final ResourceLocation TEXTURE_LOCATION = Constants.rl("textures/gui/container/mecha_assembler.png");
    
    public MechaAssemblerScreen(MechaAssemblerMenu menu, Inventory playerInventory, Component title) {
        super(menu, playerInventory, title);
    }
    
    @Override
    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        super.render(guiGraphics, mouseX, mouseY, partialTick);
        renderTooltip(guiGraphics, mouseX, mouseY);
    }

    @Override
    protected void renderBg(GuiGraphics guiGraphics, float partialTick, int mouseX, int mouseY) {
        guiGraphics.blit(TEXTURE_LOCATION, leftPos, topPos, 0, 0, imageWidth, imageHeight);
    }
}
