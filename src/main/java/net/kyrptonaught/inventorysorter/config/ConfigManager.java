package net.kyrptonaught.inventorysorter.config;

import blue.endless.jankson.Jankson;
import blue.endless.jankson.JsonObject;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import net.fabricmc.loader.api.FabricLoader;
import net.kyrptonaught.inventorysorter.InventorySorterMod;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class ConfigManager {
    private static final Gson GSON = new GsonBuilder().create();
    private static final Jankson JANKSON = Jankson.builder().build();
    public ConfigOptions config;
    private final File configFile;

    public ConfigManager() {
        this.configFile = new File(FabricLoader.getInstance().getConfigDirectory(), "inventorysorter.json5");
        File old = new File(FabricLoader.getInstance().getConfigDirectory(), "inventorysorter.json");
        if (old.exists())
            loadConfig(old);
    }

    public void saveConfig() {
        try {
            if (!configFile.exists() && !configFile.createNewFile()) {
                System.out.println(InventorySorterMod.MOD_ID + " Failed to save config! Overwriting with default config.");
                config = new ConfigOptions();
                return;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            String result = JANKSON.toJson(config).toJson(true, true, 0);
            if (!configFile.exists())
                configFile.createNewFile();
            FileOutputStream out = new FileOutputStream(configFile, false);

            out.write(result.getBytes());
            out.flush();
            out.close();
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println(InventorySorterMod.MOD_ID + " Failed to save config! Overwriting with default config.");
            config = new ConfigOptions();
            return;
        }
    }

    public void loadConfig() {
        if (!configFile.exists() || !configFile.canRead()) {
            System.out.println(InventorySorterMod.MOD_ID + " Config not found! Creating one.");
            config = new ConfigOptions();
            saveConfig();
            return;
        }
        boolean failed = false;
        try {
            JsonObject configJson = JANKSON.load(configFile);
            String regularized = configJson.toJson(false, false, 0);
            config = GSON.fromJson(regularized, ConfigOptions.class);
        } catch (Exception e) {
            e.printStackTrace();
            failed = true;
        }
        if (failed || config == null) {
            System.out.println(InventorySorterMod.MOD_ID + " Failed to load config! Overwriting with default config.");
            config = new ConfigOptions();
        }
        saveConfig();
    }

    public void loadConfig(File file) {
        if (!file.exists() || !file.canRead()) {
            System.out.println(InventorySorterMod.MOD_ID + " Config not found! Creating one.");
            config = new ConfigOptions();
            saveConfig();
            return;
        }
        boolean failed = false;
        try {
            JsonObject configJson = JANKSON.load(file);
            String regularized = configJson.toJson(false, false, 0);
            config = GSON.fromJson(regularized, ConfigOptions.class);
        } catch (Exception e) {
            e.printStackTrace();
            failed = true;
        }
        if (failed || config == null) {
            System.out.println(InventorySorterMod.MOD_ID + " Failed to load config! Overwriting with default config.");
            config = new ConfigOptions();
        }
        saveConfig();
        file.delete();
    }
}
