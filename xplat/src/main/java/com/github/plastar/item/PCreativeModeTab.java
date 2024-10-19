package com.github.plastar.item;

import java.util.Optional;
import java.util.function.Supplier;

import com.github.plastar.Constants;
import com.github.plastar.PLASTARMod;
import com.github.plastar.data.MechaPart;
import com.github.plastar.data.PRegistries;
import com.github.plastar.data.Palette;
import com.github.plastar.data.PartMaterial;

import net.minecraft.core.Holder;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
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
                output.accept(PItems.MECHA.get());
                
                var paletteRegistry = parameters.holders().lookupOrThrow(PRegistries.PALETTE);
                var palette = getPalette(paletteRegistry);

                var partRegistry = parameters.holders().lookupOrThrow(PRegistries.PART);
                partRegistry.listElements()
                    .forEach(partDefinition -> {
                        var pattern = partDefinition.value().supportedPatterns().stream().findFirst().flatMap(Holder::unwrapKey);
                        if (pattern.isEmpty()) return;
                        var part = new MechaPart(partDefinition.key(), new PartMaterial(Optional.empty()), pattern.get(), palette);
                        var stack = PItems.MECHA_PART.get().getDefaultInstance();
                        stack.set(PComponents.MECHA_PART.get(), part);
                        output.accept(stack);
                    });
            })
            .build());
    
    public static void register() {
    }

    private static ResourceKey<Palette> getPalette(HolderLookup.RegistryLookup<Palette> paletteRegistry) {
        var palette = ResourceKey.create(PRegistries.PALETTE, Constants.rl("a"));
        if (paletteRegistry.get(palette).isEmpty()) {
            var optional = paletteRegistry.listElementIds().findFirst();
            if (optional.isPresent()) {
                return optional.get();
            }
        }
        return palette;
    }
}
