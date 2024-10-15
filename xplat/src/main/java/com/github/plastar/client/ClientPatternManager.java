package com.github.plastar.client;

import java.util.HashMap;
import java.util.Map;

import com.github.plastar.data.Palette;
import com.github.plastar.data.Pattern;

import org.jetbrains.annotations.Nullable;

import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;

public class ClientPatternManager {
    public static final ClientPatternManager INSTANCE = new ClientPatternManager();
    
    private final Map<ResourceKey<Pattern>, Pattern> patterns = new HashMap<>();
    private final Map<ResourceKey<Palette>, Palette> palettes = new HashMap<>();
    
    private ClientPatternManager() {
    }
    
    public void clear() {
        patterns.clear();
        palettes.clear();
    }
    
    public void reload(Map<ResourceKey<Pattern>, Pattern> patterns, Map<ResourceKey<Palette>, Palette> palettes) {
        clear();
        this.patterns.putAll(patterns);
        this.palettes.putAll(palettes);
    }
    
    @Nullable
    public Pattern getPattern(ResourceKey<Pattern> key) {
        return patterns.get(key);
    }
    
    @Nullable
    public Palette getPalette(ResourceKey<Palette> key) {
        return palettes.get(key);
    }
    
    public ResourceLocation getTexture(ResourceKey<Pattern> patternKey, ResourceKey<Palette> paletteKey) {
        var pattern = getPattern(patternKey);
        var palette = getPalette(paletteKey);
        if (pattern == null || palette == null) {
            return ResourceLocation.withDefaultNamespace("missingno");
        }
        
        return pattern.texture().withSuffix("_" + palette.textureSuffix());
    }
}
