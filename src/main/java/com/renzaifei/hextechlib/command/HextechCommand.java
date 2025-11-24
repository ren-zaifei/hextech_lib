package com.renzaifei.hextechlib.command;


import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.context.CommandContext;
import com.renzaifei.hextechlib.HexTriggers;
import com.renzaifei.hextechlib.card.HCard;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;

public class HextechCommand {

    public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
        dispatcher.register(Commands.literal("hextech")
                .requires(source -> source.hasPermission(4))
                .then(Commands.literal("common")
                        .executes(context -> execute(context, HCard.Rarity.COMMON))
                )
                .then(Commands.literal("rare")
                        .executes(context -> execute(context, HCard.Rarity.RARE))
                )
                .then(Commands.literal("epic")
                        .executes(context -> execute(context, HCard.Rarity.EPIC))
                )
                .then(Commands.literal("legendary")
                        .executes(context -> execute(context, HCard.Rarity.LEGENDARY))
                )
        );
    }

    private static int execute(CommandContext<CommandSourceStack> context, HCard.Rarity rarity) {
        CommandSourceStack source = context.getSource();

        if (source.getPlayer() instanceof ServerPlayer player) {
            HexTriggers.selection(player, rarity);
            return 1;
        } else {
            source.sendFailure(Component.translatable("command.hextech_lib.error.noplayer"));
            return 0;
        }
    }
}