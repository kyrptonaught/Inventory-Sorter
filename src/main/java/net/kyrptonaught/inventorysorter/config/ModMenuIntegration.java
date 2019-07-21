package net.kyrptonaught.inventorysorter.config;

import io.github.prospector.modmenu.api.ModMenuApi;
import me.shedaniel.clothconfig2.api.ConfigBuilder;
import me.shedaniel.clothconfig2.api.ConfigCategory;
import me.shedaniel.clothconfig2.api.ConfigEntryBuilder;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.kyrptonaught.inventorysorter.InventorySorterMod;
import net.minecraft.client.gui.screen.Screen;

import java.util.Optional;
import java.util.function.Supplier;

@Environment(EnvType.CLIENT)
public class ModMenuIntegration implements ModMenuApi {

    @Override
    public String getModId() {
        return InventorySorterMod.MOD_ID;
    }

    @Override
    public Optional<Supplier<Screen>> getConfigScreen(Screen screen) {
        ConfigOptions options = InventorySorterMod.config.config;
        ConfigBuilder builder = ConfigBuilder.create().setParentScreen(screen).setTitle("Inventory Sorting Config");
        builder.setSavingRunnable(() -> {
            InventorySorterMod.config.saveConfig();
        });
        ConfigCategory category = builder.getOrCreateCategory("key.inventorysorter.config.category.main");
        ConfigEntryBuilder entryBuilder = ConfigEntryBuilder.create();

        category.addEntry(entryBuilder.startBooleanToggle("key.inventorysorter.config.display_sort", options.display_sort).setSaveConsumer(val -> options.display_sort = val).build());
        category.addEntry(entryBuilder.startBooleanToggle("key.inventorysorter.config.left_display", options.left_display).setSaveConsumer(val -> options.left_display = val).build());
        category.addEntry(entryBuilder.startBooleanToggle("key.inventorysorter.config.middle_click", options.middle_click).setSaveConsumer(val -> options.middle_click = val).build());
        category.addEntry(entryBuilder.startBooleanToggle("key.inventorysorter.config.seperate_btn", options.seperate_btn).setSaveConsumer(val -> options.seperate_btn = val).build());
        category.addEntry(entryBuilder.startBooleanToggle("key.inventorysorter.config.sort_player", options.sort_player).setSaveConsumer(val -> options.sort_player = val).build());


        return Optional.of(builder::build);
    }
}
