package com.github.plastar.neoforge;

import com.github.plastar.Constants;
import com.github.plastar.PLASTARMod;

import com.github.plastar.entity.MechaEntity;
import com.github.plastar.entity.PEntities;

import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.neoforge.event.entity.EntityAttributeCreationEvent;
import net.neoforged.neoforge.registries.RegisterEvent;

@Mod(Constants.MOD_ID)
public class NeoForgeMod {
    public NeoForgeMod(IEventBus modBus) {
        PLASTARMod.init();

        modBus.addListener(this::onInit);
        modBus.addListener(this::onRegister);
        
        modBus.addListener((EntityAttributeCreationEvent event) -> event.put(PEntities.MECHA_ENTITY, MechaEntity.createAttributes().build()));
    }

    private void onInit(FMLCommonSetupEvent event) {
        event.enqueueWork(PLASTARMod::initSync);
    }

    private void onRegister(RegisterEvent event) {
        PLASTARMod.REGISTRARS.register(event.getRegistry());
    }
}
