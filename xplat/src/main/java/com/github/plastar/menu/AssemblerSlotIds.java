package com.github.plastar.menu;

import com.github.plastar.data.MechaSection;

import org.jetbrains.annotations.Nullable;

public class AssemblerSlotIds {
    public static final int HEAD = 0;
    public static final int TORSO = 1;
    public static final int RIGHT_ARM = 2;
    public static final int LEFT_ARM = 3;
    public static final int RIGHT_LEG = 4;
    public static final int LEFT_LEG = 5;
    public static final int BACKPACK = 6;

    public static int SLOT_COUNT = 7;
    static final MechaSection[] REQUIRED_SECTIONS = {
        MechaSection.HEAD, MechaSection.TORSO,
        MechaSection.RIGHT_ARM, MechaSection.LEFT_ARM,
        MechaSection.RIGHT_LEG, MechaSection.LEFT_LEG
    };
    
    public static int getSlot(MechaSection section) {
        return switch (section) {
            case HEAD -> HEAD;
            case TORSO -> TORSO;
            case RIGHT_ARM -> RIGHT_ARM;
            case LEFT_ARM -> LEFT_ARM;
            case RIGHT_LEG -> RIGHT_LEG;
            case LEFT_LEG -> LEFT_LEG;
            case BACKPACK -> BACKPACK;
            default -> -1;
        };
    }
    
    @Nullable
    public static MechaSection getSection(int slot) {
        return switch (slot) {
            case HEAD -> MechaSection.HEAD;
            case TORSO -> MechaSection.TORSO;
            case RIGHT_ARM -> MechaSection.RIGHT_ARM;
            case LEFT_ARM -> MechaSection.LEFT_ARM;
            case RIGHT_LEG -> MechaSection.RIGHT_LEG;
            case LEFT_LEG -> MechaSection.LEFT_LEG;
            case BACKPACK -> MechaSection.BACKPACK;
            default -> null;
        };
    }
    
    private AssemblerSlotIds() {}
}
