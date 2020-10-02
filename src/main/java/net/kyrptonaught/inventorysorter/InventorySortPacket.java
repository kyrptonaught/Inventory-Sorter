package net.kyrptonaught.inventorysorter;

import io.netty.buffer.Unpooled;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.network.ServerSidePacketRegistry;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.Inventory;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.network.packet.c2s.play.CustomPayloadC2SPacket;
import net.minecraft.util.Identifier;

public class InventorySortPacket {
    private static final Identifier SORT_INV_PACKET = new Identifier("inventorysorter", "sort_inv_packet");

    static void registerReceivePacket() {
        ServerSidePacketRegistry.INSTANCE.register(SORT_INV_PACKET, (packetContext, packetByteBuf) -> {
            boolean playerInv = packetByteBuf.readBoolean();
            SortCases.SortType sortType = SortCases.SortType.values()[packetByteBuf.readInt()];
            packetContext.getTaskQueue().execute(() -> {
                PlayerEntity player = packetContext.getPlayer();
                if (playerInv) {
                    InventoryHelper.sortInv(player.inventory, 9, 27, sortType);
                } else {
                    Inventory inv = ((SortableContainer) player.currentScreenHandler).getInventory();
                    InventoryHelper.sortInv(inv, 0, inv.size(), sortType);
                }
            });
        });
    }

    @Environment(EnvType.CLIENT)
    public static void sendSortPacket(boolean playerInv) {
        PacketByteBuf buf = new PacketByteBuf(Unpooled.buffer());
        buf.writeBoolean(playerInv);
        buf.writeInt(InventorySorterMod.getConfig().sortType.ordinal());
        MinecraftClient.getInstance().getNetworkHandler().getConnection().send(new CustomPayloadC2SPacket(SORT_INV_PACKET, new PacketByteBuf(buf)));
        if (!playerInv && InventorySorterMod.getConfig().sortPlayer)
            sendSortPacket(true);
    }
}
