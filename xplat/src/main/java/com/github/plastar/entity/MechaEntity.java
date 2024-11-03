package com.github.plastar.entity;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import com.github.plastar.data.Mecha;
import com.github.plastar.data.program.Mecha2dArea;
import com.github.plastar.data.program.MechaProgram;
import com.github.plastar.item.PComponents;
import com.github.plastar.item.PItems;

import net.tslat.smartbrainlib.api.SmartBrainOwner;
import net.tslat.smartbrainlib.api.core.BrainActivityGroup;
import net.tslat.smartbrainlib.api.core.SmartBrainProvider;
import net.tslat.smartbrainlib.api.core.behaviour.FirstApplicableBehaviour;
import net.tslat.smartbrainlib.api.core.behaviour.OneRandomBehaviour;
import net.tslat.smartbrainlib.api.core.behaviour.custom.attack.AnimatableMeleeAttack;
import net.tslat.smartbrainlib.api.core.behaviour.custom.look.LookAtTarget;
import net.tslat.smartbrainlib.api.core.behaviour.custom.misc.Idle;
import net.tslat.smartbrainlib.api.core.behaviour.custom.move.MoveToWalkTarget;
import net.tslat.smartbrainlib.api.core.behaviour.custom.path.SetRandomWalkTarget;
import net.tslat.smartbrainlib.api.core.behaviour.custom.path.SetWalkTargetToAttackTarget;
import net.tslat.smartbrainlib.api.core.behaviour.custom.target.InvalidateAttackTarget;
import net.tslat.smartbrainlib.api.core.behaviour.custom.target.SetPlayerLookTarget;
import net.tslat.smartbrainlib.api.core.behaviour.custom.target.SetRandomLookTarget;
import net.tslat.smartbrainlib.api.core.behaviour.custom.target.TargetOrRetaliate;
import net.tslat.smartbrainlib.api.core.sensor.ExtendedSensor;
import net.tslat.smartbrainlib.api.core.sensor.vanilla.HurtBySensor;
import net.tslat.smartbrainlib.api.core.sensor.vanilla.NearbyLivingEntitySensor;
import net.tslat.smartbrainlib.api.core.sensor.vanilla.NearbyPlayersSensor;

import org.jetbrains.annotations.Nullable;

import net.minecraft.core.component.DataComponents;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtOps;
import net.minecraft.nbt.Tag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.resources.RegistryOps;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.OwnableEntity;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.Brain;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.monster.Enemy;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.CustomData;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

public class MechaEntity extends PathfinderMob implements SmartBrainOwner<MechaEntity>, OwnableEntity {
    private static final EntityDataAccessor<Mecha> MECHA_DATA_ACCESSOR =
        SynchedEntityData.defineId(MechaEntity.class, PEntities.MECHA_DATA_SERIALIZER);
    private static final EntityDataAccessor<Optional<MechaProgram>> MECHA_PROGRAM_ACCESSOR =
        SynchedEntityData.defineId(MechaEntity.class, PEntities.MECHA_PROGRAM_SERIALIZER);
    private static final EntityDataAccessor<Optional<UUID>> OWNER_UUID_ACCESSOR =
        SynchedEntityData.defineId(MechaEntity.class, EntityDataSerializers.OPTIONAL_UUID);
    private Mecha lastMecha = getMecha();

    protected MechaEntity(EntityType<? extends MechaEntity> entityType, Level level) {
        super(entityType, level);
    }

