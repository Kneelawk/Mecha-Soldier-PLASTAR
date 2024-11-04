package com.github.plastar.entity;

import java.util.List;
import java.util.function.Function;
import java.util.function.Predicate;

import net.tslat.smartbrainlib.api.core.behaviour.ExtendedBehaviour;
import net.tslat.smartbrainlib.object.MemoryTest;
import net.tslat.smartbrainlib.util.BrainUtils;

import com.mojang.datafixers.util.Pair;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.memory.MemoryStatus;
import net.minecraft.world.entity.ai.memory.WalkTarget;
import net.minecraft.world.phys.Vec3;

public class StayWithinAreaTarget<E extends PathfinderMob> extends ExtendedBehaviour<E> {
    private static final MemoryTest MEMORY_REQUIREMENTS = MemoryTest.builder(1).noMemory(MemoryModuleType.WALK_TARGET);

    private Predicate<Vec3> isWithin = e -> true;
    private Function<E, Vec3> nearest = Entity::position;
    private float repositionSpeedMod = 1.3f;

    public StayWithinAreaTarget<E> withinPredicate(Predicate<Vec3> isWithin) {
        this.isWithin = isWithin;
        return this;
    }

    public StayWithinAreaTarget<E> nearestProvider(Function<E, Vec3> nearest) {
        this.nearest = nearest;
        return this;
    }

    public StayWithinAreaTarget<E> speedMod(float modifier) {
        this.repositionSpeedMod = modifier;
        return this;
    }

    @Override
    protected List<Pair<MemoryModuleType<?>, MemoryStatus>> getMemoryRequirements() {
        return MEMORY_REQUIREMENTS;
    }

    @Override
    protected boolean shouldKeepRunning(E entity) {
        return !isWithin.test(entity.position()) && !BrainUtils.hasMemory(entity, MemoryModuleType.WALK_TARGET);
    }

    @Override
    protected void tick(E entity) {
        WalkTarget memory = BrainUtils.getMemory(entity, MemoryModuleType.WALK_TARGET);

        if (!isWithin.test(entity.position()) && memory == null) {
            Vec3 nearest = this.nearest.apply(entity);
            BrainUtils.setMemory(entity, MemoryModuleType.WALK_TARGET,
                new WalkTarget(nearest, repositionSpeedMod, 0));
        }
    }
}
