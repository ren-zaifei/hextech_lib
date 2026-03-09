package com.renzaifei.hextechlib.network;


import com.renzaifei.hextechlib.HextechLib;
import com.renzaifei.hextechlib.card.HCard;
import com.renzaifei.hextechlib.card.HCardAttachment;
import com.renzaifei.hextechlib.card.HCardPool;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import net.neoforged.neoforge.network.registration.PayloadRegistrar;

@EventBusSubscriber(modid = HextechLib.MODID)
public class ServerPacketHandler {
    @SubscribeEvent
    public static void register(final RegisterPayloadHandlersEvent event){
        final PayloadRegistrar registrar = event.registrar(HextechLib.MODID);
        registrar.playToServer(PackChooseCard.TYPE,
                PackChooseCard.STREAM_CODEC,
                (ServerPacketHandler::handlePacket));
    }

    public static void handlePacket(final PackChooseCard data, final IPayloadContext context) {
        context.enqueueWork(() -> {
            if (!(context.player() instanceof ServerPlayer player)) {
                return;
            }
            ResourceLocation chosenCardId = data.getCardID();
            HCard chosenCard = HCardPool.getByID(chosenCardId);
            if (chosenCard == null) return;
            if (data.getFlag() == 1){
                chosenCard.applyEffect(player);
                HCardAttachment.add(player,chosenCardId);

                player.displayClientMessage(
                        Component.translatable("network.hextech_lib.packet.msg", chosenCard.title()),
                        true
                );

                player.playSound(
                        SoundEvents.PLAYER_LEVELUP,
                        1.2F,
                        1.2F
                );
            }
            if (data.getFlag() == 2){
                chosenCard.disconnect(player);
                HCardAttachment.remove(player,chosenCardId);
                player.displayClientMessage(
                        Component.translatable("network.hextech_lib.packet.msg2", chosenCard.title()),
                        true
                );

                player.playSound(
                        SoundEvents.PLAYER_LEVELUP,
                        1.2F,
                        1.2F
                );
            }
        });
    }
}
