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
import net.minecraft.world.entity.ai.attributes.Attributes;

public class Parts {
    public static final ResourceKey<PartDefinition> PLASTAR_HEAD = key("plastar/head");
    public static final ResourceKey<PartDefinition> PLASTAR_TORSO = key("plastar/torso");
    public static final ResourceKey<PartDefinition> PLASTAR_LEFT_ARM = key("plastar/left_arm");
    public static final ResourceKey<PartDefinition> PLASTAR_RIGHT_ARM = key("plastar/right_arm");
    public static final ResourceKey<PartDefinition> PLASTAR_LEFT_LEG = key("plastar/left_leg");
    public static final ResourceKey<PartDefinition> PLASTAR_RIGHT_LEG = key("plastar/right_leg");
    public static final ResourceKey<PartDefinition> PLASTAR_BACKPACK = key("plastar/backpack");
    
    public static final ResourceKey<PartDefinition> EXTERO_HEAD = key("extero/head");
    public static final ResourceKey<PartDefinition> EXTERO_TORSO = key("extero/torso");
    public static final ResourceKey<PartDefinition> EXTERO_LEFT_ARM = key("extero/left_arm");
    public static final ResourceKey<PartDefinition> EXTERO_RIGHT_ARM = key("extero/right_arm");
    public static final ResourceKey<PartDefinition> EXTERO_LEFT_LEG = key("extero/left_leg");
    public static final ResourceKey<PartDefinition> EXTERO_RIGHT_LEG = key("extero/right_leg");
    public static final ResourceKey<PartDefinition> EXTERO_BACKPACK = key("extero/backpack");
    
    public static final ResourceKey<PartDefinition> NEORA_HEAD = key("neora/head");
    public static final ResourceKey<PartDefinition> NEORA_TORSO = key("neora/torso");
    public static final ResourceKey<PartDefinition> NEORA_LEFT_ARM = key("neora/left_arm");
    public static final ResourceKey<PartDefinition> NEORA_RIGHT_ARM = key("neora/right_arm");
    public static final ResourceKey<PartDefinition> NEORA_LEFT_LEG = key("neora/left_leg");
    public static final ResourceKey<PartDefinition> NEORA_RIGHT_LEG = key("neora/right_leg");
    public static final ResourceKey<PartDefinition> NEORA_BACKPACK = key("neora/backpack");

    private static ResourceKey<PartDefinition> key(String path) {
        return ResourceKey.create(PRegistries.PART, Constants.rl(path));
    }

