package com.renzaifei.hextechlib.network;

import com.renzaifei.hextechlib.HextechLib;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import net.neoforged.neoforge.network.registration.PayloadRegistrar;

@EventBusSubscriber(modid = HextechLib.MODID,value = Dist.DEDICATED_SERVER)
public class ServerEmptyPacketRegister {
    @SubscribeEvent
    public static void register(final RegisterPayloadHandlersEvent event){
        final PayloadRegistrar registrar = event.registrar(HextechLib.MODID);
        registrar.playToClient(PackOpenChooseUI.TYPE,
                PackOpenChooseUI.STREAM_CODEC,
                (payload, context) -> {});
    }
}
