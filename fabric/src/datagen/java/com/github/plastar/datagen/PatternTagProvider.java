package com.github.plastar.datagen;

import java.util.concurrent.CompletableFuture;

import com.github.plastar.data.PRegistries;
import com.github.plastar.data.Pattern;
import com.github.plastar.data.Patterns;

import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagProvider;

import net.minecraft.core.HolderLookup;

public class PatternTagProvider extends FabricTagProvider<Pattern> {
    public PatternTagProvider(FabricDataOutput output, CompletableFuture<HolderLookup.Provider> registriesFuture) {
        super(output, PRegistries.PATTERN, registriesFuture);
    }

    @Override
    protected void addTags(HolderLookup.Provider wrapperLookup) {
        tag(Patterns.Tags.TEST_PATTERNS)
            .add(Patterns.CORE, Patterns.CHECKER, Patterns.STRIPED);
        tag(Patterns.Tags.BASE_SET_PATTERNS)
            .add(Patterns.STRIPY, Patterns.SHINY);
    }
}
