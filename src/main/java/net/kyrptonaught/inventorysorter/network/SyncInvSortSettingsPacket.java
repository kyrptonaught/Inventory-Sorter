package net.kyrptonaught.inventorysorter.network;

import io.netty.buffer.Unpooled;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.kyrptonaught.inventorysorter.interfaces.InvSorterPlayer;
import net.kyrptonaught.inventorysorter.SortCases;
import net.kyrptonaught.inventorysorter.client.InventorySorterModClient;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.util.Identifier;

public class SyncInvSortSettingsPacket {
    private static final Identifier SYNC_SETTINGS = new Identifier("inventorysorter", "sync_settings_packet");

    @Environment(EnvType.CLIENT)
    public static void registerSyncOnPlayerJoin() {
        PacketByteBuf buf = new PacketByteBuf(Unpooled.buffer());
        buf.writeBoolean(InventorySorterModClient.getConfig().middleClick);
        buf.writeBoolean(InventorySorterModClient.getConfig().doubleClickSort);
        buf.writeInt(InventorySorterModClient.getConfig().sortType.ordinal());
        ClientPlayNetworking.send(SYNC_SETTINGS, buf);
    }

    public static void registerReceiveSyncData() {
        ServerPlayNetworking.registerGlobalReceiver(SYNC_SETTINGS, ((server, player, handler, buf, responseSender) -> {
            boolean middleClick = buf.readBoolean();
            boolean doubleClick = buf.readBoolean();
            int sortType = buf.readInt();
            server.execute(() -> {
                ((InvSorterPlayer) player).setMiddleClick(middleClick);
                ((InvSorterPlayer) player).setMiddleClick(doubleClick);
                ((InvSorterPlayer) player).setSortType(SortCases.SortType.values()[sortType]);
            });
        }));
    }
}
