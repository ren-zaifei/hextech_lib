package com.renzaifei.hextechlib.network;

import com.renzaifei.hextechlib.HextechLib;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class PackOpenChooseUI implements CustomPacketPayload {
    public static final Type<PackOpenChooseUI> TYPE =
            new Type<>(ResourceLocation.fromNamespaceAndPath(HextechLib.MODID, "h_ocu"));

    public static final StreamCodec<FriendlyByteBuf, PackOpenChooseUI> STREAM_CODEC =
            new StreamCodec<>() {
                @Override
                public PackOpenChooseUI decode(FriendlyByteBuf buf) {
                    int flag = buf.readInt();
                    int length = buf.readVarInt();
                    List<ResourceLocation> cards = new ArrayList<>(length);
                    for (int i = 0; i < length; i++) {
                        cards.add(buf.readResourceLocation());
                    }
                    return new PackOpenChooseUI(flag, cards);
                }

                @Override
                public void encode(FriendlyByteBuf buf, PackOpenChooseUI packet) {
                    buf.writeInt(packet.flag);
                    buf.writeVarInt(packet.cards.size());
                    for (ResourceLocation id : packet.cards) {
                        buf.writeResourceLocation(id);
                    }
                }
            };

    private final int flag;
    private final List<ResourceLocation> cards;

    public PackOpenChooseUI(int flag, List<ResourceLocation> cards) {
        this.flag = flag;
        this.cards = cards;
    }

    public int flag() { return flag; }
    public List<ResourceLocation> cards() { return cards; }

    @Override
    @NotNull
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
