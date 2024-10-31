package com.github.plastar.item;

import com.github.plastar.sound.PSounds;

import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.SlotAccess;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ClickAction;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;

import java.util.List;

public class NippersItem extends Item {
    public NippersItem(Properties properties) {
        super(properties);
    }

    @Override
    public boolean overrideStackedOnOther(ItemStack stack, Slot slot, ClickAction action, Player player) {
        ItemStack other = slot.getItem();
        if (action == ClickAction.SECONDARY && other.has(PComponents.PART_SNIPS.get())) {
            int snipsLeft = other.get(PComponents.PART_SNIPS.get());
            if (snipsLeft > 1) {
                other.set(PComponents.PART_SNIPS.get(), snipsLeft-1);
                player.playNotifySound(PSounds.RUNNER_SNIP.get(), SoundSource.PLAYERS, 0.9f, player.getRandom().nextFloat()*0.1f+0.9f);
            } else {
                other.remove(PComponents.PART_SNIPS.get());
                player.playNotifySound(PSounds.RUNNER_DROP.get(), SoundSource.PLAYERS, 0.9f, player.getRandom().nextFloat()*0.1f+0.9f);
            }
            //TODO: take damage? that seems to require an equipment slot...
            return true;
        }
        return super.overrideStackedOnOther(stack, slot, action, player);
    }

    @Override
    public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltipComponents,
                                TooltipFlag tooltipFlag) {
        super.appendHoverText(stack, context, tooltipComponents, tooltipFlag);
        tooltipComponents.add(Component.translatable("tooltip.nippers.description").withColor(7977658));
    }
}
