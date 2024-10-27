package com.github.plastar.data;

import java.util.ArrayList;
import java.util.List;

import com.github.plastar.Constants;

import net.minecraft.core.Holder;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;

public class Parts {
    public static final ResourceKey<PartDefinition> TEST_HEAD = key("test/head");
    public static final ResourceKey<PartDefinition> TEST_TORSO = key("test/torso");
    public static final ResourceKey<PartDefinition> TEST_LEFT_ARM = key("test/left_arm");
    public static final ResourceKey<PartDefinition> TEST_RIGHT_ARM = key("test/right_arm");
    public static final ResourceKey<PartDefinition> TEST_LEFT_LEG = key("test/left_leg");
    public static final ResourceKey<PartDefinition> TEST_RIGHT_LEG = key("test/right_leg");
    
    public static final ResourceKey<PartDefinition> EMMA_HEAD = key("emma/head");
    public static final ResourceKey<PartDefinition> EMMA_TORSO = key("emma/torso");
    public static final ResourceKey<PartDefinition> EMMA_LEFT_ARM = key("emma/left_arm");
    public static final ResourceKey<PartDefinition> EMMA_RIGHT_ARM = key("emma/right_arm");
    public static final ResourceKey<PartDefinition> EMMA_LEFT_LEG = key("emma/left_leg");
    public static final ResourceKey<PartDefinition> EMMA_RIGHT_LEG = key("emma/right_leg");
    public static final ResourceKey<PartDefinition> EMMA_BACKPACK = key("emma/backpack");
    
    public static final ResourceKey<PartDefinition> HAMA_HEAD = key("hama/head");
    public static final ResourceKey<PartDefinition> HAMA_TORSO = key("hama/torso");
    public static final ResourceKey<PartDefinition> HAMA_LEFT_ARM = key("hama/left_arm");
    public static final ResourceKey<PartDefinition> HAMA_RIGHT_ARM = key("hama/right_arm");
    public static final ResourceKey<PartDefinition> HAMA_LEFT_LEG = key("hama/left_leg");
    public static final ResourceKey<PartDefinition> HAMA_RIGHT_LEG = key("hama/right_leg");
    public static final ResourceKey<PartDefinition> HAMA_BACKPACK = key("hama/backpack");

    private static ResourceKey<PartDefinition> key(String path) {
        return ResourceKey.create(PRegistries.PART, Constants.rl(path));
    }

    public static void bootstrap(BootstrapContext<PartDefinition> context) {
        register(context, TEST_HEAD, Patterns.Tags.TEST_PATTERNS, MechaSection.HEAD);
        register(context, TEST_TORSO, Patterns.Tags.TEST_PATTERNS, MechaSection.TORSO);
        register(context, TEST_LEFT_ARM, Patterns.Tags.TEST_PATTERNS, MechaSection.LEFT_ARM);
        register(context, TEST_RIGHT_ARM, Patterns.Tags.TEST_PATTERNS, MechaSection.RIGHT_ARM);
        register(context, TEST_LEFT_LEG, Patterns.Tags.TEST_PATTERNS, MechaSection.LEFT_LEG);
        register(context, TEST_RIGHT_LEG, Patterns.Tags.TEST_PATTERNS, MechaSection.RIGHT_LEG);

        register(context, EMMA_HEAD, Patterns.Tags.BASE_SET_PATTERNS, MechaSection.HEAD);
        register(context, EMMA_TORSO, Patterns.Tags.BASE_SET_PATTERNS, MechaSection.TORSO);
        register(context, EMMA_LEFT_ARM, Patterns.Tags.BASE_SET_PATTERNS, MechaSection.LEFT_ARM);
        register(context, EMMA_RIGHT_ARM, Patterns.Tags.BASE_SET_PATTERNS, MechaSection.RIGHT_ARM);
        register(context, EMMA_LEFT_LEG, Patterns.Tags.BASE_SET_PATTERNS, MechaSection.LEFT_LEG);
        register(context, EMMA_RIGHT_LEG, Patterns.Tags.BASE_SET_PATTERNS, MechaSection.RIGHT_LEG);
        register(context, EMMA_BACKPACK, Patterns.Tags.BASE_SET_PATTERNS, MechaSection.BACKPACK);

        register(context, HAMA_HEAD, Patterns.Tags.BASE_SET_PATTERNS, MechaSection.HEAD);
        register(context, HAMA_TORSO, Patterns.Tags.BASE_SET_PATTERNS, MechaSection.TORSO);
        register(context, HAMA_LEFT_ARM, Patterns.Tags.BASE_SET_PATTERNS, MechaSection.LEFT_ARM);
        register(context, HAMA_RIGHT_ARM, Patterns.Tags.BASE_SET_PATTERNS, MechaSection.RIGHT_ARM);
        register(context, HAMA_LEFT_LEG, Patterns.Tags.BASE_SET_PATTERNS, MechaSection.LEFT_LEG);
        register(context, HAMA_RIGHT_LEG, Patterns.Tags.BASE_SET_PATTERNS, MechaSection.RIGHT_LEG);
        register(context, HAMA_BACKPACK, Patterns.Tags.BASE_SET_PATTERNS, MechaSection.BACKPACK);
    }
    
    private static Builder register(BootstrapContext<PartDefinition> context, ResourceKey<PartDefinition> key, 
                                    TagKey<Pattern> patterns, MechaSection section) {
        var builder = new Builder(key.location());
        context.register(key, new PartDefinition(context.lookup(PRegistries.PATTERN).getOrThrow(patterns), section, builder.modifiers));
        return builder;
    }

    private static class Builder {
        private final List<SpecializedAttributeModifier> modifiers = new ArrayList<>();
        private final ResourceLocation rl;

        private Builder(ResourceLocation rl) {
            this.rl = rl;
        }

        public Builder addAdditiveModifier(Holder<Attribute> attribute, double value) {
            modifiers.add(new SpecializedAttributeModifier(attribute, new AttributeModifier(rl, value, AttributeModifier.Operation.ADD_VALUE)));
            return this;
        }

        public Builder addMultiplyBaseModifier(Holder<Attribute> attribute, double value) {
            modifiers.add(new SpecializedAttributeModifier(attribute, new AttributeModifier(rl, value, AttributeModifier.Operation.ADD_MULTIPLIED_BASE)));
            return this;
        }

        public Builder addMultiplyTotalModifier(Holder<Attribute> attribute, double value) {
            modifiers.add(new SpecializedAttributeModifier(attribute, new AttributeModifier(rl, value, AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL)));
            return this;
        }
    }
}
