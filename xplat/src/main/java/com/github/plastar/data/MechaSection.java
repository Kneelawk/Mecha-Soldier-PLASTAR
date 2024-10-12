package com.github.plastar.data;

import com.mojang.serialization.Codec;

import net.minecraft.util.StringRepresentable;

/**
 * A discrete section of an assembled Mecha Soldier.
 */
public enum MechaSection implements StringRepresentable {
    HEAD("head"),
    TORSO("torso"),
    LEFT_ARM("left_arm"),
    RIGHT_ARM("right_arm"),
    LEFT_LEG("left_leg"),
    RIGHT_LEG("right_leg"),
    BACKPACK("backpack"),
    WEAPON("weapon"),
    SHIELD("shield");

    private final String name;

    MechaSection(String name) {
        this.name = name;
    }

    public static final Codec<MechaSection> CODEC = StringRepresentable.fromEnum(MechaSection::values);

    @Override
    public String getSerializedName() {
        return name;
    }
}
