package com.github.plastar.entity;

import com.github.plastar.data.Mecha;
import com.github.plastar.item.PItems;

import net.minecraft.core.component.DataComponents;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtOps;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.resources.RegistryOps;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.component.CustomData;
import net.minecraft.world.level.Level;

public class MechaEntity extends Mob {
    private static final EntityDataAccessor<Mecha> MECHA_DATA_ACCESSOR =
        SynchedEntityData.defineId(MechaEntity.class, PEntities.MECHA_DATA_SERIALIZER);
    private Mecha lastMecha = getMecha();

    protected MechaEntity(EntityType<? extends Mob> entityType, Level level) {
        super(entityType, level);
    }

    public static AttributeSupplier.Builder createAttributes() {
        return createMobAttributes();
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {
        super.defineSynchedData(builder);
        builder.define(MECHA_DATA_ACCESSOR, Mecha.DEFAULT);
    }

    @Override
    public void tick() {
        super.tick();
        if (level() instanceof ServerLevel serverLevel) {
            if (getMecha() != lastMecha) {
                lastMecha.forEachAttributeModifier(serverLevel.registryAccess(), (attribute, modifier) -> {
                    var instance = getAttribute(attribute);
                    if (instance != null) {
                        instance.removeModifier(modifier.id());
                    }
                });
                lastMecha = getMecha();
                lastMecha.forEachAttributeModifier(serverLevel.registryAccess(), (attribute, modifier) -> {
                    var instance = getAttribute(attribute);
                    if (instance != null) {
                        instance.addTransientModifier(modifier);
                    }
                });
            }
        }
    }

    @Override
    public void addAdditionalSaveData(CompoundTag compound) {
        super.addAdditionalSaveData(compound);
        compound.put("mecha", Mecha.CODEC.encodeStart(RegistryOps.create(NbtOps.INSTANCE, registryAccess()), getMecha()).getOrThrow());
    }

    @Override
    public void readAdditionalSaveData(CompoundTag compound) {
        super.readAdditionalSaveData(compound);
        Mecha.CODEC.parse(RegistryOps.create(NbtOps.INSTANCE, registryAccess()), compound.get("mecha")).ifSuccess(this::setMecha);
    }

    @Override
    protected InteractionResult mobInteract(Player player, InteractionHand hand) {
        if (player.isShiftKeyDown()) {
            var stack = PItems.MECHA.get().getDefaultInstance();
            CustomData.update(DataComponents.CUSTOM_DATA, stack, compound -> {
                compound.put("mecha", Mecha.CODEC.encodeStart(RegistryOps.create(NbtOps.INSTANCE, registryAccess()), getMecha()).getOrThrow());
                compound.putFloat("Health", getHealth());
            });
            if (player.getItemInHand(hand).isEmpty()) {
                player.setItemInHand(hand, stack);
            } else {
                player.getInventory().placeItemBackInInventory(stack);
            }
            return InteractionResult.SUCCESS;
        }
        
        return super.mobInteract(player, hand);
    }

    public Mecha getMecha() {
        return entityData.get(MECHA_DATA_ACCESSOR);
    }
    
    public void setMecha(Mecha mecha) {
        entityData.set(MECHA_DATA_ACCESSOR, mecha);
    }
}
