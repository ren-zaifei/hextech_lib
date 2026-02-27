package com.renzaifei.hextechlib.event;

import com.renzaifei.hextechlib.card.HCard;
import net.minecraft.world.entity.player.Player;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;

public class HCardEvent extends PlayerEvent {
    HCard card;

    public HCardEvent(Player player, HCard card) {
        super(player);
        this.card = card;
    }


}
