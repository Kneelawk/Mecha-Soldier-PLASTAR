package com.github.plastar.item.modifier;

import java.util.function.Supplier;

import com.github.plastar.PLASTARMod;
import com.github.plastar.registry.Registrar;

import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.storage.loot.functions.LootItemFunctionType;

public class PLootFunctions {
    private static final Registrar<LootItemFunctionType<?>> REGISTRAR = PLASTARMod.REGISTRARS.get(Registries.LOOT_FUNCTION_TYPE);
    
    public static final Supplier<LootItemFunctionType<PaintPartModifier>> PAINT_PART = REGISTRAR.register("paint_part", () -> new LootItemFunctionType<>(PaintPartModifier.CODEC));
    
    public static void register() {
    }
}
