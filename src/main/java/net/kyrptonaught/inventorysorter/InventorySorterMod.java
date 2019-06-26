package net.kyrptonaught.inventorysorter;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.client.itemgroup.FabricItemGroupBuilder;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.Identifier;

public class InventorySorterMod implements ModInitializer {
    public static final String MOD_ID = "inventorysorter";
    public static ItemGroup modItemGroup = FabricItemGroupBuilder.build(new Identifier("inventorysorter", "inventorysorter"), () -> new ItemStack(Items.DIAMOND_PICKAXE));
    @Override
    public void onInitialize() {
       InventorySorter.registerReceivePacket();
    }
}
