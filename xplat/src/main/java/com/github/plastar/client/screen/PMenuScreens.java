package com.github.plastar.client.screen;

import com.github.plastar.menu.PMenus;

import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.inventory.MenuAccess;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;

public class PMenuScreens {
    public static void register(MenuScreenConsumer consumer) {
        consumer.register(PMenus.MECHA_ASSEMBLER.get(), MechaAssemblerScreen::new);
        consumer.register(PMenus.PRINTER.get(), PrinterScreen::new);
    }
    
    public interface MenuScreenConsumer {
        <M extends AbstractContainerMenu, U extends Screen & MenuAccess<M>> void register(
            MenuType<? extends M> type, MenuScreens.ScreenConstructor<M, U> factory
        );
    }
}
