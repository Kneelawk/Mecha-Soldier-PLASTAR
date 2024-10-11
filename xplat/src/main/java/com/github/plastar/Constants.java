package com.github.plastar;

import net.minecraft.resources.ResourceLocation;

public class Constants {
    public static final String MOD_ID = "plastar";

    public static ResourceLocation rl(String path) {
        return ResourceLocation.fromNamespaceAndPath(MOD_ID, path);
    }
}
