package net.kyrptonaught.inventorysorter.network;

import io.netty.buffer.Unpooled;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.kyrptonaught.inventorysorter.InventoryHelper;
import net.kyrptonaught.inventorysorter.SortCases;
import net.kyrptonaught.inventorysorter.client.InventorySorterModClient;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.util.Identifier;

public class InventorySortPacket {
    private static final Identifier SORT_INV_PACKET = new Identifier("inventorysorter", "sort_inv_packet");

    public static void registerReceivePacket() {
        ServerPlayNetworking.registerGlobalReceiver(SORT_INV_PACKET, ((server, player, handler, buf, responseSender) -> {
            boolean playerInv = buf.readBoolean();
            SortCases.SortType sortType = SortCases.SortType.values()[buf.readInt()];
            server.execute(() -> InventoryHelper.sortInv(player, playerInv, sortType));
        }));
    }

    @Environment(EnvType.CLIENT)
    public static void sendSortPacket(boolean playerInv) {
        PacketByteBuf buf = new PacketByteBuf(Unpooled.buffer());
        buf.writeBoolean(playerInv);
        buf.writeInt(InventorySorterModClient.getConfig().sortType.ordinal());
        ClientPlayNetworking.send(SORT_INV_PACKET, new PacketByteBuf(buf));
        if (!playerInv && InventorySorterModClient.getConfig().sortPlayer)
            sendSortPacket(true);
    }
}
