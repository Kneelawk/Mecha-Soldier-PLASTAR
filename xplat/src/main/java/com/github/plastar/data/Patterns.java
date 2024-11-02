package com.github.plastar.data;

import com.github.plastar.Constants;

import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.tags.TagKey;

public class Patterns {
    public static final ResourceKey<Pattern> STRIPY = key("base_sets/stripy");
    public static final ResourceKey<Pattern> SHINY = key("base_sets/shiny");
    public static final ResourceKey<Pattern> FLAME = key("base_sets/flame");
    public static final ResourceKey<Pattern> UNPAINTED = key("base_sets/unpainted");
    
    private static ResourceKey<Pattern> key(String path) {
        return ResourceKey.create(PRegistries.PATTERN, Constants.rl(path));
    }

    public static void bootstrap(BootstrapContext<Pattern> context) {
        context.register(STRIPY, new Pattern(Constants.rl("mecha/base_sets/stripy")));
        context.register(SHINY, new Pattern(Constants.rl("mecha/base_sets/shiny")));
        context.register(FLAME, new Pattern(Constants.rl("mecha/base_sets/flame")));
        context.register(UNPAINTED, new Pattern(Constants.rl("mecha/base_sets/unpainted")));
    }
    
    public static class Tags {
        public static final TagKey<Pattern> BASE_SET_PATTERNS = key("base_set_patterns");
        
        private static TagKey<Pattern> key(String path) {
            return TagKey.create(PRegistries.PATTERN, Constants.rl(path));
        }
    }
}
