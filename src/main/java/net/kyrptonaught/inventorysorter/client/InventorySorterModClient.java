package net.kyrptonaught.inventorysorter.client;

import net.fabricmc.api.ClientModInitializer;
import net.kyrptonaught.inventorysorter.InventorySorterMod;
import net.kyrptonaught.inventorysorter.client.config.ConfigOptions;
import net.kyrptonaught.inventorysorter.client.config.IgnoreList;
import net.kyrptonaught.kyrptconfig.config.ConfigManager;
import net.kyrptonaught.kyrptconfig.config.NonConflicting.AddNonConflictingKeyBind;
import net.kyrptonaught.kyrptconfig.config.NonConflicting.NonConflictingKeyBindData;
import net.minecraft.client.util.InputUtil;

import java.util.List;

public class InventorySorterModClient implements ClientModInitializer, AddNonConflictingKeyBind {
    public static ConfigManager configManager = new ConfigManager.MultiConfigManager(InventorySorterMod.MOD_ID);
    public static InputUtil.Key keycode;

    @Override
    public void onInitializeClient() {
        configManager.registerFile("config.json5", new ConfigOptions());
        configManager.registerFile("blacklist.json5", new IgnoreList());
        configManager.load();
    }

    public static ConfigOptions getConfig() {
        return (ConfigOptions) configManager.getConfig("config.json5");
    }

    public static IgnoreList getBlacklist() {
        return (IgnoreList) configManager.getConfig("blacklist.json5");
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
            configManager.save();
            keycode = null;
        });
        list.add(bindData);
    }
}
