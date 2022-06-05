package net.kyrptonaught.inventorysorter.client;

import com.mojang.blaze3d.systems.RenderSystem;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.kyrptonaught.inventorysorter.InventoryHelper;
import net.kyrptonaught.inventorysorter.InventorySorterMod;
import net.kyrptonaught.inventorysorter.SortCases;
import net.kyrptonaught.inventorysorter.network.InventorySortPacket;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.widget.TexturedButtonWidget;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.ClickEvent;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import org.apache.commons.lang3.StringUtils;
import org.lwjgl.glfw.GLFW;

import java.util.ArrayList;
import java.util.List;

@Environment(EnvType.CLIENT)
public class SortButtonWidget extends TexturedButtonWidget {
    private static final Identifier texture = new Identifier(InventorySorterMod.MOD_ID, "textures/gui/button.png");
    private final boolean playerInv;

    public SortButtonWidget(int int_1, int int_2, boolean playerInv) {
        super(int_1, int_2, 10, 9, 0, 0, 19, texture, 20, 37, null, Text.literal(""));
        this.playerInv = playerInv;
    }

    @Override
    public void onPress() {
        if (InventorySorterModClient.getConfig().debugMode && GLFW.glfwGetKey(MinecraftClient.getInstance().getWindow().getHandle(), GLFW.GLFW_KEY_LEFT_CONTROL) == 1) {
            if (InventoryHelper.canSortInventory(MinecraftClient.getInstance().player)) {
                String screenID = Registry.SCREEN_HANDLER.getId(MinecraftClient.getInstance().player.currentScreenHandler.getType()).toString();
                System.out.println("Add the line below to config/inventorysorter/blacklist.json5 to blacklist this inventory");
                System.out.println(screenID);

                MutableText MODID = Text.literal("[" + InventorySorterMod.MOD_ID + "]: ").formatted(Formatting.BLUE);
                MutableText autoDNS = (Text.translatable("key.inventorysorter.sortbtn.clickhere")).formatted(Formatting.UNDERLINE, Formatting.WHITE)
                        .styled((style) -> style.withClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/invsort blacklist doNotSort " + screenID)));
                MutableText autoDND = (Text.translatable("key.inventorysorter.sortbtn.clickhere")).formatted(Formatting.UNDERLINE, Formatting.WHITE)
                        .styled((style) -> style.withClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/invsort blacklist doNotDisplay " + screenID)));
                MinecraftClient.getInstance().player.sendMessage(MODID.copyContentOnly().append(autoDNS).append(Text.translatable("key.inventorysorter.sortbtn.dnsadd").formatted(Formatting.WHITE)), false);
                MinecraftClient.getInstance().player.sendMessage(MODID.copyContentOnly().append(autoDND).append(Text.translatable("key.inventorysorter.sortbtn.dndadd").formatted(Formatting.WHITE)), false);
            } else
                MinecraftClient.getInstance().player.sendMessage(Text.literal("[" + InventorySorterMod.MOD_ID + "]: ").append(Text.translatable("key.inventorysorter.sortbtn.error")), false);
        } else
            InventorySortPacket.sendSortPacket(playerInv);
    }

    @Override
    public void renderButton(MatrixStack matrixStack, int int_1, int int_2, float float_1) {
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderTexture(0, texture);
        RenderSystem.enableDepthTest();
        matrixStack.push();
        matrixStack.scale(.5f, .5f, 1);
        matrixStack.translate(x, y, 0);
        drawTexture(matrixStack, this.x, this.y, 0, this.isHovered() ? 19 : 0, 20, 18, 20, 37);
        this.renderTooltip(matrixStack, int_1, int_2);
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
        InventorySorterMod.configManager.save();
        InventorySorterModClient.syncConfig();
        return true;
    }

    @Override
    public void renderTooltip(MatrixStack matrices, int mouseX, int mouseY) {
        if (InventorySorterModClient.getConfig().displayTooltip && this.isHovered()) {
            List<Text> lines = new ArrayList<>();
            lines.add(Text.translatable("key.inventorysorter.sortbtn.sort").append(Text.translatable(InventorySorterModClient.getConfig().sortType.getTranslationKey())));
            if (GLFW.glfwGetKey(MinecraftClient.getInstance().getWindow().getHandle(), GLFW.GLFW_KEY_LEFT_CONTROL) == 1) {
                lines.add(Text.translatable("key.inventorysorter.sortbtn.debug"));
                lines.add(Text.translatable("key.inventorysorter.sortbtn.debug2"));
            }
            MinecraftClient.getInstance().currentScreen.renderTooltip(matrices, lines, x, y);
        }
    }
}
