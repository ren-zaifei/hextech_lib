package com.renzaifei.hextechlib.network;

import com.renzaifei.hextechlib.HextechLib;
import net.minecraft.server.level.ServerPlayer;
import net.neoforged.neoforge.network.PacketDistributor;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import net.neoforged.neoforge.network.registration.PayloadRegistrar;

public class NetworkHandler {
    private static final String PROTOCOL_VERSION = "1";
    public static void register(final RegisterPayloadHandlersEvent event) {
        final PayloadRegistrar registrar = event.registrar(HextechLib.MODID)
                .versioned(PROTOCOL_VERSION)
                .optional();


        registrar.playToClient(
                PacketOpenCardSelection.TYPE,
                PacketOpenCardSelection.STREAM_CODEC,
                (payload, context) -> payload.handle(context)
        );
        registrar.playToServer(
                PacketChooseCard.TYPE,
                PacketChooseCard.STREAM_CODEC,
                (packet, context) -> packet.handle(context)
        );
    }
    public static void sendToClient(PacketOpenCardSelection packet, ServerPlayer player) {
        PacketDistributor.sendToPlayer(player, packet);
    }
}
