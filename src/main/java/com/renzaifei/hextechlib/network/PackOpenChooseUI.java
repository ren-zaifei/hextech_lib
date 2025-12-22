package com.renzaifei.hextechlib.network;

import com.renzaifei.hextechlib.HextechLib;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class PackOpenChooseUI implements CustomPacketPayload {
    //静态存储数据
    static List<ResourceLocation> cardsID;
    static UUID uuid;
    public static final Type<PackOpenChooseUI> TYPE =
            new Type<PackOpenChooseUI>(ResourceLocation.fromNamespaceAndPath(HextechLib.MODID,"h_ocu"));
    public static final StreamCodec<FriendlyByteBuf,PackOpenChooseUI> STREAM_CODEC =
            CustomPacketPayload.codec(PackOpenChooseUI::write,PackOpenChooseUI::new);


    //在网络中传输的数据
    public List<ResourceLocation> cardsIDMessage;
    public UUID uuidMessage;
    public int flag;

    public static void receive(PackOpenChooseUI data){
        int flag = data.flag;
        if (flag == 1){
            cardsID = data.cardsIDMessage;
        }
        if (flag == 2){
            uuid = data.uuidMessage;
        }
    }


    @Override
    public Type<PackOpenChooseUI> type() {
        return TYPE;
    }

    //获取存储的ID组
    public static List<ResourceLocation> getCardsIDs() {
        return cardsID;
    }

    //获取uuid
    public static UUID getUUID() {
        if (uuid == null) return null;
        return uuid;
    }

    private PackOpenChooseUI(FriendlyByteBuf buf) {
        this.flag = buf.readInt();
        if (flag == 1){
            int length = buf.readVarInt();
            List<ResourceLocation> cards = new ArrayList<>(length);
            for (int i = 0; i < length; i++) {
                cards.add(buf.readResourceLocation());
            }
            this.cardsIDMessage = cards;
        }
    }

    public PackOpenChooseUI(int flag, List<ResourceLocation> cardsIDMessage, UUID uuidMessage) {
        this.flag = flag;
        this.cardsIDMessage = cardsIDMessage;
        this.uuidMessage = uuidMessage;
    }


    public void write (FriendlyByteBuf buf) {
        buf.writeInt(flag);
        if (flag == 1){
            buf.writeVarInt(cardsIDMessage.size());
            for (ResourceLocation id : cardsIDMessage) {
                buf.writeResourceLocation(id);
            }
        }
        if (flag == 2){
            buf.writeUUID(this.uuidMessage);
        }
    }
}
