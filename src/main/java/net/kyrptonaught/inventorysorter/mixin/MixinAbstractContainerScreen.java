package net.kyrptonaught.inventorysorter.mixin;

import net.kyrptonaught.inventorysorter.InventoryHelper;
import net.kyrptonaught.inventorysorter.InventorySortPacket;
import net.kyrptonaught.inventorysorter.InventorySorterMod;
import net.kyrptonaught.inventorysorter.client.SortButtonWidget;
import net.kyrptonaught.inventorysorter.client.SortableContainerScreen;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.ingame.AbstractContainerScreen;
import net.minecraft.container.Container;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;


@Mixin(AbstractContainerScreen.class)
public abstract class MixinAbstractContainerScreen extends Screen implements SortableContainerScreen {
    @Shadow
    protected int containerWidth;
    @Shadow
    protected int containerHeight;
    @Shadow
    protected int top;
    @Shadow
    protected int left;

    @Shadow
    @Final
    protected Container container;

    private SortButtonWidget invsort$SortBtn;

    protected MixinAbstractContainerScreen(Text text_1) {
        super(text_1);
    }

    @Inject(method = "init", at = @At("TAIL"), cancellable = true)
    private void invsort$init(CallbackInfo callbackinfo) {
        if (InventorySorterMod.getConfig().displaySort) {
            boolean playerOnly = InventoryHelper.isPlayerOnlyInventory(this);
            this.addButton(invsort$SortBtn = new SortButtonWidget(this.left + this.containerWidth - 20, top + (playerOnly ? (containerHeight - 95) : 6), playerOnly));
            if (!playerOnly && InventorySorterMod.getConfig().seperateBtn)
                this.addButton(new SortButtonWidget(invsort$SortBtn.x, this.top + this.container.getSlot(this.container.slotList.size() - 36).yPosition - 12, true));
        }
    }

    @Inject(method = "mouseClicked", at = @At("HEAD"))
    private void invsort$mouseClicked(double x, double y, int button, CallbackInfoReturnable callbackInfoReturnable) {
        if (InventorySorterMod.getConfig().middleClick && button == 2) {
            InventorySortPacket.sendSortPacket(InventoryHelper.isPlayerOnlyInventory(this));
        }
    }

    @Inject(method = "keyPressed", at = @At("HEAD"))
    private void invsort$keyPressed(int keycode, int scancode, int modifiers, CallbackInfoReturnable callbackInfoReturnable) {
        if (InventorySorterMod.keyBinding.matchesKey(keycode, scancode)) {
            InventorySortPacket.sendSortPacket(InventoryHelper.isPlayerOnlyInventory(this));
        }
    }

    @Inject(method = "render", at = @At("TAIL"))
    private void invsort$render(int int_1, int int_2, float float_1, CallbackInfo callbackinfo) {
        if (invsort$SortBtn != null)
            invsort$SortBtn.x = this.left + this.containerWidth - 20;
    }

    @Override
    public SortButtonWidget getSortButton() {
        return invsort$SortBtn;
    }
}