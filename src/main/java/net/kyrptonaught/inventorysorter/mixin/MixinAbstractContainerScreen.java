package net.kyrptonaught.inventorysorter.mixin;

import net.kyrptonaught.inventorysorter.InventoryHelper;
import net.kyrptonaught.inventorysorter.InventorySortPacket;
import net.kyrptonaught.inventorysorter.InventorySorterMod;
import net.kyrptonaught.inventorysorter.SortButtonWidget;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.ingame.AbstractContainerScreen;
import net.minecraft.client.gui.screen.ingame.InventoryScreen;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;


@Mixin(AbstractContainerScreen.class)
public abstract class MixinAbstractContainerScreen extends Screen {
    @Shadow
    protected int containerWidth;
    @Shadow
    protected int containerHeight;
    @Shadow
    protected int top;
    @Shadow
    protected int left;

    private SortButtonWidget invsort$SortBtn;

    protected MixinAbstractContainerScreen(Text text_1) {
        super(text_1);
    }

    @Inject(method = "init", at = @At("TAIL"), cancellable = true)
    private void invsort$init(CallbackInfo callbackinfo) {
        if (!InventoryHelper.isSortableInventory(this)) return;
        if (InventorySorterMod.getConfig().displaySort) {
            this.addButton(invsort$SortBtn = new SortButtonWidget(this.left + this.containerWidth - 20, top + 6, press -> InventorySortPacket.sendSortPacket(this)));
            if (InventorySorterMod.getConfig().seperateBtn && !(super.minecraft.currentScreen instanceof InventoryScreen))
                this.addButton(new SortButtonWidget(invsort$SortBtn.x, containerHeight - 86, press -> InventorySortPacket.sendSortPacket(true)));
        }
    }

    @Inject(method = "mouseClicked", at = @At("HEAD"))
    private void invsort$mouseClicked(double x, double y, int button, CallbackInfoReturnable callbackInfoReturnable) {
        if (InventorySorterMod.getConfig().middleClick && button == 2) {
            if (InventoryHelper.isSortableInventory(this)) {
                InventorySortPacket.sendSortPacket(this);
            }
        }
    }

    @Inject(method = "keyPressed", at = @At("HEAD"))
    private void invsort$keyPressed(int keycode, int scancode, int modifiers, CallbackInfoReturnable callbackInfoReturnable) {
        if (InventorySorterMod.keyBinding.matchesKey(keycode, scancode)) {
            InventorySortPacket.sendSortPacket(this);
        }
    }

    @Inject(method = "render", at = @At("TAIL"))
    private void invsort$render(int int_1, int int_2, float float_1, CallbackInfo callbackinfo) {
        if (invsort$SortBtn != null) {
            invsort$SortBtn.x = this.left + this.containerWidth - 20;
        }
    }
}