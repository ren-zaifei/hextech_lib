package com.renzaifei.hextechlib.test;

import com.renzaifei.hextechlib.HextechLib;
import com.renzaifei.hextechlib.card.HCard;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

public class brisksteps extends HCard {
    private int brisksteps_layers = 0;
    private ResourceLocation modifierId = ResourceLocation.fromNamespaceAndPath(HextechLib.MODID,"briskstepsevent");

    protected brisksteps(ResourceLocation id, String titleKey, String descKey, Rarity rarity, ItemStack icon) {
        super(id, titleKey, descKey, rarity, icon);
    }

    @Override
    public void applyEffect(Player player) {
        player.getPersistentData().putBoolean("brisksteps", true);
        CompoundTag data = player.getPersistentData();
        brisksteps_layers = data.getInt("brisksteps_layers") + 1;
        data.putInt("brisksteps_layers", brisksteps_layers);
        ServerPlayer serverPlayer = (ServerPlayer) player;
        serverPlayer.getAttribute(Attributes.MOVEMENT_SPEED).removeModifier(modifierId);
        AttributeModifier modifier = new AttributeModifier(
                modifierId,
                0.1 * brisksteps_layers,
                AttributeModifier.Operation.ADD_MULTIPLIED_BASE
        );
        serverPlayer.getAttribute(Attributes.MOVEMENT_SPEED).addOrReplacePermanentModifier(modifier);
    }

    @Override
    public void reload(Player oldPlayer, Player newPlayer) {
        brisksteps_layers = oldPlayer.getPersistentData().getInt("brisksteps_layers");
        if (brisksteps_layers > 0){
            newPlayer.getPersistentData().putInt("brisksteps_layers", brisksteps_layers);
        }
        if (oldPlayer.getPersistentData().getBoolean("brisksteps")){
            newPlayer.getPersistentData().putBoolean("brisksteps", true);
            newPlayer.getAttribute(Attributes.MOVEMENT_SPEED).removeModifier(modifierId);
            AttributeModifier modifier = new AttributeModifier(
                    modifierId,
                    0.1 * brisksteps_layers,
                    AttributeModifier.Operation.ADD_MULTIPLIED_BASE
            );
            newPlayer.getAttribute(Attributes.MOVEMENT_SPEED).addOrReplacePermanentModifier(modifier);
        }

    }

    @Override
    public void disconnect(Player player) {
        player.getPersistentData().remove("brisksteps");
        player.getPersistentData().remove("brisksteps_layers");
        if (this.brisksteps_layers > 0){
            this.brisksteps_layers--;
        }
        player.getAttribute(Attributes.MOVEMENT_SPEED).removeModifier(modifierId);
    }
}
