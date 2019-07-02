package net.kyrptonaught.inventorysorter;

import net.minecraft.item.ItemStack;

public class SortableStack implements Comparable {
    private ItemStack sortStack;

    SortableStack(ItemStack stack) {
        sortStack = stack;
    }

    ItemStack getStack() {
        return sortStack;
    }

    private static String getCleanName(ItemStack stack) {
        return stack.getItem().getName().getText();
    }

    @Override
    public String toString() {
        return getCleanName(sortStack) + " x" + sortStack.getCount();
    }

    @Override
    public int compareTo(Object o) {
        ItemStack otherStack = ((SortableStack) o).getStack();
        int compared = getCleanName(sortStack).compareTo(getCleanName(otherStack));
        if (compared == 0)
            compared = sortStack.getCount() >= otherStack.getCount() ? 1 : -1;
        return compared;
    }
}
