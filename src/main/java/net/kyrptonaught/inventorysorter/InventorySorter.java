package net.kyrptonaught.inventorysorter;

import io.netty.buffer.Unpooled;
import net.fabricmc.fabric.api.network.ServerSidePacketRegistry;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.server.network.packet.CustomPayloadC2SPacket;
import net.minecraft.util.Identifier;
import net.minecraft.util.PacketByteBuf;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class InventorySorter {
    private static final Identifier SORT_INV = new Identifier("inventorysorter", "sort_inv");

    static void registerReceivePacket() {
        ServerSidePacketRegistry.INSTANCE.register(SORT_INV, (packetContext, packetByteBuf) -> {
            boolean isPlayer = packetByteBuf.readBoolean();
            packetContext.getTaskQueue().execute(() -> {
                PlayerEntity player = packetContext.getPlayer();
                if (isPlayer) sortInv(player.inventory, 9, 27);
                else {
                    Inventory inv = player.container.getSlot(0).inventory;
                    sortInv(inv, 0, inv.getInvSize());
                }
            });
        });
    }

    private static void sortInv(Inventory inv, int startSlot, int invSize) {
        List<SortableStack> stacks = new ArrayList<>();
        for (int i = 0; i < invSize; i++) {
            ItemStack stack = inv.getInvStack(startSlot + i);
            if (stack.getItem() != Items.AIR)
                stacks.add(new SortableStack(stack));
        }
        InventoryHelper.mergeStacks(stacks);
        Collections.sort(stacks);
        for (int i = 0; i < invSize; i++) {
            inv.setInvStack(startSlot + i, ItemStack.EMPTY);
        }
        for (int i = 0; i < stacks.size(); i++)
            inv.setInvStack(startSlot + i, stacks.get(i).getStack());
        inv.markDirty();
    }
    public static void sendPacket(Boolean isPlayer) {
        MinecraftClient.getInstance().getNetworkHandler().getClientConnection().send(createSortPacket(isPlayer));
    }

    private static CustomPayloadC2SPacket createSortPacket(boolean isPlayer) {
        PacketByteBuf buf = new PacketByteBuf(Unpooled.buffer());
        buf.writeBoolean(isPlayer);
        return new CustomPayloadC2SPacket(SORT_INV, new PacketByteBuf(buf));
    }
}
