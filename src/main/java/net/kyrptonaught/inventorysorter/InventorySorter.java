package net.kyrptonaught.inventorysorter;

import io.netty.buffer.Unpooled;
import net.fabricmc.fabric.api.network.ServerSidePacketRegistry;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.Entity;
import net.minecraft.entity.vehicle.StorageMinecartEntity;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.server.network.packet.CustomPayloadC2SPacket;
import net.minecraft.util.Identifier;
import net.minecraft.util.PacketByteBuf;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.BlockPos;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class InventorySorter {
    private static final Identifier SORT_INV = new Identifier("inventorysorter", "sort_inv");

    static void registerReceivePacket() {
        ServerSidePacketRegistry.INSTANCE.register(SORT_INV, (packetContext, packetByteBuf) -> {
            int sortType = packetByteBuf.readInt();
            BlockPos invPos = packetByteBuf.readBlockPos();
            packetContext.getTaskQueue().execute(() -> {
                Inventory inv;
                switch (sortType) {
                    case 0:
                        inv = packetContext.getPlayer().inventory;
                        sortInv(inv, 9, 27);
                        break;
                    case 1:
                        inv = packetContext.getPlayer().getEnderChestInventory();
                        sortInv(inv, 0, inv.getInvSize());
                        break;
                    case 2:
                        inv = InventoryHelper.getInventoryAt(packetContext.getPlayer().world, invPos.getX(), invPos.getY(), invPos.getZ());
                        sortInv(inv, 0, inv.getInvSize());
                        break;
                    case 3:
                        inv = ((StorageMinecartEntity) packetContext.getPlayer().world.getEntityById(invPos.getX()));
                        sortInv(inv, 0, inv.getInvSize());
                        break;
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
        MinecraftClient mc = MinecraftClient.getInstance();
        if (isPlayer) mc.getNetworkHandler().getClientConnection().send(createSortPacket(0, new BlockPos(0, 0, 0)));
        else {
            HitResult rt = mc.hitResult;
            if (rt.getType() == HitResult.Type.BLOCK) {
                Block block = mc.world.getBlockState(((BlockHitResult) rt).getBlockPos()).getBlock();
                if (block == Blocks.ENDER_CHEST)
                    mc.getNetworkHandler().getClientConnection().send(createSortPacket(1, ((BlockHitResult) rt).getBlockPos()));
                else if (block.hasBlockEntity())
                    mc.getNetworkHandler().getClientConnection().send(createSortPacket(2, ((BlockHitResult) rt).getBlockPos()));
            } else if (rt.getType() == HitResult.Type.ENTITY) {
                Entity entity = ((EntityHitResult) rt).getEntity();
                if (entity instanceof Inventory) {
                    mc.getNetworkHandler().getClientConnection().send(createSortPacket(3, (new BlockPos(entity.getEntityId(), 0, 0))));// cheaty but works
                }
            }
        }
    }

    private static CustomPayloadC2SPacket createSortPacket(int sortInvType, BlockPos sortInv) { //sortType: 0 = player, 1 = enderchest, 2 = blockentity, 3 = entity
        PacketByteBuf buf = new PacketByteBuf(Unpooled.buffer());
        buf.writeInt(sortInvType);
        buf.writeBlockPos(sortInv);
        return new CustomPayloadC2SPacket(SORT_INV, new PacketByteBuf(buf));
    }
}
