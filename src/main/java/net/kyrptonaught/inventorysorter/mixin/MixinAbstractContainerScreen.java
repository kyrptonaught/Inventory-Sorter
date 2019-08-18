package net.kyrptonaught.inventorysorter.mixin;

import net.kyrptonaught.inventorysorter.*;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.ingame.AbstractContainerScreen;
import net.minecraft.client.gui.screen.ingame.CreativeInventoryScreen;
import net.minecraft.client.gui.screen.ingame.InventoryScreen;
import net.minecraft.container.Container;
import net.minecraft.container.Slot;
import net.minecraft.container.SlotActionType;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;
import net.minecraft.util.Pair;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;


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

    private SortButtonWidget invsort$SortBtn;

    protected MixinAbstractContainerScreen(Text text_1) {
        super(text_1);
    }

    @Inject(method = "init", at = @At("TAIL"), cancellable = true)
    private void invsort$init(CallbackInfo callbackinfo) {
        if (InventorySorterMod.getConfig().displaySort) {
            this.addButton(invsort$SortBtn = new SortButtonWidget(this.left + this.containerWidth - 20, top + 6, press -> InventorySortPacket.sendSortPacket(this)));
            if (InventorySorterMod.getConfig().seperateBtn && !InventoryHelper.isPlayerOnlyInventory(this))
                this.addButton(new SortButtonWidget(invsort$SortBtn.x, top + (containerHeight - 95), press -> InventorySortPacket.sendSortPacket(true)));
        }
    }

    @Inject(method = "mouseClicked", at = @At("HEAD"))
    private void invsort$mouseClicked(double x, double y, int button, CallbackInfoReturnable callbackInfoReturnable) {
        if (InventorySorterMod.getConfig().middleClick && button == 2) {
            InventorySortPacket.sendSortPacket(this);
        }
    }

    @Inject(method = "keyPressed", at = @At("HEAD"))
    private void invsort$keyPressed(int keycode, int scancode, int modifiers, CallbackInfoReturnable callbackInfoReturnable) {
        if (InventorySorterMod.keyBinding.matchesKey(keycode, scancode)) {
            InventorySortPacket.sendSortPacket(this);
        }
    }

    @Override
    public SortButtonWidget getSortButton() {
        return invsort$SortBtn;
    }
}