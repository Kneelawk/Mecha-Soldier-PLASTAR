package com.github.plastar.data;

import net.minecraft.network.chat.Component;

import java.util.Map;

/**
 * A record holding all the data for a specific built instance of a Mecha Soldier kit.
 * TODO: color data
 * @param name The name of this mecha, determined by the player.
 * @param parts The parts used to build this mecha.
 */
public record Mecha(Component name, Map<MechaSection, MechaPart> parts) {
}
