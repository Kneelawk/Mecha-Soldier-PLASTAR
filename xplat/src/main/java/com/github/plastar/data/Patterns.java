package com.github.plastar.data;

import com.github.plastar.Constants;

import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.tags.TagKey;

public class Patterns {
    public static final ResourceKey<Pattern> CORE = key("core");
    public static final ResourceKey<Pattern> CHECKER = key("checker");
    public static final ResourceKey<Pattern> STRIPED = key("striped");
    
    private static ResourceKey<Pattern> key(String path) {
        return ResourceKey.create(PRegistries.PATTERN, Constants.rl(path));
    }

    public static void bootstrap(BootstrapContext<Pattern> context) {
        context.register(CHECKER, new Pattern(Constants.rl("mecha/test_part/checker")));
        context.register(STRIPED, new Pattern(Constants.rl("mecha/test_part/striped")));
        context.register(CORE, new Pattern(Constants.rl("mecha/test_part/core")));
    }
    
    public static class Tags {
        public static final TagKey<Pattern> TEST_PATTERNS = key("test_patterns");
        
        private static TagKey<Pattern> key(String path) {
            return TagKey.create(PRegistries.PATTERN, Constants.rl(path));
        }
    }
}
