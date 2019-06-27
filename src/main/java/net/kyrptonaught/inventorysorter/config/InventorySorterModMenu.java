package net.kyrptonaught.inventorysorter.config;

import io.github.prospector.modmenu.api.ModMenuApi;
import net.kyrptonaught.inventorysorter.InventorySorterMod;
import net.minecraft.client.gui.screen.Screen;

import java.util.Optional;
import java.util.function.Supplier;

public class InventorySorterModMenu implements ModMenuApi {
    @Override
    public String getModId() {
        return InventorySorterMod.MOD_ID;
    }

    @Override
    public Optional<Supplier<Screen>> getConfigScreen(Screen screen) {
        return Optional.of(new ModOptionsGUI(screen, InventorySorterMod.MOD_ID, InventorySorterMod.config));
    }
}