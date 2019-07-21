package net.kyrptonaught.inventorysorter;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.item.EnchantedBookItem;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

class SpecialSortCases {

    static String EnchantedBookNameCase(ItemStack stack) {
        ListTag enchants = EnchantedBookItem.getEnchantmentTag(stack);
        List<String> names = new ArrayList<>();
        String enchantNames = "";
        for (int i = 0; i < enchants.size(); i++) {
            CompoundTag enchantTag = enchants.getCompoundTag(i);
            Identifier enchantID = Identifier.tryParse(enchantTag.getString("id"));
            Enchantment enchant = Registry.ENCHANTMENT.get(enchantID);
            names.add(enchant.getName(enchants.getCompoundTag(i).getInt("lvl")).asFormattedString());
        }
        Collections.sort(names);
        for (String enchant : names) {
            enchantNames += enchant + " ";
        }
        return stack.getName().getString() + " " + enchants.size() + " " + enchantNames;
    }
}
