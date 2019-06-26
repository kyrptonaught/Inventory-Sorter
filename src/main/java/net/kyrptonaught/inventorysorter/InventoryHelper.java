package net.kyrptonaught.inventorysorter;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.ChestBlock;
import net.minecraft.block.InventoryProvider;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.ChestBlockEntity;
import net.minecraft.entity.Entity;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.predicate.entity.EntityPredicates;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BoundingBox;
import net.minecraft.world.World;

import java.util.List;

public class InventoryHelper {

    static void mergeStacks(List<SortableStack> stacks) {
        for (int i = 0; i < stacks.size(); i++) {
            ItemStack stack = stacks.get(i).getStack();
            if (!stack.isStackable() || isStackFull(stack)) continue;
            for (int j = stacks.size() - 1; j > i && !isStackFull(stack); j--) {
                ItemStack stack2 = stacks.get(j).getStack();
                if (canMergeItems(stack, stack2))
                    combineStacks(stack, stack2);
                if (stack2.getItem() == Items.AIR || stack2.getCount() == 0) stacks.remove(j);
            }
        }
    }

    public static Inventory getInventoryAt(World world_1, double double_1, double double_2, double double_3) {
        Inventory inventory_1 = null;
        BlockPos blockPos_1 = new BlockPos(double_1, double_2, double_3);
        BlockState blockState_1 = world_1.getBlockState(blockPos_1);
        Block block_1 = blockState_1.getBlock();
        if (block_1 instanceof InventoryProvider) {
            inventory_1 = ((InventoryProvider) block_1).getInventory(blockState_1, world_1, blockPos_1);
        } else if (block_1.hasBlockEntity()) {
            BlockEntity blockEntity_1 = world_1.getBlockEntity(blockPos_1);
            if (blockEntity_1 instanceof Inventory) {
                inventory_1 = (Inventory) blockEntity_1;
                if (inventory_1 instanceof ChestBlockEntity && block_1 instanceof ChestBlock) {
                    inventory_1 = ChestBlock.getInventory(blockState_1, world_1, blockPos_1, true);
                }
            }
        }

        if (inventory_1 == null) {
            List<Entity> list_1 = world_1.getEntities((Entity) null, new BoundingBox(double_1 - 0.5D, double_2 - 0.5D, double_3 - 0.5D, double_1 + 0.5D, double_2 + 0.5D, double_3 + 0.5D), EntityPredicates.VALID_INVENTORIES);
            if (!list_1.isEmpty()) {
                inventory_1 = (Inventory) list_1.get(world_1.random.nextInt(list_1.size()));
            }
        }

        return inventory_1;
    }

    private static boolean isStackFull(ItemStack stack) {
        return stack.getCount() == stack.getMaxCount();
    }

    private static void combineStacks(ItemStack stack, ItemStack stack2) {
        if (stack.getMaxCount() >= stack.getCount() + stack2.getCount()) {
            stack.increment(stack2.getCount());
            stack2.setCount(0);
        }
        int maxInsertAmount = Math.min(stack.getMaxCount() - stack.getCount(), stack2.getCount());
        stack.increment(maxInsertAmount);
        stack2.increment(-maxInsertAmount);
    }

    public static ItemStack insertStack(Inventory inventory, ItemStack insertStack) {
        for (int j = 0; j < inventory.getInvSize(); j++) {
            ItemStack invStack = inventory.getInvStack(j);
            if (!invStack.isStackable()) continue;
            if (canMergeItems(invStack, insertStack)) {
                combineStacks(invStack, insertStack);
                if (insertStack.getCount() == 0) return insertStack;
            }
        }
        for (int j = 0; j < inventory.getInvSize(); j++) {
            ItemStack invStack = inventory.getInvStack(j);
            if (invStack.isEmpty()) {
                inventory.setInvStack(j, insertStack.copy());
                insertStack.setCount(0);
                return insertStack;
            }
        }
        return insertStack;
    }

    private static boolean canMergeItems(ItemStack itemStack_1, ItemStack itemStack_2) {
        if (itemStack_1.getItem() != itemStack_2.getItem()) {
            return false;
        } else if (itemStack_1.getDamage() != itemStack_2.getDamage()) {
            return false;
        } else {
            return ItemStack.areTagsEqual(itemStack_1, itemStack_2);
        }
    }
}