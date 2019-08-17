package net.kyrptonaught.inventorysorter;

import com.mojang.blaze3d.platform.GlStateManager;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.widget.TexturedButtonWidget;
import net.minecraft.util.Identifier;
import org.lwjgl.glfw.GLFW;

@Environment(EnvType.CLIENT)
public class SortButtonWidget extends TexturedButtonWidget {
    private static final Identifier texture = new Identifier(InventorySorterMod.MOD_ID, "textures/gui/button.png");

    public SortButtonWidget(int int_1, int int_2, PressAction pressAction) {
        super(int_1, int_2, 10, 10, 0, 0, 19, texture, 20, 37, pressAction, "");
    }

    @Override
    public void onPress() {
        if (InventorySorterMod.getConfig().debugMode && GLFW.glfwGetKey(MinecraftClient.getInstance().window.getHandle(), GLFW.GLFW_KEY_LEFT_CONTROL) == 1) {
            System.out.println("Add the line below to config/inventorysorter/blacklist.json5 to blacklist this inventory");
            System.out.println(MinecraftClient.getInstance().currentScreen.getClass().getName());
        } else this.onPress.onPress(this);
    }

    @Override
    public void renderButton(int int_1, int int_2, float float_1) {
        GlStateManager.pushMatrix();
        MinecraftClient minecraftClient_1 = MinecraftClient.getInstance();
        minecraftClient_1.getTextureManager().bindTexture(texture);
        GlStateManager.scalef(.5f, .5f, 1);
        GlStateManager.translatef(this.x, this.y, 0);
        blit(this.x, this.y, 0, this.isHovered() ? 19 : 0, 20, 18, 20, 37);
        this.renderToolTip(int_1, int_2);
        GlStateManager.disableLighting();
        GlStateManager.popMatrix();
    }

    @Override
    public void renderToolTip(int x, int y) {
        if (InventorySorterMod.getConfig().displayTooltip && this.isHovered())
            MinecraftClient.getInstance().currentScreen.renderTooltip("Sort by: " + InventorySorterMod.getConfig().sortType, x, y);
    }
}
