package com.renzaifei.hextechlib.client;


import com.renzaifei.hextechlib.card.HCard;
import com.renzaifei.hextechlib.card.HexCardRegistry;
import com.renzaifei.hextechlib.network.PackOpenChooseUI;
import com.renzaifei.hextechlib.ui.CardSelectionScreen;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.neoforge.network.handling.IPayloadContext;

import java.util.ArrayList;
import java.util.List;

@OnlyIn(Dist.CLIENT)
public class ClientPacketHandler {
    public static void handle(final PackOpenChooseUI data, final IPayloadContext context) {
        context.enqueueWork(() -> {
            PackOpenChooseUI.receive(data);
            if (data.flag == 1) {
                List<ResourceLocation> cardsID = PackOpenChooseUI.getCardsIDs();
                List<HCard> cards = new ArrayList<>();
                cardsID.forEach(cardID -> {
                    cards.add(HexCardRegistry.getById(cardID));
                });
                Minecraft.getInstance().setScreen(new CardSelectionScreen(cards));
            }
        });
    }
}
