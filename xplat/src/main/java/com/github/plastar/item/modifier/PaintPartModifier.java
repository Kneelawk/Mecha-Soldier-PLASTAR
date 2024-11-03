package com.github.plastar.item.modifier;

import java.util.List;
import java.util.Optional;

import com.github.plastar.data.MechaPart;
import com.github.plastar.data.PRegistries;
import com.github.plastar.data.Palette;
import com.github.plastar.data.Pattern;
import com.github.plastar.item.PComponents;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import net.minecraft.core.Holder;
import net.minecraft.resources.RegistryFixedCodec;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.functions.LootItemConditionalFunction;
import net.minecraft.world.level.storage.loot.functions.LootItemFunctionType;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;

public class PaintPartModifier extends LootItemConditionalFunction {
    public static final MapCodec<PaintPartModifier> CODEC = RecordCodecBuilder.mapCodec(
        instance -> commonFields(instance)
            .and(instance.group(
                RegistryFixedCodec.create(PRegistries.PALETTE).optionalFieldOf("palette").forGetter(modifier -> modifier.palette),
                RegistryFixedCodec.create(PRegistries.PATTERN).optionalFieldOf("pattern").forGetter(modifier -> modifier.pattern)
            ))
            .apply(instance, PaintPartModifier::new)
    );
    
    private final Optional<Holder<Palette>> palette;
    private final Optional<Holder<Pattern>> pattern;
    
    PaintPartModifier(List<LootItemCondition> predicates, 
                      Optional<Holder<Palette>> palette,
                      Optional<Holder<Pattern>> pattern) {
        super(predicates);
        this.palette = palette;
        this.pattern = pattern;
    }

    @Override
    public LootItemFunctionType<? extends LootItemConditionalFunction> getType() {
        return PLootFunctions.PAINT_PART.get();
    }

    @Override
    protected ItemStack run(ItemStack stack, LootContext context) {
        var part = stack.get(PComponents.MECHA_PART.get());
        if (part == null) return stack;
        
        stack.set(PComponents.MECHA_PART.get(), new MechaPart(
            part.definition(), part.material(), pattern.orElse(part.pattern()), palette.orElse(part.palette())));
        
        return stack;
    }
}
