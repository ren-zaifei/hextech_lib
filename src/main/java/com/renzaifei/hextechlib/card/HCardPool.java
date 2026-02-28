package com.renzaifei.hextechlib.card;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.player.Player;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.loading.FMLEnvironment;

import java.util.*;

public class HCardPool {
    private static final Map<HCard.Rarity, List<ResourceLocation>> ID_POOLS = new EnumMap<>(HCard.Rarity.class);
    private static final Map<ResourceLocation, HCard> BY_ID = new HashMap<>();

    static {
        for (HCard.Rarity rarity : HCard.Rarity.values()) {
            ID_POOLS.put(rarity, new ArrayList<>());
        }
    }

    public static void register(IEventBus bus) {}

    public static void registerHCard(HCard card) {
        ID_POOLS.get(card.getRarity()).add(card.getId());
        BY_ID.put(card.getId(), card);
    }

    public static List<ResourceLocation> getHCard(HCard.Rarity rarity,Player player){
        RandomSource random =  player.getRandom();
        List<ResourceLocation> pool = ID_POOLS.getOrDefault(rarity, List.of());
        if (pool.isEmpty()) {
            return List.of();
        }
        int n = pool.size();
        int k = Math.min(3, n);
        List<ResourceLocation> list = new ArrayList<>(pool);
        for (int i = 0; i < k; i++) {
            int j = i + random.nextInt(n - i);
            Collections.swap(list, i, j);
        }
        return new ArrayList<>(list.subList(0, k));
    }

    public static List<ResourceLocation> getHCard(HCard.Rarity rarity, Player player,int count){
        RandomSource random =  player.getRandom();
        List<ResourceLocation> pool = ID_POOLS.getOrDefault(rarity, List.of());
        if (pool.isEmpty() || count <= 0) {
            return List.of();
        }
        int n = pool.size();
        int k = Math.min(count, n);
        List<ResourceLocation> list = new ArrayList<>(pool);
        for (int i = 0; i < k; i++) {
            int j = i + random.nextInt(n - i);
            Collections.swap(list, i, j);
        }
        return new ArrayList<>(list.subList(0, k));
    }

    public static HCard getByID(ResourceLocation id){return BY_ID.get(id);}

    public static List<HCard> getAllCard(HCard.Rarity rarity) {
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

    //这里的代码来自于EvanHsieh0415的issues
    public static void removeHCard(ResourceLocation id){
        HCard card = BY_ID.remove(id);
        if (card == null) {
            return;
        }
        List<ResourceLocation> pool = ID_POOLS.get(card.getRarity());
        if (pool != null) {
            pool.remove(id);
        }
    }

}
