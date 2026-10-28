package com.renzaifei.hextechlib.card;

import com.renzaifei.hextechlib.HextechLib;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;

import java.util.List;

public final class DebugCards {
    private static final List<Definition> DEFINITIONS = List.of(
            new Definition("common_feather", HCard.Rarity.COMMON, "feather"),
            new Definition("common_shield", HCard.Rarity.COMMON, "shield"),
            new Definition("common_bread", HCard.Rarity.COMMON, "bread"),
            new Definition("rare_emerald", HCard.Rarity.RARE, "emerald"),
            new Definition("rare_boots", HCard.Rarity.RARE, "iron_boots"),
            new Definition("rare_compass", HCard.Rarity.RARE, "compass"),
            new Definition("epic_amethyst", HCard.Rarity.EPIC, "amethyst_shard"),
            new Definition("epic_pearl", HCard.Rarity.EPIC, "ender_pearl"),
            new Definition("epic_book", HCard.Rarity.EPIC, "enchanted_book"),
            new Definition("legendary_star", HCard.Rarity.LEGENDARY, "nether_star"),
            new Definition("legendary_totem", HCard.Rarity.LEGENDARY, "totem_of_undying"),
            new Definition("legendary_elytra", HCard.Rarity.LEGENDARY, "elytra")
    );

    private DebugCards() {}

    public static void register() {
        for (Definition definition : DEFINITIONS) {
            ResourceLocation id = ResourceLocation.fromNamespaceAndPath(HextechLib.MODID, definition.id());
            if (HCardPool.getByID(id) == null) {
                HCardPool.registerHCard(new PreviewCard(id, definition));
            }
        }
    }

    static List<Definition> definitions() {
        return DEFINITIONS;
    }

    record Definition(String name, HCard.Rarity rarity, String icon) {
        String id() {
            return "test_" + name;
        }

        String titleKey() {
            return "hextech_lib.card.test." + name + ".title";
        }

        String descriptionKey() {
            return "hextech_lib.card.test." + name + ".description";
        }
    }

    private static final class PreviewCard extends HCard {
        private PreviewCard(ResourceLocation id, Definition definition) {
            super(id, definition.titleKey(), definition.descriptionKey(), definition.rarity(),
                    BuiltInRegistries.ITEM.get(ResourceLocation.withDefaultNamespace(definition.icon())).getDefaultInstance());
        }

        @Override
        public void applyEffect(Player player) {}

        @Override
        public void reload(Player oldPlayer, Player newPlayer) {}

        @Override
        public void disconnect(Player player) {}
    }
}
