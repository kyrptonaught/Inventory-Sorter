package net.kyrptonaught.inventorysorter.client;

import com.mojang.blaze3d.systems.RenderSystem;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.kyrptonaught.inventorysorter.InventoryHelper;
import net.kyrptonaught.inventorysorter.InventorySorterMod;
import net.kyrptonaught.inventorysorter.SortCases;
import net.kyrptonaught.inventorysorter.network.InventorySortPacket;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.ButtonTextures;
import net.minecraft.client.gui.widget.TexturedButtonWidget;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.registry.Registries;
import net.minecraft.text.ClickEvent;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.Identifier;
import org.lwjgl.glfw.GLFW;

import java.util.ArrayList;
import java.util.List;

@Environment(EnvType.CLIENT)
public class SortButtonWidget extends TexturedButtonWidget {
    private static final Identifier texture = new Identifier(InventorySorterMod.MOD_ID, "textures/gui/button.png");
    private static final Identifier textureFocused = new Identifier(InventorySorterMod.MOD_ID, "textures/gui/button_focused.png");
    private final boolean playerInv;

    public SortButtonWidget(int int_1, int int_2, boolean playerInv) {
        super(int_1, int_2, 10, 9, new ButtonTextures(texture, textureFocused), null, Text.literal(""));
        this.playerInv = playerInv;
    }

    @Override
    public void onPress() {
        if (InventorySorterModClient.getConfig().debugMode && GLFW.glfwGetKey(MinecraftClient.getInstance().getWindow().getHandle(), GLFW.GLFW_KEY_LEFT_CONTROL) == 1) {
            if (InventoryHelper.canSortInventory(MinecraftClient.getInstance().player)) {
                String screenID = Registries.SCREEN_HANDLER.getId(MinecraftClient.getInstance().player.currentScreenHandler.getType()).toString();
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
    public void renderButton(DrawContext context, int mouseX, int mouseY, float delta) {
        RenderSystem.setShader(GameRenderer::getPositionProgram);
        RenderSystem.enableDepthTest();
        context.getMatrices().push();
        context.getMatrices().scale(.5f, .5f, 1);
        context.getMatrices().translate(getX(), getY(), 0);

        Identifier identifier = this.textures.get(true, this.isSelected() || this.isHovered());
        context.drawTexture(identifier, getX(), getY(), 0, 0, 20, 18, 20, 18);
        this.renderTooltip(context, mouseX, mouseY);
        context.getMatrices().pop();
    }

    @Override
    public boolean mouseScrolled(double mouseX, double mouseY, double horizontalAmount, double verticalAmount) {
        int current = InventorySorterModClient.getConfig().sortType.ordinal();
        if (verticalAmount > 0) {
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


    public void renderTooltip(DrawContext context, int mouseX, int mouseY) {
        if (InventorySorterModClient.getConfig().displayTooltip && this.isHovered()) {
            List<Text> lines = new ArrayList<>();
            lines.add(Text.translatable("key.inventorysorter.sortbtn.sort").append(Text.translatable(InventorySorterModClient.getConfig().sortType.getTranslationKey())));
            if (GLFW.glfwGetKey(MinecraftClient.getInstance().getWindow().getHandle(), GLFW.GLFW_KEY_LEFT_CONTROL) == 1) {
                lines.add(Text.translatable("key.inventorysorter.sortbtn.debug"));
                lines.add(Text.translatable("key.inventorysorter.sortbtn.debug2"));
            }
            context.drawTooltip(MinecraftClient.getInstance().textRenderer, lines, getX(), getY());
        }
    }
}
