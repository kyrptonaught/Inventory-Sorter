package net.kyrptonaught.inventorysorter.config;

import net.fabricmc.loader.api.FabricLoader;
import net.kyrptonaught.inventorysorter.InventorySorterMod;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.util.List;
public class ConfigHelper {
    public enum Option {
        display_sort, middle_click, left_display, sort_player
    }

    public ConfigOption[] configOptions;

    private ConfigHelper(boolean displayButton, boolean middleClick, boolean left, boolean playerSort) {
        ConfigOption displaySortButton = new ConfigOption("display_Sort", displayButton, "Should the Sort button be displayed in inventorys");
        ConfigOption enableMiddleClick = new ConfigOption("enable_Middle_Click", middleClick, "Allows clicking the middle mouse button to sort inventorys");
        ConfigOption leftDisplay = new ConfigOption("display_left_side", left, "Should the Sort button be displayed on the left instead");
        ConfigOption sortPlayerInventory = new ConfigOption("always_sort_player_inventory", playerSort, "Should sorting another inventory also sort the players");
        configOptions = new ConfigOption[]{displaySortButton, enableMiddleClick, leftDisplay, sortPlayerInventory};
    }

    public static ConfigHelper loadConfig() {
        ConfigHelper config = new ConfigHelper(true, true, false, false);
        File configFile = new File(FabricLoader.getInstance().getConfigDirectory(), InventorySorterMod.MOD_ID + ".json");
        if (!configFile.exists()) {
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(configFile))) {
                writer.write(config.buildConfig());
            } catch (Exception e) {
                System.out.println("Inventory Sorter: error writing config file");
            }
        } else {
            try {
                config.readConfig(Files.readAllLines(configFile.toPath()));
            } catch (IOException e) {
                System.out.println("Inventory Sorter: error reading config file");
            }
        }
        config.saveConfig();
        return config;
    }

    void saveConfig() {
        File configFile = new File(FabricLoader.getInstance().getConfigDirectory(), InventorySorterMod.MOD_ID + ".json");
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(configFile))) {
            writer.write(buildConfig());
        } catch (IOException e) {
            System.out.println("Inventory Sorter: error writing config file");
        }
    }

    public String toString() {
        String string = "";
        for (int i = 0; i < configOptions.length; i++)
            string += configOptions[i].toString();
        return string;
    }

    public ConfigOption getConfigOption(Option option) {
        return configOptions[option.ordinal()];
    }

    public String buildConfig() {
        String string = "{\n";
        for (int i = 0; i < configOptions.length; i++)
            string += configOptions[i].generateConfig() + (i < configOptions.length - 1 ? ",\n" : "\n}");
        return string;
    }

    private void readConfig(List<String> configLines) {
        for (int i = 0; i < configLines.size(); i++) {
            for (int j = 0; j < configOptions.length; j++) {
                configOptions[j].isConfigLine(configLines.get(i));
            }
        }
    }
}