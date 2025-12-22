package com.renzaifei.hextechlib;


import com.renzaifei.hextechlib.card.HCard;
import com.renzaifei.hextechlib.card.HexCardRegistry;
import com.renzaifei.hextechlib.network.PackOpenChooseUI;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.Items;
import net.neoforged.neoforge.network.PacketDistributor;

import java.util.List;

public class HexTriggers {
    private HexTriggers() {}

    public static void selection(ServerPlayer player, HCard.Rarity rarity) {
        List<ResourceLocation> ids = HexCardRegistry.getRandomThreeIds(rarity);
        if (ids.isEmpty()) {
            player.displayClientMessage(Component.translatable("gui.hextech_lib.error.empty", rarity.name()), true);
            return;
        }
        PacketDistributor.sendToPlayer(player,new PackOpenChooseUI(1,ids,null));
    }

    public static HCard createDebugCard() {
        return new HCard(
                ResourceLocation.fromNamespaceAndPath("hextech_lib", "debug"),
                "hextech_lib.card.debug.title",
                "hextech_lib.card.debug.description",
                HCard.Rarity.COMMON,
                Items.BARRIER.getDefaultInstance(),
                p -> {}
        );
    }
}
