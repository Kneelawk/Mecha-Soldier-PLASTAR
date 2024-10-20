package com.github.plastar.sound;

import java.util.function.Supplier;

import com.github.plastar.Constants;
import com.github.plastar.PLASTARMod;
import com.github.plastar.registry.Registrar;

import net.minecraft.core.registries.Registries;
import net.minecraft.sounds.SoundEvent;

public class PSounds {
    private static final Registrar<SoundEvent> REGISTRAR = PLASTARMod.REGISTRARS.get(Registries.SOUND_EVENT);

    public static final Supplier<SoundEvent> RUNNER_SNIP = REGISTRAR.register("runner_snip", () -> SoundEvent.createVariableRangeEvent(
        Constants.rl("runner_snip")));
    public static final Supplier<SoundEvent> RUNNER_DROP = REGISTRAR.register("runner_drop", () -> SoundEvent.createVariableRangeEvent(
        Constants.rl("runner_drop")));

    public static void register() {
    }
}
