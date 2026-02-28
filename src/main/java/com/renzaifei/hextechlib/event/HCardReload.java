package com.renzaifei.hextechlib.event;

import com.renzaifei.hextechlib.HextechLib;
import com.renzaifei.hextechlib.card.HCard;
import com.renzaifei.hextechlib.card.HCardAttachment;
import com.renzaifei.hextechlib.card.HCardPool;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;

import java.util.ArrayList;
import java.util.List;

@EventBusSubscriber(modid = HextechLib.MODID)
public class HCardReload {
    @SubscribeEvent
    public static void onReloadClone(PlayerEvent.Clone event){
        Player oldPlayer = event.getOriginal();
        Player newPlayer = event.getEntity();
        List<ResourceLocation> list = HCardAttachment.get((ServerPlayer)oldPlayer);
        List<HCard> cards = new ArrayList<>();
        if (list.isEmpty()) return;
        list.forEach(item -> {
            cards.add(HCardPool.getByID(item));
        });
        cards.forEach(item -> {
            item.reload(oldPlayer,newPlayer);
        });
    }
}
