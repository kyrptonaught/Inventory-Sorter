package net.kyrptonaught.inventorysorter.mixin;

import net.kyrptonaught.inventorysorter.InventoryHelper;
import net.kyrptonaught.inventorysorter.InventorySortPacket;
import net.kyrptonaught.inventorysorter.InventorySorterMod;
import net.kyrptonaught.inventorysorter.client.SortButtonWidget;
import net.kyrptonaught.inventorysorter.client.SortableContainerScreen;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;


@Mixin(HandledScreen.class)
public abstract class MixinHandledScreen extends Screen implements SortableContainerScreen {
    @Shadow
    protected int backgroundWidth;
    @Shadow
    protected int backgroundHeight;

    @Shadow
    @Final
    protected ScreenHandler handler;

    @Shadow
    protected int x;
    @Shadow
    protected int y;
    private SortButtonWidget invsort$SortBtn;

    protected MixinHandledScreen(Text text_1) {
        super(text_1);
    }

    @Inject(method = "init", at = @At("TAIL"))
    private void invsort$init(CallbackInfo callbackinfo) {
        if (InventorySorterMod.getConfig().displaySort) {
            boolean playerOnly = InventoryHelper.isPlayerOnlyInventory(this);
            this.addButton(invsort$SortBtn = new SortButtonWidget(this.x + this.backgroundWidth - 20, this.y + (playerOnly ? (backgroundHeight - 95) : 6), playerOnly));
            if (!playerOnly && InventorySorterMod.getConfig().seperateBtn)
                this.addButton(new SortButtonWidget(invsort$SortBtn.x, this.y + this.handler.getSlot(this.handler.slots.size() - 36).y - 12, true));
        }
    }

    @Inject(method = "mouseClicked", at = @At("HEAD"), cancellable = true)
    private void invsort$mouseClicked(double x, double y, int button, CallbackInfoReturnable<Boolean> callbackInfoReturnable) {
        if (InventorySorterMod.getConfig().middleClick && button == 2) {
            InventorySortPacket.sendSortPacket(InventoryHelper.isPlayerOnlyInventory(this));
            callbackInfoReturnable.setReturnValue(true);
        }
    }

    @Inject(method = "keyPressed", at = @At("HEAD"), cancellable = true)
    private void invsort$keyPressed(int keycode, int scancode, int modifiers, CallbackInfoReturnable<Boolean> callbackInfoReturnable) {
        if (InventorySorterMod.keyBinding.matchesKey(keycode, scancode)) {
            InventorySortPacket.sendSortPacket(InventoryHelper.isPlayerOnlyInventory(this));
            callbackInfoReturnable.setReturnValue(true);
        }
    }

    @Inject(method = "render", at = @At("TAIL"))
    private void invsort$render(int int_1, int int_2, float float_1, CallbackInfo callbackinfo) {
        if (invsort$SortBtn != null)
            invsort$SortBtn.x = this.x + this.backgroundWidth - 20;
    }

    @Override
    public SortButtonWidget getSortButton() {
        return invsort$SortBtn;
    }
}
