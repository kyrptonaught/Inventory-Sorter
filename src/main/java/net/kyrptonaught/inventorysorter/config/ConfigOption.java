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

    String generateConfig() {
        return "/* " + comments + "*/\n" + generateDisplayLine() + value;
    }

    private String generateDisplayLine() {
        return '"' + display + '"' + ": ";
    }

    void parseString(String line) {
        line = line.substring(generateDisplayLine().length());
        line = line.replaceAll("[^a-zA-Z]", "");
        value = Boolean.parseBoolean(line);
    }
}
