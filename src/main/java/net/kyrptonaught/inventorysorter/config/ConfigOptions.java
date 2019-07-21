package net.kyrptonaught.inventorysorter.config;

import blue.endless.jankson.Comment;

public class ConfigOptions {

    @Comment("Enable 'Sort' button in inventorys")
    public boolean display_sort = true;
    @Comment("Middle click to sort inventorys")
    public boolean middle_click = true;
    @Comment("Display 'Sort' button on left")
    public boolean left_display = false;
    @Comment("Enable second 'Sort' button in player inv")
    public boolean seperate_btn = false;
    @Comment("Sorting inv also sorts player inv")
    public boolean sort_player = false;

}
