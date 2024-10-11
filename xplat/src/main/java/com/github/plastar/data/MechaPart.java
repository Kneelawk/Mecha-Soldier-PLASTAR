package com.github.plastar.data;

import net.minecraft.world.entity.ai.attributes.AttributeModifier;

import java.util.List;

/**
 * An individual part of a model kit.
 * TODO: record instead of interface?
 */
public interface MechaPart {
    public MechaSection getSection();
    public List<AttributeModifier> getStats();
}
