package com.renzaifei.hextechlib;


import com.renzaifei.hextechlib.card.HCard;
import com.renzaifei.hextechlib.card.HexCardRegistry;
import com.renzaifei.hextechlib.network.NetworkHandler;
import com.renzaifei.hextechlib.network.PacketOpenCardSelection;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;

import java.util.List;

public class HexTriggers {
    private HexTriggers() {}

    public static void selection(ServerPlayer player, HCard.Rarity rarity) {
        List<HCard> choices = HexCardRegistry.getRandomThreeCards(rarity);
        if (choices.isEmpty()) {
            player.displayClientMessage(Component.translatable("gui.hextech_lib.error.empty", rarity.name()), true);
            return;
        }
        NetworkHandler.sendToClient(new PacketOpenCardSelection(choices), player);
    }
    public static HCard createDebugCard() {
        return new HCard(
                ResourceLocation.fromNamespaceAndPath("hextech_lib", "debug"),
                "hextech_lib.card.debug.title",
                "hextech_lib.card.debug.description",
                HCard.Rarity.COMMON,
                net.minecraft.world.item.Items.BARRIER.getDefaultInstance(),
                p -> {}
        );
    }
}
