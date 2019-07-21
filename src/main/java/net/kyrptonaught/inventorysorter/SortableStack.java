package net.kyrptonaught.inventorysorter;

import net.minecraft.item.EnchantedBookItem;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.ListTag;

public class SortableStack implements Comparable {
    private ItemStack sortStack;

    SortableStack(ItemStack stack) {
        sortStack = stack;
    }

    ItemStack getStack() {
        return sortStack;
    }

    private static String getCleanName(ItemStack stack) {
        if (stack.getItem() instanceof EnchantedBookItem)
            return SpecialSortCases.EnchantedBookNameCase(stack);
        return stack.getItem().toString();
    }

    @Override
    public String toString() {
        return getCleanName(sortStack) + " x" + sortStack.getCount();
    }

    @Override
    public int compareTo(Object o) {
        ItemStack otherStack = ((SortableStack) o).getStack();
        int compared = getCleanName(sortStack).compareTo(getCleanName(otherStack));
        if (compared == 0) {
            compared = sortStack.getCount() >= otherStack.getCount() ? -1 : 1;
        }
        return compared;
    }
}
