package net.kyrptonaught.inventorysorter.config;

public class ConfigOption {
    String display;
    public Boolean value;
    private String comments;
    Boolean writeToConfig = true;

    public ConfigOption(String display, Boolean value, String comments) {
        this.display = display;
        this.value = value;
        this.comments = comments;

    }

    void isConfigLine(String line) {
        line = line.replaceAll("[^a-zA-Z\\s]", "").toLowerCase();
        String tempDisplay = display.replaceAll("[^a-zA-Z\\s]", "").toLowerCase();
        if (line.contains(tempDisplay))
            parseString(line);
    }

    public void btnPressed() {
        this.value = !this.value;
    }

    public String getDisplay() {
        return value.toString();
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

    private void parseString(String line) {
        value = Boolean.parseBoolean(line.split(" ")[1]);
    }
}
