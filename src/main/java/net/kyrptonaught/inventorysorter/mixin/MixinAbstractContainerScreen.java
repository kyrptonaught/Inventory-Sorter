package net.kyrptonaught.inventorysorter.mixin;

import com.google.common.collect.ImmutableSet;
import net.kyrptonaught.inventorysorter.InventorySortPacket;
import net.kyrptonaught.inventorysorter.InventorySorterMod;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.ingame.*;
import net.minecraft.client.gui.widget.TexturedButtonWidget;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.HashSet;


@Mixin(AbstractContainerScreen.class)
public abstract class MixinAbstractContainerScreen extends Screen {
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

    protected MixinAbstractContainerScreen(Text component) {
        super(component);
    }

    @Inject(method = "init", at = @At("TAIL"), cancellable = true)
    protected void invsort$init(CallbackInfo callbackinfo) {
        if (InventorySorterMod.config.config.display_sort) {
            Screen currentScreen = MinecraftClient.getInstance().currentScreen;
            if (!invsort$shouldInject(currentScreen)) return;
            int calcOffset = this.left + this.containerWidth;
            if (InventorySorterMod.config.config.left_display)
                calcOffset = this.left - 20;
            this.addButton(invsort$btn = new TexturedButtonWidget(calcOffset, top, 20, 18, 0, 0, 19, invsort$BUTTON_TEX, var1 -> InventorySortPacket.sendSortPacket(currentScreen)));
            if (InventorySorterMod.config.config.seperate_btn && !(currentScreen instanceof InventoryScreen))
                this.addButton(new TexturedButtonWidget(calcOffset, containerHeight - 85, 20, 18, 0, 0, 19, invsort$BUTTON_TEX, var1 -> InventorySortPacket.sendSortPacket(true)));
        }
    }

    @Inject(method = "mouseClicked", at = @At("HEAD"), cancellable = true)
    public void invsort$mouseClicked(double x, double y, int button, CallbackInfoReturnable<Boolean> callbackInfoReturnable) {
        if (InventorySorterMod.config.config.middle_click && button == 2) {
            Screen currentScreen = MinecraftClient.getInstance().currentScreen;
            if (invsort$shouldInject(currentScreen)) {
                InventorySortPacket.sendSortPacket(currentScreen);
                callbackInfoReturnable.setReturnValue(true);
            }
        }
    }

    @Inject(method = "keyPressed", at = @At("HEAD"), cancellable = true)
    public void invsort$keyPressed(int keycode, int scancode, int modifiers, CallbackInfoReturnable<Boolean> callbackInfoReturnable) {
        if (InventorySorterMod.keyBinding.matchesKey(keycode, scancode)) {
            Screen currentScreen = MinecraftClient.getInstance().currentScreen;
            if (currentScreen instanceof AbstractContainerScreen && invsort$shouldInject(currentScreen)) {
                InventorySortPacket.sendSortPacket(currentScreen);
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

    private HashSet<String> invsort$invalidScreens = new HashSet<>(ImmutableSet.of(CreativeInventoryScreen.class.getName(),
            BeaconScreen.class.getName(), AnvilScreen.class.getName(), EnchantingScreen.class.getName(),
            GrindstoneScreen.class.getName(), AbstractContainerScreen.class.getName(), LoomScreen.class.getName(),
            CraftingTableScreen.class.getName(), BrewingStandScreen.class.getName(), HorseScreen.class.getName()));

    private Boolean invsort$shouldInject(Screen currentScreen) {
        return !invsort$invalidScreens.contains(currentScreen.getClass().getName());
    }
}