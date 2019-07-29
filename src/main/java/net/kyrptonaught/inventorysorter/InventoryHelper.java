package net.kyrptonaught.inventorysorter;

import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class InventoryHelper {
    static void sortInv(Inventory inv, int startSlot, int invSize) {
        List<SortableStack> stacks = new ArrayList<>();
        for (int i = 0; i < invSize; i++) {
            ItemStack stack = inv.getInvStack(startSlot + i);
            if (stack.getItem() != Items.AIR)
                stacks.add(new SortableStack(stack));
        }
        mergeStacks(stacks);
        Collections.sort(stacks);
        for (int i = 0; i < invSize; i++) {
            inv.setInvStack(startSlot + i, ItemStack.EMPTY);
        }
        for (int i = 0; i < stacks.size(); i++)
            inv.setInvStack(startSlot + i, stacks.get(i).getStack());
        inv.markDirty();
    }

    private static void mergeStacks(List<SortableStack> stacks) {
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