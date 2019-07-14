package net.kyrptonaught.inventorysorter.mixin;

import net.kyrptonaught.inventorysorter.InventorySorter;
import net.kyrptonaught.inventorysorter.InventorySorterMod;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.ingame.AbstractContainerScreen;
import net.minecraft.client.gui.screen.ingame.InventoryScreen;
import net.minecraft.client.gui.widget.TexturedButtonWidget;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;


@Mixin(AbstractContainerScreen.class)
public abstract class MixinAbstractContainerScreen extends Screen {
    protected MixinAbstractContainerScreen(Text component) {
        super(component);
    }

    @Shadow
    private int containerWidth;
    @Shadow
    private int containerHeight;
    @Shadow
    private int top;
    @Shadow
    private int left;

    private Identifier invsort$BUTTON_TEX = new Identifier(InventorySorterMod.MOD_ID, "textures/gui/button.png");
    private TexturedButtonWidget invsort$btn;

    @Inject(method = "init", at = @At("TAIL"), cancellable = true)
    protected void invsort$init(CallbackInfo callbackinfo) {
        if (InventorySorterMod.config.getConfigOption(InventorySorterMod.ConfigNames.display_sort).value) {
            Screen currentScreen = MinecraftClient.getInstance().currentScreen;
            if (!InventorySorter.shouldInject(currentScreen)) return;
            int calcOffset = this.left + this.containerWidth;
            if (InventorySorterMod.config.getConfigOption(InventorySorterMod.ConfigNames.left_display).value)
                calcOffset = this.left - 20;
            this.addButton(invsort$btn = new TexturedButtonWidget(calcOffset, top, 20, 18, 0, 0, 19, invsort$BUTTON_TEX, var1 -> InventorySorter.sendSortPacket(currentScreen)));
            if (InventorySorterMod.config.getConfigOption(InventorySorterMod.ConfigNames.seperate_btn).value && !(currentScreen instanceof InventoryScreen))
                this.addButton(new TexturedButtonWidget(calcOffset, containerHeight - 85, 20, 18, 0, 0, 19, invsort$BUTTON_TEX, var1 -> InventorySorter.sendSortPacketWithValue(true)));
        }
    }

    @Inject(method = "mouseClicked", at = @At("HEAD"), cancellable = true)
    public void invsort$mouseClicked(double x, double y, int button, CallbackInfoReturnable<Boolean> callbackInfoReturnable) {
        if (InventorySorterMod.config.getConfigOption(InventorySorterMod.ConfigNames.middle_click).value && button == 2) {
            Screen currentScreen = MinecraftClient.getInstance().currentScreen;
            if (InventorySorter.shouldInject(currentScreen)) {
                InventorySorter.sendSortPacket(currentScreen);
                callbackInfoReturnable.setReturnValue(true);
            }
        }
    }

    @Inject(method = "keyPressed", at = @At("HEAD"), cancellable = true)
    public void invsort$keyPressed(int keycode, int scancode, int modifiers, CallbackInfoReturnable<Boolean> callbackInfoReturnable) {
        if (InventorySorterMod.keyBinding.matchesKey(keycode, scancode)) {
            Screen currentScreen = MinecraftClient.getInstance().currentScreen;
            if (currentScreen instanceof AbstractContainerScreen && InventorySorter.shouldInject(currentScreen)) {
                InventorySorter.sendSortPacket(currentScreen);
                callbackInfoReturnable.setReturnValue(true);
            }
        }
    }

    @Inject(method = "render", at = @At("TAIL"), cancellable = true)
    public void invsort$render(int int_1, int int_2, float float_1, CallbackInfo callbackinfo) {
        Screen currentScreen = MinecraftClient.getInstance().currentScreen;
        if (invsort$btn != null && currentScreen instanceof InventoryScreen) {
            invsort$btn.setPos(this.left + 125, this.height / 2 - 22);
        }
    }
}