package net.kyrptonaught.inventorysorter;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.item.*;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtList;
import net.minecraft.registry.Registries;
import net.minecraft.resource.featuretoggle.FeatureSet;
import net.minecraft.util.Identifier;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.stream.IntStream;

public class SortCases {
    public enum SortType {
        NAME, CATEGORY, MOD, ID;

        public String getTranslationKey() {
            return "key." + InventorySorterMod.MOD_ID + ".sorttype." + this.toString().toLowerCase();
        }
    }

    static Comparator<ItemStack> getComparator(SortType sortType) {
        var defaultComparator = Comparator.comparing(SortCases::specialCases);
        switch (sortType) {
            case CATEGORY -> {
                return Comparator.comparing(SortCases::getGroupIdentifier).thenComparing(defaultComparator);
            }
            case MOD -> {
                return Comparator.comparing((ItemStack stack) -> {
                    return Registries.ITEM.getId(stack.getItem()).getNamespace();
                }).thenComparing(defaultComparator);
            }
            case NAME -> {
                return Comparator.comparing(stack -> {
                    var name = specialCases(stack);
                    if (stack.hasCustomName()) return stack.getName() + name;
                    return name;
                });
            }
            default -> {
                return defaultComparator;
            }
        }
    }

    private static int getGroupIdentifier(ItemStack stack) {
        List<ItemGroup> groups = ItemGroups.getGroups();
        for (int i = 0; i < groups.size(); i++) {
            var group = groups.get(i);
            var stacks = group.getSearchTabStacks().stream().toList();
            var index = IntStream
                    .range(0, stacks.size())
                    .filter(j -> ItemStack.canCombine(stacks.get(j), stack))
                    .findFirst();

            if (index.isPresent()) {
                return i * 1000 + index.getAsInt();
            }
        }
        return 99999;
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
            Enchantment enchant = Registries.ENCHANTMENT.get(enchantID);
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
