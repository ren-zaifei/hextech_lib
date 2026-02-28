package com.renzaifei.hextechlib;


import com.renzaifei.hextechlib.card.HCard;
import com.renzaifei.hextechlib.card.HCardAttachment;
import com.renzaifei.hextechlib.card.HCardPool;
import com.renzaifei.hextechlib.network.PackOpenChooseUI;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.neoforged.neoforge.network.PacketDistributor;

import java.util.List;

public class HexTriggers {
    private HexTriggers() {}

    public static void selection(ServerPlayer player, HCard.Rarity rarity) {
        List<ResourceLocation> ids = HCardPool.getHCard(rarity,player);
        if (ids.isEmpty()) {
            player.displayClientMessage(Component.translatable("gui.hextech_lib.error.empty"), true);
            return;
        }
        PacketDistributor.sendToPlayer(player,new PackOpenChooseUI(1,ids));
    }

    public static void selection(ServerPlayer player, HCard.Rarity rarity,int count) {
        List<ResourceLocation> ids = HCardPool.getHCard(rarity,player,count);
        if (ids.isEmpty()) {
            player.displayClientMessage(Component.translatable("gui.hextech_lib.error.empty"), true);
            return;
        }
        PacketDistributor.sendToPlayer(player,new PackOpenChooseUI(1,ids));
    }

    public static void getControl(ServerPlayer player){
        List<ResourceLocation> ids = HCardAttachment.get(player);
        if (ids.isEmpty()){
            player.displayClientMessage(Component.translatable("gui.hextech_lib.error.empty"), true);
            return;
        }
        PacketDistributor.sendToPlayer(player,new PackOpenChooseUI(2,ids));
    }
}
