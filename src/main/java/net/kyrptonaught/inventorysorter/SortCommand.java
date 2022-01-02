package net.kyrptonaught.inventorysorter;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import net.kyrptonaught.inventorysorter.interfaces.InvSorterPlayer;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.HopperBlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.Inventory;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.LiteralText;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.registry.Registry;

import java.util.function.BiConsumer;
import java.util.function.Consumer;


public class SortCommand {
    public static void register(CommandDispatcher<ServerCommandSource> dispatcher, boolean b) {
        dispatcher.register(CommandManager.literal("sort")
                .requires((source) -> source.hasPermissionLevel(0))
                .executes((commandContext) -> {
                    HitResult hit = commandContext.getSource().getPlayer().raycast(6, 1, false);
                    if (hit instanceof BlockHitResult) {
                       //Inventory inventory = HopperBlockEntity.getInventoryAt(commandContext.getSource().getPlayer().getWorld(), ((BlockHitResult) hit).getBlockPos());

                        Text feedBack = null;
                        feedBack = InventoryHelper.sortBlock(commandContext.getSource().getPlayer().getWorld(), ((BlockHitResult) hit).getBlockPos(), commandContext.getSource().getPlayer(), ((InvSorterPlayer) commandContext.getSource().getPlayer()).getSortType());
                        commandContext.getSource().sendFeedback(feedBack, false);
                    }
                    return 1;
                }));
        dispatcher.register(CommandManager.literal("sortme")
                .requires((source) -> source.hasPermissionLevel(0))
                .executes((commandContext) -> {
                    InventoryHelper.sortInv(commandContext.getSource().getPlayer(), true, ((InvSorterPlayer) commandContext.getSource().getPlayer()).getSortType());
                    Text feedBack = new LiteralText("Sorted inventory");
                    commandContext.getSource().sendFeedback(feedBack, false);
                    return 1;
                }));

        LiteralArgumentBuilder<ServerCommandSource> invsortCommand = CommandManager.literal("invsort").requires((source) -> source.hasPermissionLevel(0));

        for (SortCases.SortType sortType : SortCases.SortType.values()) {
            invsortCommand.then(CommandManager.literal("sortType")
                    .then(CommandManager.literal(sortType.name())
                            .executes(context -> {
                                ((InvSorterPlayer) context.getSource().getPlayer()).setSortType(sortType);
                                Text feedBack = new LiteralText("Updated Sorting Type");
                                context.getSource().sendFeedback(feedBack, false);
                                return 1;
                            })));
        }
        registerBooleanCommand(invsortCommand, "middleClickSort", new LiteralText("Set Middle click slot to sort to "), (player, value) -> ((InvSorterPlayer) player).setMiddleClick(value));
        registerBooleanCommand(invsortCommand, "doubleClickSort", new LiteralText("Set Double click slot to sort to "), (player, value) -> ((InvSorterPlayer) player).setDoubleClickSort(value));

        dispatcher.register(invsortCommand);
    }

    public static void registerBooleanCommand(LiteralArgumentBuilder<ServerCommandSource> invsortCommand, String command, Text response, BiConsumer<PlayerEntity, Boolean> storage) {
        invsortCommand.then(CommandManager.literal(command)
                .then(CommandManager.literal("false")
                        .executes(context -> {
                            storage.accept(context.getSource().getPlayer(), false);
                            Text feedBack = response.copy().append("False");
                            context.getSource().sendFeedback(feedBack, false);
                            return 1;
                        })));
        invsortCommand.then(CommandManager.literal(command)
                .then(CommandManager.literal("true")
                        .executes(context -> {
                            storage.accept(context.getSource().getPlayer(), true);
                            Text feedBack = response.copy().append("True");
                            context.getSource().sendFeedback(feedBack, false);
                            return 1;
                        })));
    }
}
