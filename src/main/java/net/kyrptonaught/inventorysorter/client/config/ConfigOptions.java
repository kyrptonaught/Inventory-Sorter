package net.kyrptonaught.inventorysorter.client.config;

import blue.endless.jankson.Comment;
import net.kyrptonaught.inventorysorter.InventorySorterMod;
import net.kyrptonaught.inventorysorter.SortCases;
import net.kyrptonaught.kyrptconfig.config.ConfigWDefaults;
import net.kyrptonaught.kyrptconfig.keybinding.CustomKeyBinding;

public class ConfigOptions extends ConfigWDefaults {

    @Comment("Enable 'Sort' button in inventorys")
    public boolean displaySort = true;
    @Comment("Middle click slot to sort inventorys")
    public boolean middleClick = true;
    @Comment("Double click slot to sort inventorys")
    public boolean doubleClickSort = true;
    @Comment("Enable second 'Sort' button in player inv")
    public boolean seperateBtn = true;
    @Comment("Sorting inv also sorts player inv")
    public boolean sortPlayer = false;
    @Comment("Method of sorting, NAME,CATEGORY,MOD")
    public SortCases.SortType sortType = SortCases.SortType.NAME;
    @Comment("Display Sort Button Tooltip")
    public boolean displayTooltip = true;

    @Comment("Sort Inventory key")
    public CustomKeyBinding keybinding = CustomKeyBinding.configDefault(InventorySorterMod.MOD_ID, "key.keyboard.p");

    @Comment("Should sort half of open inv highlighted by mouse")
    public Boolean sortMouseHighlighted = true;

    public boolean debugMode = false;

    @Override
    public ConfigOptions getDefaults() {
        return (ConfigOptions) super.getDefaults();
    }

    @Override
    public void afterLoad() {
        keybinding.copyFromDefault(getDefaults().keybinding);
    }
}
