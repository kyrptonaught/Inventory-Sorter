package net.kyrptonaught.inventorysorter.config;

public class ConfigOption {
    private String display;
    public Boolean value;
    private String comments;

    public ConfigOption(String display, Boolean value, String comments) {
        this.display = display;
        this.value = value;
        this.comments = comments;

    }

    public String toString() {
        return display + ": " + value + " ";
    }

    public String buildString() {
        return "/* " + comments + "*/\n" + buildDisplayLine() + value;
    }

    private String buildDisplayLine() {
        return '"' + display + '"' + ": ";
    }

    void parseString(String line) {
        line = line.substring(buildDisplayLine().length());
        line = line.replaceAll("[^a-zA-Z]", "");
        value = Boolean.parseBoolean(line);
    }
}
