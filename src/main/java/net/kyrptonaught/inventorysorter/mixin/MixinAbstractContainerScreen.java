package net.kyrptonaught.inventorysorter.mixin;

import net.kyrptonaught.inventorysorter.InventorySorter;
import net.kyrptonaught.inventorysorter.InventorySorterMod;
import net.kyrptonaught.inventorysorter.config.ConfigHelper;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.ingame.*;
import net.minecraft.client.gui.widget.TexturedButtonWidget;
import net.minecraft.network.chat.Component;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;


@Mixin(AbstractContainerScreen.class)
public abstract class MixinAbstractContainerScreen extends Screen {
    protected MixinAbstractContainerScreen(Component component) {
        super(component);
    }

    @Shadow
    private int containerWidth;
    @Shadow
    private int top;
    int calcOffset;
    TexturedButtonWidget btn;
    private Identifier BUTTON_TEX = new Identifier(InventorySorterMod.MOD_ID, "textures/gui/button.png");
    @Inject(method = "init", at = @At("TAIL"), cancellable = true)
    protected void init(CallbackInfo callbackinfo) {
        if (InventorySorterMod.config.getConfigOption(ConfigHelper.Option.display_sort).value) {
            Screen currentScreen = MinecraftClient.getInstance().currentScreen;
            if (!shouldInject(currentScreen)) return;
            calcOffset = (this.width / 2) + (this.containerWidth / 2);
            if (InventorySorterMod.config.getConfigOption(ConfigHelper.Option.left_display).value)
                calcOffset = (this.width / 2) - (this.containerWidth / 2 + 35);
            this.addButton(btn = new TexturedButtonWidget(calcOffset, top, 20, 18, 0, 0, 19, BUTTON_TEX, var1 -> sendPacketToClient(currentScreen)));
        }
    }

    @Inject(method = "mouseClicked", at = @At("HEAD"), cancellable = true)
    public void mouseClicked(double x, double y, int button, CallbackInfoReturnable<Boolean> callbackInfoReturnable) {
        if (InventorySorterMod.config.getConfigOption(ConfigHelper.Option.middle_click).value && button == 2) {
            Screen currentScreen = MinecraftClient.getInstance().currentScreen;
            if (!shouldInject(currentScreen)) return;
            sendPacketToClient(currentScreen);
        }
    }

    @Inject(method = "keyPressed", at = @At("HEAD"), cancellable = true)
    public void keyPressed(int keycode, int scancode, int modifiers, CallbackInfoReturnable<Boolean> callbackInfoReturnable) {
        if (InventorySorterMod.keyBinding.matchesKey(keycode, scancode)) {
            Screen currentScreen = MinecraftClient.getInstance().currentScreen;
            if (currentScreen instanceof AbstractContainerScreen && shouldInject(currentScreen))
                sendPacketToClient(currentScreen);
            callbackInfoReturnable.setReturnValue(true);
        }
    }

    @Inject(method = "render", at = @At("TAIL"), cancellable = true)
    public void render(int int_1, int int_2, float float_1, CallbackInfo callbackinfo) {
        Screen currentScreen = MinecraftClient.getInstance().currentScreen;
        if (currentScreen instanceof InventoryScreen) {
            InventoryScreen invScreen = (InventoryScreen) currentScreen;
            if (invScreen.getRecipeBookGui().isOpen())
                btn.setPos((this.width / 2) + (this.containerWidth / 2) + 75, btn.y);
            else btn.setPos(calcOffset, btn.y);
        }
    }
    private void sendPacketToClient(Screen currentScreen) {
        if (currentScreen instanceof InventoryScreen)
            InventorySorter.sendPacket(true);
        else {
            InventorySorter.sendPacket(false);
            if (InventorySorterMod.config.getConfigOption(ConfigHelper.Option.sort_player).value)
                InventorySorter.sendPacket(true);
        }
    }

    private Boolean shouldInject(Screen currentScreen) {
        return !(currentScreen instanceof CreativeInventoryScreen) && !(currentScreen instanceof BeaconScreen) && !(currentScreen instanceof AnvilScreen) && !(currentScreen instanceof EnchantingScreen) && !(currentScreen instanceof GrindstoneScreen) && !(currentScreen instanceof AbstractFurnaceScreen) && !(currentScreen instanceof LoomScreen) && !(currentScreen instanceof CraftingTableScreen) && !(currentScreen instanceof BrewingStandScreen) && !(currentScreen instanceof HorseScreen);
    }
}