    public static void bootstrap(BootstrapContext<PartDefinition> context) {
        register(context, PLASTAR_HEAD, Patterns.Tags.BASE_SET_PATTERNS, Patterns.UNPAINTED, MechaSection.HEAD);
        register(context, PLASTAR_TORSO, Patterns.Tags.BASE_SET_PATTERNS, Patterns.UNPAINTED, MechaSection.TORSO);
        register(context, PLASTAR_LEFT_ARM, Patterns.Tags.BASE_SET_PATTERNS, Patterns.UNPAINTED, MechaSection.LEFT_ARM).addAdditiveModifier(Attributes.ENTITY_INTERACTION_RANGE, 3);
        register(context, PLASTAR_RIGHT_ARM, Patterns.Tags.BASE_SET_PATTERNS, Patterns.UNPAINTED, MechaSection.RIGHT_ARM).addAdditiveModifier(Attributes.ENTITY_INTERACTION_RANGE, 3);
        register(context, PLASTAR_LEFT_LEG, Patterns.Tags.BASE_SET_PATTERNS, Patterns.UNPAINTED, MechaSection.LEFT_LEG).addMultiplyBaseModifier(Attributes.JUMP_STRENGTH, 2);
        register(context, PLASTAR_RIGHT_LEG, Patterns.Tags.BASE_SET_PATTERNS, Patterns.UNPAINTED, MechaSection.RIGHT_LEG).addMultiplyBaseModifier(Attributes.JUMP_STRENGTH, 2);
        register(context, PLASTAR_BACKPACK, Patterns.Tags.BASE_SET_PATTERNS, Patterns.UNPAINTED, MechaSection.BACKPACK);

        register(context, EXTERO_HEAD, Patterns.Tags.BASE_SET_PATTERNS, Patterns.UNPAINTED, MechaSection.HEAD);
        register(context, EXTERO_TORSO, Patterns.Tags.BASE_SET_PATTERNS, Patterns.UNPAINTED, MechaSection.TORSO);
        register(context, EXTERO_LEFT_ARM, Patterns.Tags.BASE_SET_PATTERNS, Patterns.UNPAINTED, MechaSection.LEFT_ARM).addAdditiveModifier(Attributes.ENTITY_INTERACTION_RANGE, 1).addAdditiveModifier(Attributes.ATTACK_DAMAGE, 6);
        register(context, EXTERO_RIGHT_ARM, Patterns.Tags.BASE_SET_PATTERNS, Patterns.UNPAINTED, MechaSection.RIGHT_ARM).addAdditiveModifier(Attributes.ENTITY_INTERACTION_RANGE, 1).addAdditiveModifier(Attributes.ATTACK_DAMAGE, 6);
        register(context, EXTERO_LEFT_LEG, Patterns.Tags.BASE_SET_PATTERNS, Patterns.UNPAINTED, MechaSection.LEFT_LEG).addMultiplyBaseModifier(Attributes.MOVEMENT_SPEED, 0.2);
        register(context, EXTERO_RIGHT_LEG, Patterns.Tags.BASE_SET_PATTERNS, Patterns.UNPAINTED, MechaSection.RIGHT_LEG).addMultiplyBaseModifier(Attributes.MOVEMENT_SPEED, 0.2);
        register(context, EXTERO_BACKPACK, Patterns.Tags.BASE_SET_PATTERNS, Patterns.UNPAINTED, MechaSection.BACKPACK);

        register(context, NEORA_HEAD, Patterns.Tags.BASE_SET_PATTERNS, Patterns.UNPAINTED, MechaSection.HEAD);
        register(context, NEORA_TORSO, Patterns.Tags.BASE_SET_PATTERNS, Patterns.UNPAINTED, MechaSection.TORSO);
        register(context, NEORA_LEFT_ARM, Patterns.Tags.BASE_SET_PATTERNS, Patterns.UNPAINTED, MechaSection.LEFT_ARM).addAdditiveModifier(Attributes.ENTITY_INTERACTION_RANGE, 1).addMultiplyBaseModifier(Attributes.ATTACK_SPEED, 6);
        register(context, NEORA_RIGHT_ARM, Patterns.Tags.BASE_SET_PATTERNS, Patterns.UNPAINTED, MechaSection.RIGHT_ARM).addAdditiveModifier(Attributes.ENTITY_INTERACTION_RANGE, 1).addMultiplyBaseModifier(Attributes.ATTACK_SPEED, 6);
        register(context, NEORA_LEFT_LEG, Patterns.Tags.BASE_SET_PATTERNS, Patterns.UNPAINTED, MechaSection.LEFT_LEG).addMultiplyBaseModifier(Attributes.MOVEMENT_SPEED, 0.2);
        register(context, NEORA_RIGHT_LEG, Patterns.Tags.BASE_SET_PATTERNS, Patterns.UNPAINTED, MechaSection.RIGHT_LEG).addMultiplyBaseModifier(Attributes.MOVEMENT_SPEED, 0.2);
        register(context, NEORA_BACKPACK, Patterns.Tags.BASE_SET_PATTERNS, Patterns.UNPAINTED, MechaSection.BACKPACK);
    }
    
    private static Builder register(BootstrapContext<PartDefinition> context, ResourceKey<PartDefinition> key, 
                                    TagKey<Pattern> patterns, ResourceKey<Pattern> defaultPattern, MechaSection section) {
        var builder = new Builder(key.location());
        var patternLookup = context.lookup(PRegistries.PATTERN);
        context.register(key, new PartDefinition(patternLookup.getOrThrow(patterns), patternLookup.getOrThrow(defaultPattern), section, builder.modifiers));
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
