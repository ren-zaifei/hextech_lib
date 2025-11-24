package com.renzaifei.hextechlib;

import com.renzaifei.hextechlib.card.HexCardRegistry;
import com.renzaifei.hextechlib.command.HextechCommand;
import com.renzaifei.hextechlib.network.NetworkHandler;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.RegisterCommandsEvent;
import org.slf4j.Logger;

import com.mojang.logging.LogUtils;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;

@Mod(HextechLib.MODID)
public class HextechLib {
    public static final String MODID = "hextech_lib";
    public static final Logger LOGGER = LogUtils.getLogger();

    public HextechLib(IEventBus modEventBus) {
        HexCardRegistry.register(modEventBus);
        NeoForge.EVENT_BUS.addListener(this::registerCommands);
        modEventBus.addListener(this::commonSetup);
        modEventBus.addListener(NetworkHandler::register);
    }

    private void commonSetup(FMLCommonSetupEvent event) {
        LOGGER.info("go go go 出发喽");
    }

    private void registerCommands(RegisterCommandsEvent event) {
        HextechCommand.register(event.getDispatcher());
    }

}
