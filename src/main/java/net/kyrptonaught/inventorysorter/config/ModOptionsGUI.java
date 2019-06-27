package net.kyrptonaught.inventorysorter.config;

import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.AbstractButtonWidget;
import net.minecraft.network.chat.TextComponent;

import java.util.function.Supplier;

public class ModOptionsGUI extends Screen implements Supplier<Screen> {

    private final Screen parent;
    private final String modName;
    private final ConfigHelper config;

    ModOptionsGUI(Screen parent, String modName, ConfigHelper confHandler) {
        super(new TextComponent(modName));
        this.parent = parent;
        this.modName = modName;
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
        this.addButton(new AbstractButtonWidget(this.width / 2 + 10, 50, config.displaySortButton.value.toString()) {
            @Override
            public void onClick(double x, double y) {
                config.displaySortButton.value = !config.displaySortButton.value;
                this.setMessage(config.displaySortButton.value.toString());
            }
        });
        this.addButton(new AbstractButtonWidget(this.width / 2 + 10, 70, config.enableMiddleClick.value.toString()) {
            @Override
            public void onClick(double x, double y) {
                config.enableMiddleClick.value = !config.enableMiddleClick.value;
                this.setMessage(config.enableMiddleClick.value.toString());
            }
        });
    }

    @Override
    public void render(int mouseX, int mouseY, float partialTicks) {
        renderBackground();
        drawCenteredString(font, "Inventory Sorter Configuration", this.width / 2, 20, 0xffffff);
        super.render(mouseX, mouseY, partialTicks);

        drawString(font, "Display Sort Button", this.width / 2 - 155, 50 + 2, 0xffffff);
        drawString(font, "Middle Click Sort", this.width / 2 - 155, 70 + 2, 0xffffff);
    }

    @Override
    public Screen get() {
        return this;
    }
}