package net.kyrptonaught.inventorysorter;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.ModInitializer;
import net.kyrptonaught.inventorysorter.config.ConfigHelper;

public class InventorySorterMod implements ModInitializer, ClientModInitializer {
    public static final String MOD_ID = "inventorysorter";
    public static ConfigHelper config;

    @Override
    public void onInitialize() {
        InventorySorter.registerReceivePacket();
    }

    @Override
    public void onInitializeClient() {
        config = ConfigHelper.loadConfig();
    }
}
