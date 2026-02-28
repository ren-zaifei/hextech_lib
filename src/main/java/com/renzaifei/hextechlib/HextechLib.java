package com.renzaifei.hextechlib;

import com.renzaifei.hextechlib.card.HCardAttachment;
import com.renzaifei.hextechlib.card.HCardPool;
import com.renzaifei.hextechlib.client.ClientPacketHandler;
import com.renzaifei.hextechlib.command.HextechCommand;
import com.renzaifei.hextechlib.network.PackChooseCard;
import com.renzaifei.hextechlib.network.PackOpenChooseUI;
import com.renzaifei.hextechlib.network.ServerPacketHandler;
import com.renzaifei.hextechlib.test.CommonMan;
import net.neoforged.fml.loading.FMLEnvironment;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.RegisterCommandsEvent;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import net.neoforged.neoforge.network.registration.PayloadRegistrar;
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
        HCardPool.register(modEventBus);
        HCardAttachment.register(modEventBus);
        NeoForge.EVENT_BUS.addListener(this::registerCommands);
        modEventBus.addListener(this::commonSetup);
    }

    private void commonSetup(FMLCommonSetupEvent event) {
        LOGGER.info("go go go 出发喽");
        event.enqueueWork(CommonMan::registerCard);
    }

    private void registerCommands(RegisterCommandsEvent event) {
        HextechCommand.register(event.getDispatcher());
    }

}
