package net.kyrptonaught.inventorysorter.client;

import com.mojang.blaze3d.systems.RenderSystem;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.kyrptonaught.inventorysorter.InventorySortPacket;
import net.kyrptonaught.inventorysorter.InventorySorterMod;
import net.kyrptonaught.inventorysorter.SortCases;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.widget.TexturedButtonWidget;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.LiteralText;
import net.minecraft.util.Identifier;
import org.apache.commons.lang3.StringUtils;
import org.lwjgl.glfw.GLFW;

@Environment(EnvType.CLIENT)
public class SortButtonWidget extends TexturedButtonWidget {
    private static final Identifier texture = new Identifier(InventorySorterMod.MOD_ID, "textures/gui/button.png");
    private final boolean playerInv;

    public SortButtonWidget(int int_1, int int_2, boolean playerInv) {
        super(int_1, int_2, 10, 9, 0, 0, 19, texture, 20, 37, null, new LiteralText(""));
        this.playerInv = playerInv;
    }

    @Override
    public void onPress() {
        if (InventorySorterModClient.getConfig().debugMode && GLFW.glfwGetKey(MinecraftClient.getInstance().getWindow().getHandle(), GLFW.GLFW_KEY_LEFT_CONTROL) == 1) {
            System.out.println("Add the line below to config/inventorysorter/blacklist.json5 to blacklist this inventory");
            System.out.println(MinecraftClient.getInstance().currentScreen.getClass().getName());
        } else
            InventorySortPacket.sendSortPacket(playerInv);
    }

    @Override
    public void renderButton(MatrixStack matrixStack, int int_1, int int_2, float float_1) {
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderTexture(0, texture);
        RenderSystem.enableDepthTest();
        matrixStack.push();
        matrixStack.scale(.5f,.5f,1);
        matrixStack.translate(x,y,0);
        drawTexture(matrixStack, this.x, this.y, 0, this.isHovered() ? 19 : 0, 20, 18, 20, 37);
        this.renderToolTip(matrixStack, int_1, int_2);
        matrixStack.pop();
    }

    @Override
    public boolean mouseScrolled(double mouseX, double mouseY, double amount) {
        int current = InventorySorterModClient.getConfig().sortType.ordinal();
        if (amount > 0) {
            current++;
            if (current >= SortCases.SortType.values().length)
                current = 0;
        } else {
            current--;
            if (current < 0)
                current = SortCases.SortType.values().length - 1;
        }
        InventorySorterModClient.getConfig().sortType = SortCases.SortType.values()[current];
        InventorySorterModClient.configManager.save();
        return true;
    }

    @Override
    public void renderToolTip(MatrixStack matrixStack, int x, int y) {
        if (InventorySorterModClient.getConfig().displayTooltip && this.isHovered())
            MinecraftClient.getInstance().currentScreen.renderTooltip(matrixStack, new LiteralText("Sort by: " + StringUtils.capitalize(InventorySorterModClient.getConfig().sortType.toString().toLowerCase())), x, y);
    }
}
