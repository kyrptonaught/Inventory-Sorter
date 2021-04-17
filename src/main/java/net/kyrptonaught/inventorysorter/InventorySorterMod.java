package net.kyrptonaught.inventorysorter;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.command.v1.CommandRegistrationCallback;


public class InventorySorterMod implements ModInitializer {
    public static final String MOD_ID = "inventorysorter";

    @Override
    public void onInitialize() {
        CommandRegistrationCallback.EVENT.register(SortCommand::register);
        InventorySortPacket.registerReceivePacket();
    }

}
