package com.renzaifei.hextechlib.compat.kubejs.event;

import com.renzaifei.hextechlib.card.HCard;
import com.renzaifei.hextechlib.card.HexCardRegistry;
import dev.latvian.mods.kubejs.event.KubeEvent;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

import java.util.function.Consumer;

public class HCardRegisterKubeEvent implements KubeEvent {
    public HCardRegisterKubeEvent() {}

    public HCard add(HCard card) {
        HexCardRegistry.registerHCard(card);
        return card;
    }

    public HCard add(
            ResourceLocation id,
            Component title,
            Component desc,
            HCard.Rarity rarity,
            ItemStack icon,
            Consumer<Player> applyEffect) {
        return add(new HCard(id, title, desc, rarity, icon, applyEffect));
    }
}
