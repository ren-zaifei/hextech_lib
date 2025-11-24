package com.renzaifei.hextechlib.network;

import com.renzaifei.hextechlib.HextechLib;
import com.renzaifei.hextechlib.card.HCard;
import com.renzaifei.hextechlib.card.HexCardRegistry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.network.protocol.game.ClientboundSoundPacket;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.neoforged.neoforge.network.handling.IPayloadContext;

public record PacketChooseCard(ResourceLocation cardId) implements net.minecraft.network.protocol.common.custom.CustomPacketPayload {

    public static final Type<PacketChooseCard> TYPE =
            new Type<>(ResourceLocation.fromNamespaceAndPath(HextechLib.MODID, "choose_card"));

    public static final StreamCodec<FriendlyByteBuf, PacketChooseCard> STREAM_CODEC =
            StreamCodec.of((buf, packet) -> buf.writeResourceLocation(packet.cardId()),
                    buf -> new PacketChooseCard(buf.readResourceLocation()));

    @Override public Type<? extends CustomPacketPayload> type() { return TYPE; }

    public void handle(IPayloadContext ctx) {
        ctx.enqueueWork(() -> {
            if (ctx.player() instanceof ServerPlayer player) {
                HCard card = HexCardRegistry.getById(cardId);
                player.connection.send(new ClientboundSoundPacket(
                        BuiltInRegistries.SOUND_EVENT.wrapAsHolder(SoundEvents.PLAYER_LEVELUP),
                        SoundSource.PLAYERS,
                        player.getX(),
                        player.getY(),
                        player.getZ(),
                        1.0F,
                        1.0F,
                        player.level().getRandom().nextLong()
                ));

                if (card != null) {
                    card.applyEffect().accept(player);
                    player.displayClientMessage(Component.translatable("network.hextech_lib.packet.msg") .append(card.title()), true);
                }
            }
        });
    }
}