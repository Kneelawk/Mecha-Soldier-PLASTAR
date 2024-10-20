package com.github.plastar;

import com.github.plastar.block.PBlocks;
import com.github.plastar.crafting.PRecipes;
import com.github.plastar.entity.PEntities;
import com.github.plastar.item.PComponents;
import com.github.plastar.item.PCreativeModeTab;
import com.github.plastar.item.PItems;
import com.github.plastar.registry.RegistrarSet;
import com.github.plastar.sound.PSounds;

public class PLASTARMod {
    public static final RegistrarSet REGISTRARS = new RegistrarSet();

    public static void init() {
        PEntities.register();
        PComponents.register();
        PRecipes.register();
        PBlocks.register();
        PItems.register();
        PCreativeModeTab.register();
        PSounds.register();
    }

    public static void initSync() {
    }
}
