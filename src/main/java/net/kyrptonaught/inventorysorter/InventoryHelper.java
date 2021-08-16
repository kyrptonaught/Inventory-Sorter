package net.kyrptonaught.inventorysorter;

import net.kyrptonaught.inventorysorter.interfaces.SortableContainer;
import net.kyrptonaught.inventorysorter.mixin.ScreenHandlerTypeAccessor;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.screen.PlayerScreenHandler;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.ScreenHandlerType;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class InventoryHelper {
    public static boolean sortInv(PlayerEntity player, boolean sortPlayerInv, SortCases.SortType sortType) {
        if (sortPlayerInv) {
            sortInv(player.getInventory(), 9, 27, sortType);
            return true;
        } else if (canSortInventory(player)) {
            Inventory inv = ((SortableContainer) player.currentScreenHandler).getInventory();
            if (inv != null) {
                sortInv(inv, 0, inv.size(), sortType);
                return true;
            }
        }
        return false;
    }

    static void sortInv(Inventory inv, int startSlot, int invSize, SortCases.SortType sortType) {
        List<ItemStack> stacks = new ArrayList<>();
        for (int i = 0; i < invSize; i++)
            addStackWithMerge(stacks, inv.getStack(startSlot + i));

        stacks.sort(Comparator.comparing(stack -> SortCases.getStringForSort(stack, sortType)));
        if (stacks.size() == 0) return;
        for (int i = 0; i < invSize; i++)
            inv.setStack(startSlot + i, i < stacks.size() ? stacks.get(i) : ItemStack.EMPTY);
        inv.markDirty();
    }

    private static void addStackWithMerge(List<ItemStack> stacks, ItemStack newStack) {
        if (newStack.getItem() == Items.AIR) return;
        if (newStack.isStackable() && newStack.getCount() != newStack.getMaxCount())
            for (int j = stacks.size() - 1; j >= 0; j--) {
                ItemStack oldStack = stacks.get(j);
                if (canMergeItems(newStack, oldStack)) {
                    combineStacks(newStack, oldStack);
                    if (oldStack.getItem() == Items.AIR || oldStack.getCount() == 0) stacks.remove(j);
                }
            }
        stacks.add(newStack);
    }

    private static void combineStacks(ItemStack stack, ItemStack stack2) {
        if (stack.getMaxCount() >= stack.getCount() + stack2.getCount()) {
            stack.increment(stack2.getCount());
            stack2.setCount(0);
        }
        int maxInsertAmount = Math.min(stack.getMaxCount() - stack.getCount(), stack2.getCount());
        stack.increment(maxInsertAmount);
        stack2.decrement(maxInsertAmount);
    }

    private static boolean canMergeItems(ItemStack itemStack_1, ItemStack itemStack_2) {
        if (!itemStack_1.isStackable() || !itemStack_2.isStackable())
            return false;
        if (itemStack_1.getCount() == itemStack_1.getMaxCount() || itemStack_2.getCount() == itemStack_2.getMaxCount())
            return false;
        if (itemStack_1.getItem() != itemStack_2.getItem())
            return false;
        if (itemStack_1.getDamage() != itemStack_2.getDamage())
            return false;
        return ItemStack.areNbtEqual(itemStack_1, itemStack_2);
    }

    public static boolean shouldDisplayBtns(PlayerEntity player) {
        if (player.currentScreenHandler == null || !player.currentScreenHandler.canUse(player) || player.currentScreenHandler instanceof PlayerScreenHandler)
            return true;
        ScreenHandlerType<?> type = ((ScreenHandlerTypeAccessor) player.currentScreenHandler).gettype();
        if (type == null) return true;
        Identifier id = Registry.SCREEN_HANDLER.getId(type);
        if (id == null) return true;
        return !InventorySorterMod.getBlackList().isDisplayBlacklisted(id);
    }

    public static boolean canSortInventory(PlayerEntity player) {
        if (player.currentScreenHandler == null || !player.currentScreenHandler.canUse(player) || player.currentScreenHandler instanceof PlayerScreenHandler)
            return false;
        ScreenHandlerType<?> type = ((ScreenHandlerTypeAccessor) player.currentScreenHandler).gettype();
        if (type == null) return false;
        Identifier id = Registry.SCREEN_HANDLER.getId(type);
        if (id == null) return false;
        return isSortableContainer(player.currentScreenHandler, id);
    }

    private static boolean isSortableContainer(ScreenHandler currentScreen, Identifier screenID) {
        if (InventorySorterMod.getBlackList().isSortBlackListed(screenID))
            return false;
        if (!((SortableContainer) currentScreen).hasSlots())
            return false;
        int numSlots = currentScreen.slots.size();
        if (numSlots <= 36) return false;
        return numSlots - 36 >= 9;
    }
}