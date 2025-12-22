package com.renzaifei.hextechlib.network;


import com.renzaifei.hextechlib.card.HCard;
import com.renzaifei.hextechlib.card.HexCardRegistry;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.neoforged.neoforge.network.handling.IPayloadContext;


public class ServerPacketHandler {
    public static void handlePacket(final PackChooseCard data, final IPayloadContext context) {
        context.enqueueWork(() -> {
            PackChooseCard.receive(data);
            if (!(context.player() instanceof ServerPlayer player)) {
                return;
            }
            ResourceLocation chosenCardId = PackChooseCard.getCardID();
            HCard chosenCard = HexCardRegistry.getById(chosenCardId);

            if (chosenCard != null) {
                chosenCard.applyEffect().accept(player);

                player.displayClientMessage(
                        Component.translatable("network.hextech_lib.packet.msg", chosenCard.title()),
                        true
                );

                player.playSound(
                        SoundEvents.PLAYER_LEVELUP,
                        1.2F,
                        1.2F
                );

                PackChooseCard.cardID = null;
                PackChooseCard.uuid = null;
            }
        });
    }
}
