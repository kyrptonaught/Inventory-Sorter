package net.kyrptonaught.inventorysorter.config;

import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.AbstractButtonWidget;
import net.minecraft.network.chat.TextComponent;

import java.util.function.Supplier;

public class ModOptionsGUI extends Screen implements Supplier<Screen> {

    private final Screen parent;
    private final ConfigHelper config;

    ModOptionsGUI(Screen parent, String modName, ConfigHelper confHandler) {
        super(new TextComponent(modName));
        this.parent = parent;
        this.config = confHandler;
    }

    @Override
    protected void init() {
        this.addButton(new AbstractButtonWidget(this.width / 2 - 100, this.height - 27, "Done") {
            @Override
            public void onClick(double x, double y) {
                config.saveConfig();
                minecraft.openScreen(parent);
            }
        });
        int starty = 50;
        for (int i = 0; i < config.configOptions.length; i++) {
            int finalI = i;
            this.addButton(new AbstractButtonWidget(this.width / 2 + 10, starty + (finalI * 20), config.configOptions[finalI].value.toString()) {
                @Override
                public void onClick(double x, double y) {
                    config.configOptions[finalI].value = !config.configOptions[finalI].value;
                    this.setMessage(config.configOptions[finalI].value.toString());
                }
            });
        }
    }

    @Override
    public void render(int mouseX, int mouseY, float partialTicks) {
        renderBackground();
        drawCenteredString(font, "Inventory Sorter Configuration", this.width / 2, 20, 0xffffff);
        super.render(mouseX, mouseY, partialTicks);
        int starty = 52;
        for (int i = 0; i < config.configOptions.length; i++) {
            drawString(font, config.configOptions[i].display, this.width / 2 - 200, starty + (i * 20), 0xffffff);
        }
    }
    @Override
    public Screen get() {
        return this;
    }
}