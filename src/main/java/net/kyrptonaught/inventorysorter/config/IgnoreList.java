package net.kyrptonaught.inventorysorter.config;

import blue.endless.jankson.Comment;
import com.google.common.collect.ImmutableSet;
import net.minecraft.client.gui.screen.ingame.*;

import java.util.ArrayList;
import java.util.List;

public class IgnoreList {
    @Comment("Inventories that should not be sorted")
    public List<String> blacklistedInventories = new ArrayList<>();

    public transient ImmutableSet<String> defaultBlacklist = ImmutableSet.of(CreativeInventoryScreen.class.getName(),
            BeaconScreen.class.getName(), AnvilScreen.class.getName(), EnchantingScreen.class.getName(),
            GrindstoneScreen.class.getName(), AbstractContainerScreen.class.getName(), LoomScreen.class.getName(),
            CraftingTableScreen.class.getName(), BrewingStandScreen.class.getName(), HorseScreen.class.getName(),
            "com.github.briansemrau.cosmeticarmorslots.client.gui.screen.ingame.CosmeticArmorInventoryScreen",
            "me.marnic.extrabows.client.gui.BowUpgradeGui");

}
