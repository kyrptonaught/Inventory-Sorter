package net.kyrptonaught.inventorysorter;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.item.EnchantedBookItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ToolItem;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class SortCases {
    public enum SortType {
        NAME, CATEGORY, MOD
    }

    static String getStringForSort(ItemStack stack) {
        Item item = stack.getItem();
        String itemName = specialCases(stack);
        switch (InventoryHelper.sortType) {
            case CATEGORY:
                return item.getGroup().getName() + itemName;
            case MOD:
                return Registry.ITEM.getId(item).getNamespace() + itemName;
        }
        return itemName;
    }

    private static String specialCases(ItemStack stack) {
        if (stack.getCount() != stack.getMaxCount())
            return SortCases.stackSize(stack);
        if (stack.getItem() instanceof EnchantedBookItem)
            return SortCases.enchantedBookNameCase(stack);
        if (stack.getItem() instanceof ToolItem)
            return SortCases.toolDuribilityCase(stack);
        return stack.getItem().toString();
    }

    private static String stackSize(ItemStack stack) {
        return stack.getItem().toString() + stack.getCount();
    }

    private static String enchantedBookNameCase(ItemStack stack) {
        ListTag enchants = EnchantedBookItem.getEnchantmentTag(stack);
        List<String> names = new ArrayList<>();
        String enchantNames = "";
        for (int i = 0; i < enchants.size(); i++) {
            CompoundTag enchantTag = enchants.getCompoundTag(i);
            Identifier enchantID = Identifier.tryParse(enchantTag.getString("id"));
            if (enchantID == null) continue;
            Enchantment enchant = Registry.ENCHANTMENT.get(enchantID);
            if (enchant == null) continue;
            names.add(enchant.getName(enchants.getCompoundTag(i).getInt("lvl")).asFormattedString());
        }
        Collections.sort(names);
        for (String enchant : names) {
            enchantNames += enchant + " ";
        }
        return stack.getItem().toString() + " " + enchants.size() + " " + enchantNames;
    }

    private static String toolDuribilityCase(ItemStack stack) {
        return stack.getItem().toString() + stack.getDamage();
    }
}
