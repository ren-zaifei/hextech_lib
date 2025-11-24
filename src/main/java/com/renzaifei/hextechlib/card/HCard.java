package com.renzaifei.hextechlib.card;

import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

import java.util.function.Consumer;

public record HCard(
        ResourceLocation id,
        String titleKey,
        String descKey,
        Rarity rarity,
        ItemStack icon,
        Consumer<Player> applyEffect
) {
    public enum Rarity {
        COMMON, RARE, EPIC, LEGENDARY
    }

    public Component title() { return Component.translatable(titleKey); }
    public Component description() { return Component.translatable(descKey); }
}
