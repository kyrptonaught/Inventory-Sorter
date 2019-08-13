package net.kyrptonaught.inventorysorter;

import net.minecraft.item.EnchantedBookItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.registry.Registry;

public class SortableStack implements Comparable {
    public enum SortType {
        NAME, CATEGORY, MOD
    }

    private ItemStack sortStack;

    SortableStack(ItemStack stack) {
        sortStack = stack;
    }

    private static String getSortString(ItemStack stack) {
        Item item = stack.getItem();
        String itemName = item.toString();
        if (stack.getItem() instanceof EnchantedBookItem)
            itemName = SpecialSortCases.EnchantedBookNameCase(stack);
        switch (InventoryHelper.sortType) {
            case CATEGORY:
                return item.getGroup().getName() + itemName;
            case MOD:
                return Registry.ITEM.getId(item).getNamespace() + itemName;
        }
        return itemName;
    }

    ItemStack getStack() {
        return sortStack;
    }

    @Override
    public int compareTo(Object o) {
        ItemStack otherStack = ((SortableStack) o).getStack();
        int compared = getSortString(sortStack).compareTo(getSortString(otherStack));
        if (compared == 0) {
            compared = sortStack.getCount() >= otherStack.getCount() ? -1 : 1;
        }
        return compared;
    }
}
