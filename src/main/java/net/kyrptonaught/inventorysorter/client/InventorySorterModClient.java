package net.kyrptonaught.inventorysorter.client;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayConnectionEvents;
import net.kyrptonaught.inventorysorter.InventorySorterMod;
import net.kyrptonaught.inventorysorter.network.SyncBlacklistPacket;
import net.kyrptonaught.inventorysorter.network.SyncInvSortSettingsPacket;
import net.kyrptonaught.inventorysorter.client.config.ConfigOptions;
import net.kyrptonaught.kyrptconfig.config.NonConflicting.AddNonConflictingKeyBind;
import net.kyrptonaught.kyrptconfig.config.NonConflicting.NonConflictingKeyBindData;
import net.minecraft.client.util.InputUtil;

import java.util.List;

public class InventorySorterModClient implements ClientModInitializer, AddNonConflictingKeyBind {

    public static InputUtil.Key keycode;

    @Override
    public void onInitializeClient() {
        InventorySorterMod.configManager.registerFile("config.json5", new ConfigOptions());
        InventorySorterMod.configManager.load();
        ClientPlayConnectionEvents.JOIN.register((handler, sender, client) -> syncConfig());
        SyncBlacklistPacket.registerRecieveBlackList();
    }

    public static void syncConfig() {
        SyncInvSortSettingsPacket.registerSyncOnPlayerJoin();
    }

    public static ConfigOptions getConfig() {
        return (ConfigOptions)  InventorySorterMod.configManager.getConfig("config.json5");
    }

    public static boolean isKeybindPressed(int pressedKeyCode, boolean isMouse) {
        if (keycode == null) {
            keycode = InputUtil.fromTranslationKey(getConfig().keybinding);
        }
        if (isMouse) {
            if (keycode.getCategory() != InputUtil.Type.MOUSE) return false;
        } else {
            if (keycode.getCategory() != InputUtil.Type.KEYSYM) return false;
        }
        return keycode.getCode() == pressedKeyCode;
    }

    @Override
    public void addKeyBinding(List<NonConflictingKeyBindData> list) {
        InputUtil.Key key = InputUtil.fromTranslationKey(getConfig().keybinding);
        NonConflictingKeyBindData bindData = new NonConflictingKeyBindData("key.inventorysorter.sort", "key.categories.inventorysorter", key.getCategory(), key.getCode(), setKey -> {
            getConfig().keybinding = setKey.getTranslationKey();
            InventorySorterMod.configManager.save();
            keycode = null;
        });
        list.add(bindData);
    }
}
