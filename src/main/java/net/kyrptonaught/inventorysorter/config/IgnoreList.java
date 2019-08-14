package net.kyrptonaught.inventorysorter.config;

import blue.endless.jankson.Comment;

import java.util.ArrayList;
import java.util.List;

public class IgnoreList {
    @Comment("Inventories that should not be sorted")
    public List<String> blacklistedInventories = new ArrayList<>();
    public transient List<String> defaultBlacklist = new ArrayList<>();

    public class DefaultList {
        public List<String> defaultBlacklist = new ArrayList<>();
    }
}
