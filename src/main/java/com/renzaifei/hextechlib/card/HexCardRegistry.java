package com.renzaifei.hextechlib.card;

import net.minecraft.resources.ResourceLocation;
import net.neoforged.bus.api.IEventBus;

import java.util.*;


public class HexCardRegistry {
    private static final List<HCard> COMMON_CARDS = new ArrayList<>();
    private static final List<HCard> RARE_CARDS = new ArrayList<>();
    private static final List<HCard> EPIC_CARDS = new ArrayList<>();
    private static final List<HCard> LEGENDARY_CARDS = new ArrayList<>();
    private static final Map<ResourceLocation, HCard> BY_ID = new HashMap<>();
    private static final Random RANDOM = new Random();

    public static void register(IEventBus bus) {}


    public static void register(HCard card, HCard.Rarity rarity) {
        switch (rarity) {
            case COMMON:
                COMMON_CARDS.add(card);
                BY_ID.put(card.id(), card);
                break;
            case RARE:
                RARE_CARDS.add(card);
                BY_ID.put(card.id(), card);
                break;
            case EPIC:
                EPIC_CARDS.add(card);
                BY_ID.put(card.id(), card);
                break;
            case LEGENDARY:
                LEGENDARY_CARDS.add(card);
                BY_ID.put(card.id(), card);
                break;
        }
    }

    public static List<HCard> getPool(HCard.Rarity rarity) {
        return switch (rarity) {
            case COMMON -> COMMON_CARDS;
            case RARE -> RARE_CARDS;
            case EPIC -> EPIC_CARDS;
            case LEGENDARY -> LEGENDARY_CARDS;
        };
    }
    public static List<HCard> getRandomThreeCards(HCard.Rarity rarity) {
        List<HCard> copy = new ArrayList<>(getPool(rarity));
        Collections.shuffle(copy, RANDOM);
        List<HCard> result = copy.subList(0, Math.min(3, copy.size()));
        return new ArrayList<>(result);
    }

    public static HCard getById(ResourceLocation id) {
        return BY_ID.getOrDefault(id, null);
    }
}
