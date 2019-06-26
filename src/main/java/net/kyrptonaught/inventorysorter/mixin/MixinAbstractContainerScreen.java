package net.kyrptonaught.inventorysorter.mixin;
import net.kyrptonaught.inventorysorter.InventorySorter;
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
        Screen currenctScreen = MinecraftClient.getInstance().currentScreen;
       if(!shouldInejct(currenctScreen)) return;
        this.addButton(new ButtonWidget((this.width - containerWidth) / 2 + containerWidth + 5, top, 35, 18, "Sort", var1 -> {
            if (currenctScreen instanceof InventoryScreen)
                InventorySorter.sendPacket(true);
            else InventorySorter.sendPacket(false);
        }));
    }
    private Boolean shouldInejct(Screen currentScreen){
        if (currentScreen instanceof CreativeInventoryScreen || currentScreen instanceof BeaconScreen || currentScreen instanceof AnvilScreen || currentScreen instanceof EnchantingScreen || currentScreen instanceof GrindstoneScreen || currentScreen instanceof AbstractFurnaceScreen || currentScreen instanceof LoomScreen  || currentScreen instanceof CraftingTableScreen  || currentScreen instanceof BrewingStandScreen  || currentScreen instanceof HorseScreen)
            return false;
        return true;
    }
    /*
    @Inject(method = "mouseClicked", at = @At("HEAD"), cancellable = true)
    public void onMouseClick(double x, double y, int button, CallbackInfoReturnable<Boolean> callbackInfoReturnable) {
        if (button == 2) {
            if (MinecraftClient.getInstance().currentScreen instanceof InventoryScreen)
                InventorySorter.sendPacket(true);
            else InventorySorter.sendPacket(false);
            callbackInfoReturnable.setReturnValue(true);
        }
    }
     */
}