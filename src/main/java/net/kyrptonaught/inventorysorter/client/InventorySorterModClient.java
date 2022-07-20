package net.kyrptonaught.inventorysorter.client;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayConnectionEvents;
import net.kyrptonaught.inventorysorter.InventorySorterMod;
import net.kyrptonaught.inventorysorter.client.config.ConfigOptions;
import net.kyrptonaught.inventorysorter.network.SyncBlacklistPacket;
import net.kyrptonaught.inventorysorter.network.SyncInvSortSettingsPacket;
import net.kyrptonaught.kyrptconfig.keybinding.DisplayOnlyKeyBind;
import net.minecraft.client.util.InputUtil;

public class InventorySorterModClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        InventorySorterMod.configManager.registerFile("config.json5", new ConfigOptions());
        InventorySorterMod.configManager.load();
        ClientPlayConnectionEvents.JOIN.register((handler, sender, client) -> syncConfig());
        SyncBlacklistPacket.registerReceiveBlackList();

        KeyBindingHelper.registerKeyBinding(new DisplayOnlyKeyBind(
                "key.inventorysorter.sort",
                "key.categories.inventorysorter",
                getConfig().keybinding,
                setKey -> InventorySorterMod.configManager.save()
        ));
    }

    public static void syncConfig() {
        SyncInvSortSettingsPacket.registerSyncOnPlayerJoin();
    }

    public static ConfigOptions getConfig() {
        return (ConfigOptions) InventorySorterMod.configManager.getConfig("config.json5");
    }

    public static boolean isKeybindPressed(int pressedKeyCode, InputUtil.Type type) {
        return getConfig().keybinding.matches(pressedKeyCode, type);
    }
}
