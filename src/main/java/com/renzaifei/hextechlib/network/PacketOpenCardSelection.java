package com.renzaifei.hextechlib.network;

import com.renzaifei.hextechlib.HextechLib;
import com.renzaifei.hextechlib.card.HCard;
import com.renzaifei.hextechlib.card.HexCardRegistry;
import com.renzaifei.hextechlib.ui.CardSelectionScreen;
import net.minecraft.client.Minecraft;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.network.handling.IPayloadContext;

import java.util.ArrayList;
import java.util.List;

public record PacketOpenCardSelection(List<HCard> cards) implements CustomPacketPayload {

    public static final CustomPacketPayload.Type<PacketOpenCardSelection> TYPE =
            new CustomPacketPayload.Type<>(ResourceLocation.fromNamespaceAndPath(HextechLib.MODID, "open_selection"));

    public static final StreamCodec<FriendlyByteBuf, PacketOpenCardSelection> STREAM_CODEC = StreamCodec.of(
            (buf, packet) -> {
                buf.writeVarInt(packet.cards.size());
                for (HCard c : packet.cards) buf.writeResourceLocation(c.id());
            },
            buf -> {
                int size = buf.readVarInt();
                List<HCard> list = new ArrayList<>();
                for (int i = 0; i < size; i++) {
                    HCard card = HexCardRegistry.getById(buf.readResourceLocation());
                    if (card != null) list.add(card);
                }
                return new PacketOpenCardSelection(list);
            }
    );

    @Override
    public Type<? extends CustomPacketPayload> type() { return TYPE; }

    public void handle(IPayloadContext ctx) {
        ctx.enqueueWork(() -> Minecraft.getInstance().setScreen(new CardSelectionScreen(cards)));
    }
}
