package com.github.plastar;

import com.github.plastar.data.PComponents;
import com.github.plastar.entity.PEntities;
import com.github.plastar.registry.RegistrarSet;

public class PLASTARMod {
    public static final RegistrarSet REGISTRARS = new RegistrarSet();

    public static void init() {
        PEntities.register();
        PComponents.register();
    }

    public static void initSync() {
    }
}
