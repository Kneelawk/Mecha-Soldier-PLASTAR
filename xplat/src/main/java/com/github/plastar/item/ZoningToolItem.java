package com.github.plastar.item;

import java.util.List;

import com.github.plastar.data.program.Mecha2dArea;

import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;

public class ZoningToolItem extends Item {
    public ZoningToolItem(Properties properties) {
        super(properties);
    }

    @Override
    public InteractionResult useOn(UseOnContext context) {
        Level level = context.getLevel();
        if (level.isClientSide()) return InteractionResult.SUCCESS;

        ItemStack stack = context.getItemInHand();
        BlockPos click = context.getClickedPos();

        BlockPos partial = stack.get(PComponents.SELECTED_POSITION.get());
        if (partial == null) {
            stack.set(PComponents.SELECTED_POSITION.get(), click);
        } else {
            Mecha2dArea area = Mecha2dArea.sorted(partial.getX(), partial.getZ(), click.getX(), click.getZ());
            stack.set(PComponents.MECHA_2D_AREA.get(), area);
            stack.remove(PComponents.SELECTED_POSITION.get());
        }

        return InteractionResult.CONSUME;
    }

    @Override
    public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltipComponents,
                                TooltipFlag tooltipFlag) {
        boolean selected = false;

        BlockPos partial = stack.get(PComponents.SELECTED_POSITION.get());
        if (partial != null) {
            selected = true;
            tooltipComponents.add(
                Component.translatable("item.plastar.zoning_tool.tooltip.selected_position", partial.getX(),
                    partial.getY(), partial.getZ()).withStyle(ChatFormatting.GRAY));
        }

        Mecha2dArea area = stack.get(PComponents.MECHA_2D_AREA.get());
        if (area != null) {
            selected = true;
            tooltipComponents.add(
                Component.translatable("item.plastar.zoning_tool.tooltip.selected_area_2d", area.x0(), area.z0(),
                    area.x1(), area.z1()).withStyle(ChatFormatting.WHITE));
        }

        if (!selected) {
            tooltipComponents.add(Component.translatable("item.plastar.zoning_tool.tooltip.no_selection")
                .withStyle(ChatFormatting.DARK_GRAY));
        }
    }

    @Override
    public boolean isFoil(ItemStack stack) {
        return stack.has(PComponents.SELECTED_POSITION.get());
    }
}
