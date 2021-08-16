package net.kyrptonaught.inventorysorter.interfaces;

import net.minecraft.inventory.Inventory;

public interface SortableContainer {
    Inventory getInventory();

    boolean hasSlots();

}
