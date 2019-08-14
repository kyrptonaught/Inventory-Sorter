package net.kyrptonaught.inventorysorter.modmenu;

import io.github.prospector.modmenu.api.ModMenuApi;
import me.shedaniel.clothconfig2.api.ConfigBuilder;
import me.shedaniel.clothconfig2.api.ConfigCategory;
import me.shedaniel.clothconfig2.api.ConfigEntryBuilder;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.kyrptonaught.inventorysorter.InventorySorterMod;
import net.kyrptonaught.inventorysorter.SortCases;
import net.kyrptonaught.inventorysorter.config.ConfigOptions;
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
        ConfigOptions options = InventorySorterMod.configManager.config;
        ConfigBuilder builder = ConfigBuilder.create().setParentScreen(screen).setTitle("Inventory Sorting Config");
        builder.setSavingRunnable(() -> {
            InventorySorterMod.configManager.saveAll();
        });
        ConfigCategory category = builder.getOrCreateCategory("key.inventorysorter.config.category.main");
        ConfigEntryBuilder entryBuilder = ConfigEntryBuilder.create();

        category.addEntry(entryBuilder.startBooleanToggle("key.inventorysorter.config.display_sort", options.displaySort).setSaveConsumer(val -> options.displaySort = val).setDefaultValue(true).build());
        category.addEntry(entryBuilder.startBooleanToggle("key.inventorysorter.config.middle_click", options.middleClick).setSaveConsumer(val -> options.middleClick = val).setDefaultValue(true).build());
        category.addEntry(entryBuilder.startBooleanToggle("key.inventorysorter.config.seperate_btn", options.seperateBtn).setSaveConsumer(val -> options.seperateBtn = val).setDefaultValue(true).build());
        category.addEntry(entryBuilder.startBooleanToggle("key.inventorysorter.config.sort_player", options.sortPlayer).setSaveConsumer(val -> options.sortPlayer = val).setDefaultValue(false).build());
        category.addEntry(entryBuilder.startEnumSelector("key.inventorysorter.config.sorttype", SortCases.SortType.class, options.sortType).setSaveConsumer(val -> options.sortType = val).build());
        return Optional.of(builder::build);
    }
}
