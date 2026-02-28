package com.renzaifei.hextechlib.network;

import com.renzaifei.hextechlib.HextechLib;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;

import java.util.UUID;

public class PackChooseCard implements CustomPacketPayload {
    //静态存储数据
    public static final Type<PackChooseCard> TYPE =
            new Type<PackChooseCard>(ResourceLocation.fromNamespaceAndPath(HextechLib.MODID,"h_pcc"));
    public static final StreamCodec<FriendlyByteBuf,PackChooseCard> STREAM_CODEC =
            CustomPacketPayload.codec(PackChooseCard::write,PackChooseCard::new);


    //在网络中传输的数据
    private ResourceLocation cardIDMessage;
    private int flag;


    @Override
    public Type<PackChooseCard> type() {
        return TYPE;
    }

    public int getFlag() { return flag; }
    public ResourceLocation getCardID() { return cardIDMessage; }

    private PackChooseCard(FriendlyByteBuf buf) {
        this.flag = buf.readInt();
        this.cardIDMessage = buf.readResourceLocation();
    }

    public PackChooseCard(int flag, ResourceLocation cardsIDMessage) {
        this.flag = flag;
        this.cardIDMessage = cardsIDMessage;
    }


    public void write (FriendlyByteBuf buf) {
        buf.writeInt(this.flag);
        buf.writeResourceLocation(this.cardIDMessage);
    }
}
