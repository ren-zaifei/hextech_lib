package com.renzaifei.hextechlib.card;

import com.renzaifei.hextechlib.HextechLib;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.attachment.AttachmentType;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.NeoForgeRegistries;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.function.Supplier;

public class HCardAttachment {
    private static final DeferredRegister<AttachmentType<?>> ATTACHMENT_TYPES =
            DeferredRegister.create(NeoForgeRegistries.ATTACHMENT_TYPES, HextechLib.MODID);

    public static void register(IEventBus modEventBus) {
        ATTACHMENT_TYPES.register(modEventBus);
    }

    public static final Supplier<AttachmentType<List<ResourceLocation>>> PLAYER_HCARD_IDS =
            ATTACHMENT_TYPES.register(
                    "player_hcard_ids",
                    () -> AttachmentType.<List<ResourceLocation>>builder(() -> new ArrayList<>())
                            .serialize(ResourceLocation.CODEC.listOf())
                            .copyOnDeath()
                            .build()
            );

    public static List<ResourceLocation> get(ServerPlayer player) {
        return Collections.unmodifiableList(player.getData(PLAYER_HCARD_IDS));
    }

    public static void set(ServerPlayer player, List<ResourceLocation> newList) {
        player.setData(PLAYER_HCARD_IDS, new ArrayList<>(newList != null ? newList : List.of()));
    }

    public static void add(ServerPlayer player, ResourceLocation id) {
        if (id == null) return;
        List<ResourceLocation> list = new ArrayList<>(player.getData(PLAYER_HCARD_IDS));
        if (!list.contains(id)) {
            list.add(id);
            player.setData(PLAYER_HCARD_IDS, list);
        }
    }

    public static void remove(ServerPlayer player, ResourceLocation id) {
        if (id == null) return;
        List<ResourceLocation> list = new ArrayList<>(player.getData(PLAYER_HCARD_IDS));
        if (list.remove(id)) {
            player.setData(PLAYER_HCARD_IDS, list);
        }
    }

    public static void clear(ServerPlayer player) {
        player.setData(PLAYER_HCARD_IDS, new ArrayList<>());
    }

    public static boolean contains(ServerPlayer player, ResourceLocation id) {
        return id != null && player.getData(PLAYER_HCARD_IDS).contains(id);
    }

    public static int size(ServerPlayer player) {
        return player.getData(PLAYER_HCARD_IDS).size();
    }

}
