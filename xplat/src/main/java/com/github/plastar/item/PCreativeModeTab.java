package com.github.plastar.item;

import java.util.Optional;
import java.util.function.Supplier;

import com.github.plastar.PLASTARMod;
import com.github.plastar.data.MechaPart;
import com.github.plastar.data.PRegistries;
import com.github.plastar.data.Palettes;
import com.github.plastar.registry.RegistryUtil;

import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;

public class PCreativeModeTab {
    public static final Supplier<CreativeModeTab> MOD_TAB = PLASTARMod.REGISTRARS.get(Registries.CREATIVE_MODE_TAB)
        .register("main", () -> CreativeModeTab.builder(CreativeModeTab.Row.TOP, 0) // Both loaders provide versions with 0 parameters, but their impl just delegates to this with bogus values
            .title(Component.translatable("itemGroup.plastar.main"))
            .icon(() -> PItems.MECHA.get().getDefaultInstance())
            .displayItems((parameters, output) -> {
                output.accept(PItems.STORAX_ACACIA_LOG.get());
                output.accept(PItems.TREE_TAP.get());
                output.accept(PItems.STYROL.get());
                output.accept(PItems.POLYSTYRENE.get());
                output.accept(PItems.NIPPERS.get());
                output.accept(PItems.MECHA_ASSEMBLER.get());
                output.accept(PItems.MECHA.get());

                var partRegistry = parameters.holders().lookupOrThrow(PRegistries.PART);
                var palette = RegistryUtil.getPreferred(Palettes.UNPAINTED, parameters.holders().lookupOrThrow(PRegistries.PALETTE));
                partRegistry.listElements()
                    .forEach(partDefinition -> {
                        var pattern = partDefinition.value().defaultPattern();
                        var part = new MechaPart(partDefinition, Optional.empty(), pattern, palette);
                        var stack = PItems.MECHA_PART.get().getDefaultInstance();
                        stack.set(PComponents.MECHA_PART.get(), part);
                        output.accept(stack);
                    });
            })
            .build());
    
    public static void register() {
    }
}
