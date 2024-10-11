package com.github.plastar.neoforge;

import com.github.plastar.Constants;
import com.github.plastar.PLASTARMod;

import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.neoforge.registries.RegisterEvent;

@Mod(Constants.MOD_ID)
public class NeoForgeMod {
    public NeoForgeMod(IEventBus modBus) {
        PLASTARMod.init();

        modBus.addListener(this::onInit);
        modBus.addListener(this::onRegister);
    }

    private void onInit(FMLCommonSetupEvent event) {
        event.enqueueWork(PLASTARMod::initSync);
    }

    private void onRegister(RegisterEvent event) {
        PLASTARMod.REGISTRARS.register(event.getRegistry());
    }
}
