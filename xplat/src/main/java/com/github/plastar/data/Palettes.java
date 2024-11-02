package com.github.plastar.data;

import com.github.plastar.Constants;

import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.resources.ResourceKey;

public class Palettes {
    public static final ResourceKey<Palette> A = key("a");
    public static final ResourceKey<Palette> B = key("b");
    public static final ResourceKey<Palette> C = key("c");
    public static final ResourceKey<Palette> UNPAINTED = key("unpainted");

    private static ResourceKey<Palette> key(String path) {
        return ResourceKey.create(PRegistries.PALETTE, Constants.rl(path));
    }

    public static void bootstrap(BootstrapContext<Palette> context) {
        context.register(A, new Palette("a"));
        context.register(B, new Palette("b"));
        context.register(C, new Palette("c"));
        context.register(UNPAINTED, new Palette("unpainted"));
    }
}
