package com.renzaifei.hextechlib.network;

import com.renzaifei.hextechlib.HextechLib;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;

import java.util.UUID;

public class PackChooseCard implements CustomPacketPayload {
    //静态存储数据
    static ResourceLocation cardID;
    static UUID uuid;
    public static final Type<PackChooseCard> TYPE =
            new Type<PackChooseCard>(ResourceLocation.fromNamespaceAndPath(HextechLib.MODID,"h_pcc"));
    public static final StreamCodec<FriendlyByteBuf,PackChooseCard> STREAM_CODEC =
            CustomPacketPayload.codec(PackChooseCard::write,PackChooseCard::new);


    //在网络中传输的数据
    public ResourceLocation cardIDMessage;
    public UUID uuidMessage;
    public int flag;

    public static void receive(PackChooseCard data){
        int flag = data.flag;
        if (flag == 1){
            cardID = data.cardIDMessage;
        }
        if (flag == 2){
            uuid = data.uuidMessage;
        }
    }


    @Override
    public Type<PackChooseCard> type() {
        return TYPE;
    }

    //获取存储的ID组
    public static ResourceLocation getCardID() {
        return cardID;
    }

    //获取uuid
    public static UUID getUUID() {
        if (uuid == null) return null;
        return uuid;
    }

    private PackChooseCard(FriendlyByteBuf buf) {
        this.flag = buf.readInt();
        if (flag == 1){
            this.cardIDMessage = buf.readResourceLocation();
        }
        if (flag == 2){
            this.uuidMessage = buf.readUUID();
        }
    }

    public PackChooseCard(int flag, ResourceLocation cardsIDMessage, UUID uuidMessage) {
        this.flag = flag;
        this.cardIDMessage = cardsIDMessage;
        this.uuidMessage = uuidMessage;
    }


    public void write (FriendlyByteBuf buf) {
        buf.writeInt(flag);
        if (flag == 1){
           buf.writeResourceLocation(cardIDMessage);
        }
        if (flag == 2){
            buf.writeUUID(this.uuidMessage);
        }
    }
}
