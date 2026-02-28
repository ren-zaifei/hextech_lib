package com.renzaifei.hextechlib.card;

import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;


public abstract class HCard{
    private ResourceLocation id;
    private String titleKey;
    private String descKey;
    private Rarity rarity;
    private ItemStack icon;

    public enum Rarity {
        COMMON, RARE, EPIC, LEGENDARY
    }

    protected HCard(ResourceLocation id,String titleKey,String descKey,Rarity rarity,ItemStack icon){
        this.id = id;
        this.titleKey = titleKey;
        this.descKey = descKey;
        this.rarity = rarity;
        this.icon = icon;
    }

    public ResourceLocation getId() {
        return id;
    }

    public Rarity getRarity() {
        return rarity;
    }

    public ItemStack getIcon() {
        return icon;
    }

    public Component title() { return Component.translatable(titleKey); }
    public Component description() { return Component.translatable(descKey); }

    public abstract void applyEffect(Player player);
    public abstract void reload(Player oldPlayer,Player newPlayer);
    public abstract void disconnect(Player player);
}
