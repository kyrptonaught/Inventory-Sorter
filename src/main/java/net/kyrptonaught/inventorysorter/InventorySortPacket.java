package net.kyrptonaught.inventorysorter;

import io.netty.buffer.Unpooled;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.network.ServerSidePacketRegistry;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.ingame.InventoryScreen;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.Inventory;
import net.minecraft.server.network.packet.CustomPayloadC2SPacket;
import net.minecraft.util.Identifier;
import net.minecraft.util.PacketByteBuf;

public class InventorySortPacket {
    private static final Identifier SORT_INV = new Identifier("inventorysorter", "sort_inv");

    static void registerReceivePacket() {
        ServerSidePacketRegistry.INSTANCE.register(SORT_INV, (packetContext, packetByteBuf) -> {
            boolean isPlayer = packetByteBuf.readBoolean();
            InventoryHelper.sortType = SortableStack.SortType.values()[packetByteBuf.readInt()];
            packetContext.getTaskQueue().execute(() -> {
                PlayerEntity player = packetContext.getPlayer();
                if (isPlayer) {
                    InventoryHelper.sortInv(player.inventory, 9, 27);
                } else {
                    Inventory inv = player.container.getSlot(0).inventory;
                    InventoryHelper.sortInv(inv, 0, inv.getInvSize());
                }
            });
        });
    }

    @Environment(EnvType.CLIENT)
    public static void sendSortPacket(Screen currentScreen) {
        if (currentScreen instanceof InventoryScreen)
            sendSortPacket(true);
        else {
            if (InventorySorterMod.configManager.config.sortPlayer) sendSortPacket(true);
            sendSortPacket(false);
        }
    }

    @Environment(EnvType.CLIENT)
    public static void sendSortPacket(boolean isPlayer) {
        PacketByteBuf buf = new PacketByteBuf(Unpooled.buffer());
        buf.writeBoolean(isPlayer);
        buf.writeInt(InventorySorterMod.getConfig().sortType.ordinal());
        MinecraftClient.getInstance().getNetworkHandler().getConnection().send(new CustomPayloadC2SPacket(SORT_INV, new PacketByteBuf(buf)));
    }
}
