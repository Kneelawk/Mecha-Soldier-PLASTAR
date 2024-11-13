package com.github.plastar.item;

import java.util.List;
import java.util.Objects;

import com.github.plastar.Log;
import com.github.plastar.data.Mecha;
import com.github.plastar.data.MechaSection;
import com.github.plastar.entity.PEntities;

import org.apache.commons.lang3.mutable.MutableBoolean;

import net.minecraft.ChatFormatting;
import net.minecraft.commands.arguments.EntityAnchorArgument;
import net.minecraft.core.Direction;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.dispenser.BlockSource;
import net.minecraft.core.dispenser.DefaultDispenseItemBehavior;
import net.minecraft.core.dispenser.DispenseItemBehavior;
import net.minecraft.nbt.NbtOps;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.component.CustomData;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.block.DispenserBlock;
import net.minecraft.world.level.gameevent.GameEvent;

public class MechaItem extends Item {
    private static final DispenseItemBehavior DISPENSE_BEHAVIOUR = new DefaultDispenseItemBehavior() {
        @Override
        public ItemStack execute(BlockSource blockSource, ItemStack stack) {
            var direction = blockSource.state().getValue(DispenserBlock.FACING);

            try {
                PEntities.MECHA_ENTITY.get().spawn(blockSource.level(),
                    EntityType.appendDefaultStackConfig(
                        entity -> entity.readItemData(
                            stack.getOrDefault(DataComponents.BUCKET_ENTITY_DATA, CustomData.EMPTY).copyTag()),
                        blockSource.level(),
                        stack,
                        null),
                    blockSource.pos().relative(direction),
                    MobSpawnType.DISPENSER,
                    direction != Direction.UP,
                    false);
            } catch (Exception var6) {
                Log.LOG.error("Error while dispensing mecha from dispenser at {}", blockSource.pos(), var6);
                return ItemStack.EMPTY;
            }

            stack.shrink(1);
            blockSource.level().gameEvent(null, GameEvent.ENTITY_PLACE, blockSource.pos());
            return stack;
        }
    };
    
    public MechaItem(Properties properties) {
        super(properties);
        DispenserBlock.registerBehavior(this, DISPENSE_BEHAVIOUR);
    }

    @Override
    public InteractionResult useOn(UseOnContext context) {
        var level = context.getLevel();
        if (!(level instanceof ServerLevel serverLevel)) {
            return InteractionResult.SUCCESS;
        }

        var stack = context.getItemInHand();
        var clickedPos = context.getClickedPos();
        var side = context.getClickedFace();
        var state = level.getBlockState(clickedPos);
        var player = context.getPlayer();

        var spawnPos = state.getCollisionShape(level, clickedPos).isEmpty() ? clickedPos : clickedPos.relative(side);

        var spawned = PEntities.MECHA_ENTITY.get().spawn(
            serverLevel,
            EntityType.appendDefaultStackConfig(
                entity -> entity.readItemData(
                    stack.getOrDefault(DataComponents.BUCKET_ENTITY_DATA, CustomData.EMPTY).copyTag()),
                serverLevel,
                stack,
                player),
            spawnPos,
            MobSpawnType.BUCKET,
            true,
            !Objects.equals(clickedPos, spawnPos) && side == Direction.UP
        );
        if (spawned != null) {
            if (player != null) {
                spawned.lookAt(EntityAnchorArgument.Anchor.EYES, player.getEyePosition());
                spawned.setOwnerUUID(player.getUUID());
            }
            stack.shrink(1);
            level.gameEvent(player, GameEvent.ENTITY_PLACE, clickedPos);
        }

        return InteractionResult.CONSUME;
    }

    @Override
    public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltipComponents,
                                TooltipFlag tooltipFlag) {
        var customData = stack.getOrDefault(DataComponents.BUCKET_ENTITY_DATA, CustomData.EMPTY);

        var registries = context.registries();
        if (registries == null) return;

        customData.read(registries.createSerializationContext(NbtOps.INSTANCE), Mecha.CODEC.fieldOf("mecha"))
            .ifSuccess(mecha -> {
                if (!mecha.parts().isEmpty()) {
                    tooltipComponents.add(Component.empty());
                    tooltipComponents.add(Component.translatable("item.plastar.mecha.tooltip.parts_header")
                        .withStyle(ChatFormatting.GRAY));
                }
                for (var section : MechaSection.values()) {
                    var part = mecha.parts().get(section);
                    if (part == null) continue;
                    var partLocation = part.definition().unwrapKey().orElseThrow().location();
                    tooltipComponents.add(CommonComponents.space()
                        .append(Component.translatable(partLocation.toLanguageKey("plastar.part"))));
                }

                var needsHeader = new MutableBoolean(true);
                mecha.forEachAttributeModifier((attribute, modifier) -> {
                    if (needsHeader.booleanValue()) {
                        tooltipComponents.add(Component.empty());
                        tooltipComponents.add(Component.translatable("item.plastar.mecha.tooltip.attribute_header")
                            .withStyle(ChatFormatting.GRAY));
                        needsHeader.setFalse();
                    }
                    stack.addModifierTooltip(tooltipComponents::add, null, attribute, modifier);
                });
            });
    }
}
