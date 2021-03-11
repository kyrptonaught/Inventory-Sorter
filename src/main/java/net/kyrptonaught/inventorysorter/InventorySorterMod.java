package net.kyrptonaught.inventorysorter;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.client.keybinding.FabricKeyBinding;
import net.fabricmc.fabric.api.client.keybinding.KeyBindingRegistry;
import net.fabricmc.fabric.api.command.v1.CommandRegistrationCallback;
import net.kyrptonaught.inventorysorter.client.config.ConfigOptions;
import net.kyrptonaught.inventorysorter.client.config.IgnoreList;
import net.kyrptonaught.kyrptconfig.config.ConfigManager;
import net.minecraft.client.util.InputUtil;
import net.minecraft.util.Identifier;
import org.lwjgl.glfw.GLFW;


public class InventorySorterMod implements ModInitializer, ClientModInitializer {
    public static final String MOD_ID = "inventorysorter";
    private static final String KEY_BINDING_CATEGORY = "key.categories." + MOD_ID;
    public static ConfigManager configManager = new ConfigManager.MultiConfigManager(MOD_ID);
    public static FabricKeyBinding keyBinding;

    @Override
    public void onInitialize() {
        CommandRegistrationCallback.EVENT.register(SortCommand::register);
        InventorySortPacket.registerReceivePacket();
    }

    @Override
    @Environment(EnvType.CLIENT)
    public void onInitializeClient() {
        configManager.registerFile("config.json5", new ConfigOptions());
        configManager.registerFile("blacklist.json5", new IgnoreList());
        configManager.load();
        keyBinding = FabricKeyBinding.Builder.create(
                new Identifier(MOD_ID, "sort"),
                InputUtil.Type.KEYSYM,
                GLFW.GLFW_KEY_P,
                KEY_BINDING_CATEGORY
        ).build();
        KeyBindingRegistry.INSTANCE.addCategory(KEY_BINDING_CATEGORY);
        KeyBindingRegistry.INSTANCE.register(keyBinding);
    }

    public static ConfigOptions getConfig() {
        return (ConfigOptions) configManager.getConfig("config.json5");
    }

    public static IgnoreList getBlacklist() {
        return (IgnoreList) configManager.getConfig("blacklist.json5");
    }
}
