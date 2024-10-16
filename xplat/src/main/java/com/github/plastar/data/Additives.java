package com.github.plastar.data;

import java.util.ArrayList;
import java.util.List;

import com.github.plastar.Constants;

import net.minecraft.core.Holder;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;

public class Additives {
    public static final ResourceKey<Additive> REDSTONE = key("redstone");
    
    private static ResourceKey<Additive> key(String path) {
        return ResourceKey.create(PRegistries.ADDITIVE, Constants.rl(path));
    }
    
    public static void bootstrap(BootstrapContext<Additive> context) {
        register(context, REDSTONE, Ingredient.of(Items.REDSTONE))
            .addAdditiveModifier(Attributes.ATTACK_SPEED, 1)
            .addAdditiveModifier(Attributes.MOVEMENT_SPEED, 1);
    }
    
    private static Builder register(BootstrapContext<Additive> context, ResourceKey<Additive> key, Ingredient ingredient) {
        var builder = new Builder(key.location());
        context.register(key, new Additive(ingredient, builder.modifiers));
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
