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
    public static final ResourceKey<PartDefinition> HEAD = key("head");
    public static final ResourceKey<PartDefinition> TORSO = key("torso");
    public static final ResourceKey<PartDefinition> LEFT_ARM = key("left_arm");
    public static final ResourceKey<PartDefinition> RIGHT_ARM = key("right_arm");
    public static final ResourceKey<PartDefinition> LEFT_LEG = key("left_leg");
    public static final ResourceKey<PartDefinition> RIGHT_LEG = key("right_leg");
    
    public static final ResourceKey<PartDefinition> MODEL_A_HEAD = key("model_a/head");
    public static final ResourceKey<PartDefinition> MODEL_A_TORSO = key("model_a/torso");
    public static final ResourceKey<PartDefinition> MODEL_A_LEFT_ARM = key("model_a/left_arm");
    public static final ResourceKey<PartDefinition> MODEL_A_RIGHT_ARM = key("model_a/right_arm");
    public static final ResourceKey<PartDefinition> MODEL_A_LEFT_LEG = key("model_a/left_leg");
    public static final ResourceKey<PartDefinition> MODEL_A_RIGHT_LEG = key("model_a/right_leg");

    private static ResourceKey<PartDefinition> key(String path) {
        return ResourceKey.create(PRegistries.PART, Constants.rl(path));
    }

    public static void bootstrap(BootstrapContext<PartDefinition> context) {
        register(context, HEAD, Patterns.Tags.TEST_PATTERNS, MechaSection.HEAD);
        register(context, TORSO, Patterns.Tags.TEST_PATTERNS, MechaSection.TORSO);
        register(context, LEFT_ARM, Patterns.Tags.TEST_PATTERNS, MechaSection.LEFT_ARM);
        register(context, RIGHT_ARM, Patterns.Tags.TEST_PATTERNS, MechaSection.RIGHT_ARM);
        register(context, LEFT_LEG, Patterns.Tags.TEST_PATTERNS, MechaSection.LEFT_LEG);
        register(context, RIGHT_LEG, Patterns.Tags.TEST_PATTERNS, MechaSection.RIGHT_LEG);
        register(context, MODEL_A_HEAD, Patterns.Tags.TEST_PATTERNS, MechaSection.HEAD);
        register(context, MODEL_A_TORSO, Patterns.Tags.TEST_PATTERNS, MechaSection.TORSO);
        register(context, MODEL_A_LEFT_ARM, Patterns.Tags.TEST_PATTERNS, MechaSection.LEFT_ARM);
        register(context, MODEL_A_RIGHT_ARM, Patterns.Tags.TEST_PATTERNS, MechaSection.RIGHT_ARM);
        register(context, MODEL_A_LEFT_LEG, Patterns.Tags.TEST_PATTERNS, MechaSection.LEFT_LEG);
        register(context, MODEL_A_RIGHT_LEG, Patterns.Tags.TEST_PATTERNS, MechaSection.RIGHT_LEG);
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
