package com.renzaifei.hextechlib.ui;

import com.renzaifei.hextechlib.card.HCard;
import com.renzaifei.hextechlib.network.PackChooseCard;
import com.renzaifei.hextechlib.ui.components.BaseScreen;
import net.minecraft.client.resources.sounds.SimpleSoundInstance;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvents;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.neoforge.network.PacketDistributor;

import java.util.List;

@OnlyIn(Dist.CLIENT)
public class CardControlScreen extends BaseScreen {

    public CardControlScreen(List<HCard> cards) {
        super(Component.translatable("gui.hextech_lib.control.title"),cards);
    }

    public void onCardClicked(int index, HCard card){
        this.selectedIndex = index;
        PacketDistributor.sendToServer(new PackChooseCard(2, card.getId()));
        this.minecraft.getSoundManager().play(
                SimpleSoundInstance.forUI(SoundEvents.PLAYER_LEVELUP, 1.0F, 1.2F)
        );
        this.onClose();
    }
}