package com.github.plastar.item;

import java.util.List;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;

public class MechaPartItem extends Item {
    public MechaPartItem(Properties properties) {
        super(properties);
    }

    @Override
    public Component getName(ItemStack stack) {
        var part = stack.get(PComponents.MECHA_PART.get());
        if (part == null) {
            return super.getName(stack);
        } else {
            return Component.translatable(part.definition().unwrapKey().orElseThrow().location().toLanguageKey("plastar.part"));
        }
    }

    @Override
    public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltipComponents,
                                TooltipFlag tooltipFlag) {
        var part = stack.get(PComponents.MECHA_PART.get());
        if (part == null) return;
        
        tooltipComponents.add(Component.translatable(
            "item.plastar.mecha_part.tooltip.pattern", 
            Component.translatable(part.pattern().unwrapKey().orElseThrow().location().toLanguageKey("plastar.pattern"))
        ).withStyle(ChatFormatting.GRAY));
        tooltipComponents.add(Component.translatable(
            "item.plastar.mecha_part.tooltip.palette", 
            Component.translatable(part.palette().unwrapKey().orElseThrow().location().toLanguageKey("plastar.palette"))
        ).withStyle(ChatFormatting.GRAY));
    }
}
