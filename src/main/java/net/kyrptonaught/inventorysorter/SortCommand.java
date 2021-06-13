package net.kyrptonaught.inventorysorter;

import com.mojang.brigadier.CommandDispatcher;
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
                .requires((source) -> source.hasPermissionLevel(2))
                .executes((commandContext) -> {
                    HitResult hit = commandContext.getSource().getPlayer().raycast(6, 1, false);
                    if (hit instanceof BlockHitResult) {
                        Inventory inventory = HopperBlockEntity.getInventoryAt(commandContext.getSource().getPlayer().getServerWorld(), ((BlockHitResult) hit).getBlockPos());
                        if (inventory == null) {
                            Text feedBack = new LiteralText("Not looking at an inventory");
                            commandContext.getSource().sendFeedback(feedBack, false);
                            return 1;
                        }
                        InventoryHelper.sortInv(inventory, 0, inventory.size(), SortCases.SortType.NAME);
                        Text feedBack = new LiteralText("Sorted inventory");
                        commandContext.getSource().sendFeedback(feedBack, false);
                    }
                    return 1;
                }));
        dispatcher.register(CommandManager.literal("sortme")
                .requires((source) -> source.hasPermissionLevel(2))
                .executes((commandContext) -> {
                    InventoryHelper.sortInv(commandContext.getSource().getPlayer().getInventory(), 9, 27, SortCases.SortType.NAME);
                    Text feedBack = new LiteralText("Sorted inventory");
                    commandContext.getSource().sendFeedback(feedBack, false);
                    return 1;
                }));
    }
}
