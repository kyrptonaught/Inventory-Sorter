package net.kyrptonaught.inventorysorter.config;

import net.fabricmc.loader.api.FabricLoader;
import net.fabricmc.loader.api.ModContainer;
import net.fabricmc.loader.api.metadata.ModMetadata;
import net.kyrptonaught.inventorysorter.InventorySorterMod;
import org.apache.commons.lang3.StringUtils;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.util.List;

public class ConfigHelper {
    ConfigOption[] configOptions;
    public static String MOD_ID;
    public static String cleanName;

    public ConfigHelper(String id) {
        MOD_ID = id;
        cleanName = StringUtils.capitalize(FabricLoader.getInstance().getModContainer(id).map(ModContainer::getMetadata).map(ModMetadata::getName).orElse(id));
    }

    public void registerOptions(ConfigOption[] options) {
        configOptions = options;
    }

    public void loadConfig() {
        File configFile = new File(FabricLoader.getInstance().getConfigDirectory(), MOD_ID + ".json");
        if (!configFile.exists()) {
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(configFile))) {
                writer.write(buildConfig());
            } catch (Exception e) {
                System.out.println(cleanName + ": error writing config file");
            }
        } else {
            try {
                readConfig(Files.readAllLines(configFile.toPath()));
            } catch (IOException e) {
                System.out.println(cleanName + ": error reading config file");
            }
        }
        saveConfig();
    }

    void saveConfig() {
        File configFile = new File(FabricLoader.getInstance().getConfigDirectory(), MOD_ID + ".json");
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(configFile))) {
            writer.write(buildConfig());
        } catch (IOException e) {
            System.out.println(cleanName + ": error writing config file");
        }
    }

    public String toString() {
        String string = "";
        for (int i = 0; i < configOptions.length; i++)
            string += configOptions[i].toString();
        return string;
    }

    public ConfigOption getConfigOption(InventorySorterMod.ConfigNames option) {
        return configOptions[option.ordinal()];
    }

    public String buildConfig() {
        String string = "{\n";
        for (int i = 0; i < configOptions.length; i++)
            if (configOptions[i].writeToConfig)
                string += configOptions[i].generateConfig() + (i < configOptions.length - 1 ? ",\n" : "");
        return string + "\n}";
    }

    private void readConfig(List<String> configLines) {
        for (int i = 0; i < configLines.size(); i++) {
            for (int j = 0; j < configOptions.length; j++) {
                configOptions[j].isConfigLine(configLines.get(i));
            }
        }
    }
}