    public static AttributeSupplier.Builder createAttributes() {
        return createMobAttributes().add(Attributes.MOVEMENT_SPEED, 0.25).add(Attributes.ATTACK_DAMAGE, 2.0)
            .add(Attributes.ENTITY_INTERACTION_RANGE, 0);
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {
        super.defineSynchedData(builder);
        builder.define(MECHA_DATA_ACCESSOR, Mecha.getDefault(level().registryAccess()));
        builder.define(MECHA_PROGRAM_ACCESSOR, Optional.empty());
        builder.define(OWNER_UUID_ACCESSOR, Optional.empty());
    }

    @Override
    protected Brain.Provider<?> brainProvider() {
        return new SmartBrainProvider<>(this);
    }

    @Override
    public List<? extends ExtendedSensor<? extends MechaEntity>> getSensors() {
        return List.of(
            new NearbyLivingEntitySensor<>(),
            new HurtBySensor<>(),
            new NearbyPlayersSensor<>()
        );
    }

    @Override
    public BrainActivityGroup<? extends MechaEntity> getCoreTasks() {
        return BrainActivityGroup.coreTasks(
            new StayWithinAreaTarget<>().withinPredicate(
                pos -> entityData.get(MECHA_PROGRAM_ACCESSOR).flatMap(MechaProgram::area)
                    .map(a -> a.isWithin(pos)).orElse(true)).nearestProvider(
                e -> entityData.get(MECHA_PROGRAM_ACCESSOR).flatMap(MechaProgram::area)
                    .map(a -> a.nearest(e.position())).orElse(e.position())),
            new LookAtTarget<>(),
            new MoveToWalkTarget<>()
        );
    }

    @SuppressWarnings("unchecked") // For some reason SBL doesn't use @SafeVarargs
    @Override
    public BrainActivityGroup<? extends MechaEntity> getIdleTasks() {
        return BrainActivityGroup.idleTasks(
            new FirstApplicableBehaviour<MechaEntity>(
                new TargetOrRetaliate<>().attackablePredicate(entity -> {
                    Optional<Mecha2dArea> area = entityData.get(MECHA_PROGRAM_ACCESSOR).flatMap(MechaProgram::area);
                    if (area.isPresent() && !area.get().isWithin(entity.position())) return false;
                    return entity instanceof Enemy;
                }),
                new SetPlayerLookTarget<>(),
                new SetRandomLookTarget<>()),
            new OneRandomBehaviour<MechaEntity>(
                new SetRandomWalkTarget<>().walkTargetPredicate(
                    (mob, pos) -> entityData.get(MECHA_PROGRAM_ACCESSOR).flatMap(MechaProgram::area)
                        .map(a -> a.isWithin(pos)).orElse(true)),
                new Idle<>().runFor(entity -> entity.getRandom().nextInt(30, 60)))
        );
    }

    @Override
    public BrainActivityGroup<? extends MechaEntity> getFightTasks() {
        return BrainActivityGroup.fightTasks(
            new InvalidateAttackTarget<>(),
            new SetWalkTargetToAttackTarget<>(),
            new AnimatableMeleeAttack<>(5).whenStarting(entity -> setAggressive(true))
                .whenStopping(entity -> setAggressive(false))
        );
    }

    @Override
    protected AABB getAttackBoundingBox() {
        double attackRange = getAttributeValue(Attributes.ENTITY_INTERACTION_RANGE);
        return super.getAttackBoundingBox().inflate(attackRange, 0.0, attackRange);
    }

    @Override
    public void tick() {
        super.tick();
        if (!level().isClientSide && getMecha() != lastMecha) {
            lastMecha.forEachAttributeModifier((attribute, modifier) -> {
                var instance = getAttribute(attribute);
                if (instance != null) {
                    instance.removeModifier(modifier.id());
                }
            });
            lastMecha = getMecha();
            lastMecha.forEachAttributeModifier((attribute, modifier) -> {
                var instance = getAttribute(attribute);
                if (instance != null) {
                    instance.addTransientModifier(modifier);
                }
            });
        }
    }

    @Override
    public void addAdditionalSaveData(CompoundTag compound) {
        super.addAdditionalSaveData(compound);
        RegistryOps<Tag> ops = RegistryOps.create(NbtOps.INSTANCE, registryAccess());
        compound.put("mecha",
            Mecha.CODEC.encodeStart(ops, getMecha()).getOrThrow());
        Optional<MechaProgram> program = getProgram();
        program.ifPresent(mechaProgram -> compound.put("program",
            MechaProgram.CODEC.encodeStart(ops, mechaProgram)
                .getOrThrow()));
        UUID owner = getOwnerUUID();
        if (owner != null) {
            compound.putUUID("owner", owner);
        }
    }

    @Override
    public void readAdditionalSaveData(CompoundTag compound) {
        super.readAdditionalSaveData(compound);
        RegistryOps<Tag> ops = RegistryOps.create(NbtOps.INSTANCE, registryAccess());
        Mecha.CODEC.parse(ops, compound.get("mecha"))
            .ifSuccess(this::setMecha);

        Optional<MechaProgram> program;
        if (compound.contains("program")) {
            program = MechaProgram.CODEC.parse(ops, compound.get("program")).resultOrPartial();
        } else {
            program = Optional.empty();
        }
        setProgram(program);

        if (compound.hasUUID("owner")) {
            setOwnerUUID(compound.getUUID("owner"));
        } else {
            setOwnerUUID(null);
        }
    }

    @Override
    protected void customServerAiStep() {
        tickBrain(this);
    }

    @Override
    protected InteractionResult mobInteract(Player player, InteractionHand hand) {
        if (player.isShiftKeyDown()) {
            var stack = PItems.MECHA.get().getDefaultInstance();
            CustomData.update(DataComponents.BUCKET_ENTITY_DATA, stack, compound -> {
                compound.put("mecha",
                    Mecha.CODEC.encodeStart(RegistryOps.create(NbtOps.INSTANCE, registryAccess()), getMecha())
                        .getOrThrow());
                compound.putFloat("Health", getHealth());
            });
            if (player.getItemInHand(hand).isEmpty()) {
                player.setItemInHand(hand, stack);
            } else {
                player.getInventory().placeItemBackInInventory(stack);
            }
            discard();
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

    public Optional<MechaProgram> getProgram() {
        return entityData.get(MECHA_PROGRAM_ACCESSOR);
    }

    public void setProgram(Optional<MechaProgram> program) {
        entityData.set(MECHA_PROGRAM_ACCESSOR, program);
    }

    @Override
    public @Nullable UUID getOwnerUUID() {
        return entityData.get(OWNER_UUID_ACCESSOR).orElse(null);
    }

    public void setOwnerUUID(@Nullable UUID uuid) {
        entityData.set(OWNER_UUID_ACCESSOR, Optional.ofNullable(uuid));
    }

    @Override
    public InteractionResult interactAt(Player player, Vec3 vec, InteractionHand hand) {
        ItemStack usedStack = player.getItemInHand(hand);

        if (usedStack.is(PItems.PUNCH_CARD.get())) {
            Optional<UUID> owner = entityData.get(OWNER_UUID_ACCESSOR);
            if (owner.isPresent() && !owner.get().equals(player.getUUID())) return InteractionResult.FAIL;

            if (level().isClientSide()) return InteractionResult.SUCCESS;

            MechaProgram program = usedStack.getOrDefault(PComponents.MECHA_PROGRAM.get(), MechaProgram.DEFAULT);
            Optional<MechaProgram> prev = entityData.get(MECHA_PROGRAM_ACCESSOR);
            entityData.set(MECHA_PROGRAM_ACCESSOR, Optional.of(program));

            ItemStack prevStack = ItemStack.EMPTY;
            if (prev.isPresent()) {
                prevStack = new ItemStack(PItems.PUNCH_CARD.get());
                prevStack.set(PComponents.MECHA_PROGRAM.get(), prev.get());
            }
            player.setItemInHand(hand, prevStack);
            return InteractionResult.CONSUME;
        } else if (usedStack.isEmpty()) {
            Optional<UUID> owner = entityData.get(OWNER_UUID_ACCESSOR);
            if (owner.isPresent() && !owner.get().equals(player.getUUID())) return InteractionResult.FAIL;

            Optional<MechaProgram> prev = entityData.get(MECHA_PROGRAM_ACCESSOR);
            if (prev.isEmpty()) return InteractionResult.FAIL;

            if (level().isClientSide()) return InteractionResult.SUCCESS;

            entityData.set(MECHA_PROGRAM_ACCESSOR, Optional.empty());
            ItemStack prevStack = new ItemStack(PItems.PUNCH_CARD.get());
            prevStack.set(PComponents.MECHA_PROGRAM.get(), prev.get());
            player.setItemInHand(hand, prevStack);
            return InteractionResult.CONSUME;
        }

        return super.interactAt(player, vec, hand);
    }

    @Override
    protected void dropCustomDeathLoot(ServerLevel level, DamageSource damageSource, boolean recentlyHit) {
        Optional<MechaProgram> program = getProgram();
        if (program.isPresent()) {
            ItemStack programStack = new ItemStack(PItems.PUNCH_CARD.get());
            programStack.set(PComponents.MECHA_PROGRAM.get(), program.get());
            spawnAtLocation(programStack);
        }
        super.dropCustomDeathLoot(level, damageSource, recentlyHit);
    }
}
