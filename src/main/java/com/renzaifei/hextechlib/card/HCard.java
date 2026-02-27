package com.renzaifei.hextechlib.card;

import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

import java.util.function.Consumer;

public record HCard(
        ResourceLocation id,
        Component titleKey,
        Component descKey,
        Rarity rarity,
        ItemStack icon,
        Consumer<Player> applyEffect
) {
    public enum Rarity {
        COMMON, RARE, EPIC, LEGENDARY
    }

    public HCard(ResourceLocation id,
                 String titleKey,
                 String descKey,
                 Rarity rarity,
                 ItemStack icon,
                 Consumer<Player> applyEffect) {
        this(id, Component.translatableWithFallback(titleKey, titleKey), Component.translatableWithFallback(descKey, descKey), rarity, icon, applyEffect);
    }

    public Component title() { return titleKey; }
    public Component description() { return descKey; }
}
