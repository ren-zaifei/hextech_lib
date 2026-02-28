package com.renzaifei.hextechlib.client;


import com.renzaifei.hextechlib.HextechLib;
import com.renzaifei.hextechlib.card.HCard;
import com.renzaifei.hextechlib.card.HCardPool;
import com.renzaifei.hextechlib.network.PackOpenChooseUI;
import com.renzaifei.hextechlib.ui.CardControlScreen;
import com.renzaifei.hextechlib.ui.CardSelectionScreen;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.loading.FMLEnvironment;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import net.neoforged.neoforge.network.registration.PayloadRegistrar;

import java.util.ArrayList;
import java.util.List;


@EventBusSubscriber(modid = HextechLib.MODID,value = Dist.CLIENT)
public class ClientPacketHandler {

    @SubscribeEvent
    public static void register(final RegisterPayloadHandlersEvent event) {
        final PayloadRegistrar registrar = event.registrar(HextechLib.MODID);
        registrar.playToClient(PackOpenChooseUI.TYPE,
                PackOpenChooseUI.STREAM_CODEC,
                (ClientPacketHandler::handle)
                );
    }

    public static void handle(final PackOpenChooseUI data, final IPayloadContext context) {
        context.enqueueWork(() -> {
            if (data.flag() == 1) {
                List<ResourceLocation> cardsID = data.cards();
                List<HCard> cards = new ArrayList<>();
                cardsID.forEach(cardID -> {
                    cards.add(HCardPool.getByID(cardID));
                });
                Minecraft.getInstance().setScreen(new CardSelectionScreen(cards));
            }
            if (data.flag() == 2) {
                List<ResourceLocation> cardsID = data.cards();
                List<HCard> cards = new ArrayList<>();
                cardsID.forEach(cardID -> {
                    cards.add(HCardPool.getByID(cardID));
                });
                Minecraft.getInstance().setScreen(new CardControlScreen(cards));
            }
        });
    }
}
