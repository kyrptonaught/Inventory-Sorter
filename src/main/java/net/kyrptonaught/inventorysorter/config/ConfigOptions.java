package net.kyrptonaught.inventorysorter.config;

import blue.endless.jankson.Comment;
import net.kyrptonaught.inventorysorter.SortCases;

public class ConfigOptions {

    @Comment("Enable 'Sort' button in inventorys")
    public boolean displaySort = true;
    @Comment("Middle click to sort inventorys")
    public boolean middleClick = true;
    @Comment("Enable second 'Sort' button in player inv")
    public boolean seperateBtn = true;
    @Comment("Sorting inv also sorts player inv")
    public boolean sortPlayer = false;
    @Comment("Method of sorting, NAME,CATEGORY,MOD")
    public SortCases.SortType sortType = SortCases.SortType.NAME;

    public boolean debugMode = false;

}
