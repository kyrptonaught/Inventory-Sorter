package net.kyrptonaught.inventorysorter;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.client.keybinding.FabricKeyBinding;
import net.fabricmc.fabric.api.client.keybinding.KeyBindingRegistry;
import net.kyrptonaught.inventorysorter.config.ConfigHelper;
import net.kyrptonaught.inventorysorter.config.ConfigOption;
import net.minecraft.client.util.InputUtil;
import net.minecraft.util.Identifier;
import org.lwjgl.glfw.GLFW;

public class InventorySorterMod implements ModInitializer, ClientModInitializer {
    public static final String MOD_ID = "inventorysorter";
    public static ConfigHelper config = new ConfigHelper(MOD_ID);

    @Override
    public void onInitialize() {
        InventorySorter.registerReceivePacket();
    }

    private static final String KEY_BINDING_CATEGORY = "key.categories." + MOD_ID;
    public static FabricKeyBinding keyBinding;

    @Override
    public void onInitializeClient() {
        registerConfig();
        config.loadConfig();
        keyBinding = FabricKeyBinding.Builder.create(
                new Identifier(MOD_ID, "sort"),
                InputUtil.Type.KEYSYM,
                GLFW.GLFW_KEY_P,
                KEY_BINDING_CATEGORY
        ).build();
        KeyBindingRegistry.INSTANCE.addCategory(KEY_BINDING_CATEGORY);
        KeyBindingRegistry.INSTANCE.register(keyBinding);
    }

    public enum ConfigNames {
        display_sort, middle_click, left_display, seperate_btn, sort_player
    }

    private void registerConfig() {
        ConfigOption displaySortButton = new ConfigOption("display_Sort_btn", true, "Should the Sort button be displayed in inventorys");
        ConfigOption enableMiddleClick = new ConfigOption("enable_MiddleClick_sort", true, "Allows clicking the middle mouse button to sort inventorys");
        ConfigOption leftDisplay = new ConfigOption("display_sort_on_left", false, "Should the Sort button be displayed on the left instead");
        ConfigOption separatePlayerInvSortBtn = new ConfigOption("separate_playerInv_sort_btn", false, "Should a second btn be displayed to sort player inventory");
        ConfigOption sortPlayerInventory = new ConfigOption("also_sort_player_inventory", false, "Should sorting another inventory also sort the players");
        config.registerOptions(new ConfigOption[]{displaySortButton, enableMiddleClick, leftDisplay, separatePlayerInvSortBtn, sortPlayerInventory});
    }
}
