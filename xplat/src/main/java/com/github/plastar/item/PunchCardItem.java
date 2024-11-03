package com.github.plastar.item;

import java.util.List;

import com.github.plastar.data.program.Mecha2dArea;
import com.github.plastar.data.program.MechaProgram;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;

public class PunchCardItem extends Item {
    public PunchCardItem(Properties properties) {
        super(properties);
    }

    @Override
    public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltipComponents,
                                TooltipFlag tooltipFlag) {
        boolean empty = true;

        MechaProgram program = stack.get(PComponents.MECHA_PROGRAM.get());
        if (program != null) {
            if (program.area().isPresent()) {
                empty = false;
                Mecha2dArea area = program.area().get();
                tooltipComponents.add(
                    Component.translatable("item.plastar.punch_card.tooltip.stay_within_2d", area.x0(), area.z0(),
                        area.x1(), area.z1()).withStyle(ChatFormatting.WHITE));
            }
        }

        if (empty) {
            tooltipComponents.add(Component.translatable("item.plastar.punch_card.tooltip.program_empty")
                .withStyle(ChatFormatting.DARK_GRAY));
        }
    }

    @Override
    public boolean isFoil(ItemStack stack) {
        MechaProgram program = stack.get(PComponents.MECHA_PROGRAM.get());
        if (program != null) {
            return program.area().isPresent();
        }

        return false;
    }
}
