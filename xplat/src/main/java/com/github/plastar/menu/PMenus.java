package com.github.plastar.menu;

import com.github.plastar.PLASTARMod;
import com.github.plastar.registry.Registrar;


import net.minecraft.core.registries.Registries;
import net.minecraft.world.flag.FeatureFlagSet;
import net.minecraft.world.inventory.MenuType;

import java.util.function.Supplier;

public class PMenus {
    public static final Registrar<MenuType<?>> REGISTRAR = PLASTARMod.REGISTRARS.get(Registries.MENU);

    public static final Supplier<MenuType<PrinterMenu>> PRINTER = REGISTRAR.register("printer", () -> new MenuType<>(PrinterMenu::new, FeatureFlagSet.of()));
}
