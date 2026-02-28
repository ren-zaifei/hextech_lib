package com.renzaifei.hextechlib.test;

import com.renzaifei.hextechlib.HextechLib;
import com.renzaifei.hextechlib.card.HCard;
import com.renzaifei.hextechlib.card.HCardPool;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Items;

import java.util.ArrayList;
import java.util.List;

public class CommonMan {
    private static final List<HCard> cards = new ArrayList<>();

    public static void registerCard() {
        createCards();
        if (cards.isEmpty()) return;
        for (HCard card : cards) {
            HCardPool.registerHCard(card);
        }
    }

    private static void createCards(){
        cards.add(new brisksteps(
                ResourceLocation.fromNamespaceAndPath(HextechLib.MODID,"brisksteps"),
                HextechLib.MODID+".card."+HCard.Rarity.COMMON+".brisksteps."+"title",
                HextechLib.MODID+".card."+HCard.Rarity.COMMON+".brisksteps."+"description",
                HCard.Rarity.COMMON,
                Items.POTION.getDefaultInstance()
        ));
    }
}
