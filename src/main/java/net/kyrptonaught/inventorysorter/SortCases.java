package net.kyrptonaught.inventorysorter;

import net.minecraft.block.ShulkerBoxBlock;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.inventory.Inventories;
import net.minecraft.item.*;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtList;
import net.minecraft.tag.ItemTags;
import net.minecraft.text.MutableText;
import net.minecraft.util.Identifier;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.util.registry.Registry;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

public class SortCases {
    public enum SortType {
        NAME, CATEGORY, MOD, ID;

        public String getTranslationKey() {
            return "key." + InventorySorterMod.MOD_ID + ".sorttype." + this.toString().toLowerCase();
        }
    }

    static String getStringForSort(ItemStack stack, SortType sortType) {
        Item item = stack.getItem();
        String itemName = specialCases(stack);
        switch (sortType) {
            case CATEGORY:
                ItemGroup group = item.getGroup();
                return (group != null ? group.getName() : "zzz") + itemName;
            case MOD:
                return Registry.ITEM.getId(item).getNamespace() + itemName;
            case NAME:
                if (stack.hasCustomName()) return stack.getName() + itemName;
        }
        return itemName;
    }

    private static String specialCases(ItemStack stack) {
        Item item = stack.getItem();
        NbtCompound tag = stack.getNbt();

        if (tag != null && tag.contains("SkullOwner"))
            return playerHeadCase(stack);
        if (stack.getCount() != stack.getMaxCount())
            return stackSize(stack);
        if (item instanceof EnchantedBookItem)
            return enchantedBookNameCase(stack);
        if (item instanceof ToolItem)
            return toolDuribilityCase(stack);
		if (tag != null && item instanceof BlockItem blockItem && blockItem.getBlock() instanceof ShulkerBoxBlock){
			NbtCompound compound = BlockItem.getBlockEntityNbt(stack);
			if (compound != null){
				DefaultedList<ItemStack> defaultedList = DefaultedList.ofSize(27, ItemStack.EMPTY);
				List<String> stringList = new ArrayList<>(27);
				Inventories.readNbt(compound, defaultedList);
				for (ItemStack itemStack : defaultedList) {
					stringList.add(itemStack.getItem().toString());
				}
				return item + String.join(" ", stringList);
			}
		}
        return item.toString();
    }

    private static String playerHeadCase(ItemStack stack) {
        NbtCompound tag = stack.getNbt();
        NbtCompound skullOwner = tag.getCompound("SkullOwner");
        String ownerName = skullOwner.getString("Name");

        // this is duplicated logic, so we should probably refactor
        String count = "";
        if (stack.getCount() != stack.getMaxCount()) {
            count = Integer.toString(stack.getCount());
        }

        return stack.getItem().toString() + " " + ownerName + count;
    }

    private static String stackSize(ItemStack stack) {
        return stack.getItem().toString() + stack.getCount();
    }

    private static String enchantedBookNameCase(ItemStack stack) {
        NbtList enchants = EnchantedBookItem.getEnchantmentNbt(stack);
        List<String> names = new ArrayList<>();
        StringBuilder enchantNames = new StringBuilder();
        for (int i = 0; i < enchants.size(); i++) {
            NbtCompound enchantTag = enchants.getCompound(i);
            Identifier enchantID = Identifier.tryParse(enchantTag.getString("id"));
            if (enchantID == null) continue;
            Enchantment enchant = Registry.ENCHANTMENT.get(enchantID);
            if (enchant == null) continue;
            names.add(enchant.getName(enchantTag.getInt("lvl")).getString());
        }
        Collections.sort(names);
        for (String enchant : names) {
            enchantNames.append(enchant).append(" ");
        }
        return stack.getItem().toString() + " " + enchants.size() + " " + enchantNames;
    }

    private static String toolDuribilityCase(ItemStack stack) {
        return stack.getItem().toString() + stack.getDamage();
    }
}
