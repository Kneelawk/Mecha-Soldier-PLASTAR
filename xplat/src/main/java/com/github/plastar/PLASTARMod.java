package com.github.plastar;

import com.github.plastar.block.PBlocks;
import com.github.plastar.crafting.PRecipes;
import com.github.plastar.entity.PEntities;
import com.github.plastar.item.PComponents;
import com.github.plastar.item.PItems;
import com.github.plastar.registry.RegistrarSet;

public class PLASTARMod {
    public static final RegistrarSet REGISTRARS = new RegistrarSet();

    public static void init() {
        PEntities.register();
        PComponents.register();
        PRecipes.register();
        PItems.register();
        PBlocks.register();
    }

    public static void initSync() {
    }
}
