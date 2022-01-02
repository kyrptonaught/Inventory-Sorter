package net.kyrptonaught.inventorysorter.network;

import io.netty.buffer.Unpooled;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.kyrptonaught.inventorysorter.InventorySorterMod;
import net.kyrptonaught.inventorysorter.client.config.IgnoreList;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;

public class SyncBlacklistPacket {
    private static final Identifier SYNC_BLACKLIST = new Identifier("inventorysorter", "sync_blacklist_packet");

    public static void registerSyncOnPlayerJoin() {
        ServerPlayConnectionEvents.JOIN.register((serverPlayNetworkHandler, packetSender, minecraftServer) -> packetSender.sendPacket(SYNC_BLACKLIST, getBuf()));
    }

    public static void sync(ServerPlayerEntity player) {
        ServerPlayNetworking.send(player, SYNC_BLACKLIST, getBuf());
    }

    private static PacketByteBuf getBuf() {
        IgnoreList blacklist = InventorySorterMod.getBlackList();
        PacketByteBuf buf = new PacketByteBuf(Unpooled.buffer());

        String[] hideList = new String[blacklist.hideSortBtnsList.size()];
        hideList = blacklist.hideSortBtnsList.toArray(hideList);
        buf.writeInt(hideList.length);
        for (int i = 0; i < hideList.length; i++)
            buf.writeString(hideList[i]);

        String[] unSortList = new String[blacklist.doNotSortList.size()];
        unSortList = blacklist.doNotSortList.toArray(unSortList);
        buf.writeInt(unSortList.length);
        for (int i = 0; i < unSortList.length; i++)
            buf.writeString(unSortList[i]);
        return buf;
    }

    @Environment(EnvType.CLIENT)
    public static void registerRecieveBlackList() {
        ClientPlayNetworking.registerGlobalReceiver(SYNC_BLACKLIST, (client, handler, packet, sender) -> {
            int numHides = packet.readInt();
            for (int i = 0; i < numHides; i++)
                InventorySorterMod.getBlackList().hideSortBtnsList.add(packet.readString());

            int numNoSort = packet.readInt();
            for (int i = 0; i < numNoSort; i++)
                InventorySorterMod.getBlackList().doNotSortList.add(packet.readString());
        });
    }
}
