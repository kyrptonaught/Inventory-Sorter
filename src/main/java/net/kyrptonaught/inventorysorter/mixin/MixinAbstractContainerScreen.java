package net.kyrptonaught.inventorysorter.mixin;

import net.kyrptonaught.inventorysorter.InventorySorter;
import net.kyrptonaught.inventorysorter.InventorySorterMod;
import net.kyrptonaught.inventorysorter.config.ConfigHelper;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.ingame.*;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.network.chat.Component;
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

    @Inject(method = "init", at = @At("TAIL"), cancellable = true)
    protected void init(CallbackInfo callbackinfo) {
        if (InventorySorterMod.config.getConfigOption(ConfigHelper.Option.display_sort).value) {
            Screen currentScreen = MinecraftClient.getInstance().currentScreen;
            if (!shouldInject(currentScreen)) return;
            int offset = this.containerWidth / 2 + 5;
            if (InventorySorterMod.config.getConfigOption(ConfigHelper.Option.left_display).value)
                offset = -(this.containerWidth / 2 + 40);
            int finalOffset = offset;
            this.addButton(new ButtonWidget((this.width / 2) + finalOffset, top, 35, 18, "Sort", var1 -> sendPacketToClient(currentScreen)));
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