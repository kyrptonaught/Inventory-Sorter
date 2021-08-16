package net.kyrptonaught.inventorysorter;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import net.kyrptonaught.inventorysorter.interfaces.InvSorterPlayer;
import net.minecraft.block.entity.HopperBlockEntity;
import net.minecraft.inventory.Inventory;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.LiteralText;
import net.minecraft.text.Text;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.HitResult;


public class SortCommand {
    public static void register(CommandDispatcher<ServerCommandSource> dispatcher, boolean b) {
        dispatcher.register(CommandManager.literal("sort")
                .requires((source) -> source.hasPermissionLevel(0))
                .executes((commandContext) -> {
                    HitResult hit = commandContext.getSource().getPlayer().raycast(6, 1, false);
                    if (hit instanceof BlockHitResult) {
                        Inventory inventory = HopperBlockEntity.getInventoryAt(commandContext.getSource().getPlayer().getServerWorld(), ((BlockHitResult) hit).getBlockPos());
                        //commandContext.getSource().getWorld().getBlockState(((BlockHitResult) hit).getBlockPos()).onUse()
                        if (inventory == null) {
                            Text feedBack = new LiteralText("Not looking at an inventory");
                            commandContext.getSource().sendFeedback(feedBack, false);
                            return 1;
                        }
                        InventoryHelper.sortInv(commandContext.getSource().getPlayer(), false, ((InvSorterPlayer)commandContext.getSource().getPlayer()).getSortType());
                        Text feedBack = new LiteralText("Sorted inventory");
                        commandContext.getSource().sendFeedback(feedBack, false);
                    }
                    return 1;
                }));
        dispatcher.register(CommandManager.literal("sortme")
                .requires((source) -> source.hasPermissionLevel(0))
                .executes((commandContext) -> {
                    InventoryHelper.sortInv(commandContext.getSource().getPlayer(), true, ((InvSorterPlayer)commandContext.getSource().getPlayer()).getSortType());
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
        invsortCommand.then(CommandManager.literal("middleClickSort")
                .then(CommandManager.literal("true")
                        .executes(context -> {
                            ((InvSorterPlayer) context.getSource().getPlayer()).setMiddleClick(true);
                            Text feedBack = new LiteralText("Set Middle click to sort to true");
                            context.getSource().sendFeedback(feedBack, false);
                            return 1;
                        })));
        invsortCommand.then(CommandManager.literal("middleClickSort")
                .then(CommandManager.literal("false")
                        .executes(context -> {
                            ((InvSorterPlayer) context.getSource().getPlayer()).setMiddleClick(false);
                            Text feedBack = new LiteralText("Set Middle click to sort to false");
                            context.getSource().sendFeedback(feedBack, false);
                            return 1;
                        })));
        dispatcher.register(invsortCommand);
    }
}
