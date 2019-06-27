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
    public ConfigOption displaySortButton;
    public ConfigOption enableMiddleClick;

    private ConfigHelper(boolean displayButton, boolean middleClick) {
        this.displaySortButton = new ConfigOption("display_Sort", displayButton, "Should the Sort button be displayed in inventorys");
        this.enableMiddleClick = new ConfigOption("enable_Middle_Click", middleClick, "Allows clicking the middle mouse button to sort inventorys");
    }

    public static ConfigHelper loadConfig() {
        ConfigHelper config = new ConfigHelper(true, true);
        File configFile = new File(FabricLoader.getInstance().getConfigDirectory(), InventorySorterMod.MOD_ID + ".json");
        if (!configFile.exists()) {
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(configFile))) {
                writer.write(config.buildConfig());
            } catch (IOException e) {
                System.out.println("Inventory Sorter: error writing config file");
            }
        } else {
            try {
                config.readConfig(Files.readAllLines(configFile.toPath()));
            } catch (IOException e) {
                System.out.println("Inventory Sorter: error reading config file");
            }
        }
        return config;
    }

    public String toString() {
        return displaySortButton.toString() + enableMiddleClick.toString();
    }

    public String buildConfig() {
        return "{\n" + displaySortButton.buildString() + ",\n" + enableMiddleClick.buildString() + "\n}";
    }

    private void readConfig(List<String> configLines) {
        this.displaySortButton.parseString(configLines.get(2));
        this.enableMiddleClick.parseString(configLines.get(4));
    }
}