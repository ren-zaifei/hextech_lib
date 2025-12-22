package com.renzaifei.hextechlib.card;

import net.minecraft.resources.ResourceLocation;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.loading.FMLEnvironment;

import java.util.*;


public class HexCardRegistry {
    private static final Map<HCard.Rarity, List<ResourceLocation>> ID_POOLS = new EnumMap<>(HCard.Rarity.class);
    private static final Map<ResourceLocation, HCard> BY_ID = new HashMap<>();
    private static final Random RANDOM = new Random();

    static {
        for (HCard.Rarity rarity : HCard.Rarity.values()) {
            ID_POOLS.put(rarity, new ArrayList<>());
        }
    }

    public static void register(IEventBus bus) {}

    public static void registerHCard(HCard card) {
        ID_POOLS.get(card.rarity()).add(card.id());
        BY_ID.put(card.id(), card);
    }

    public static List<ResourceLocation> getRandomThreeIds(HCard.Rarity rarity) {
        List<ResourceLocation> pool = ID_POOLS.getOrDefault(rarity, List.of());
        if (pool.isEmpty()) {
            return List.of();
        }
        List<ResourceLocation> copy = new ArrayList<>(pool);
        Collections.shuffle(copy, RANDOM);
        return copy.subList(0, Math.min(3, copy.size()));
    }

    public static HCard getById(ResourceLocation id) {
        return BY_ID.get(id);
    }

    public static List<HCard> getPool(HCard.Rarity rarity) {
        if (!FMLEnvironment.dist.isClient()) {
            throw new UnsupportedOperationException("getPool only available on client");
        }
        List<HCard> list = new ArrayList<>();
        for (ResourceLocation id : ID_POOLS.getOrDefault(rarity, List.of())) {
            HCard card = BY_ID.get(id);
            if (card != null) list.add(card);
        }
        return list;
    }
}
