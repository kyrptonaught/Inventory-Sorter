package net.kyrptonaught.inventorysorter.mixin;

import net.kyrptonaught.inventorysorter.InventorySorterMod;
import net.kyrptonaught.inventorysorter.client.SortButtonWidget;
import net.kyrptonaught.inventorysorter.client.SortableContainerScreen;
import net.minecraft.client.gui.screen.ingame.CreativeInventoryScreen;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.ItemGroup;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(CreativeInventoryScreen.class)
public abstract class MixinCreativeInventoryScreen implements SortableContainerScreen {

    @Shadow
    private static int selectedTab;

    @Inject(method = "init", at = @At("TAIL"), cancellable = true)
    private void invsort$init(CallbackInfo callbackinfo) {
        if (InventorySorterMod.getConfig().displaySort) {
            SortButtonWidget sortbtn = this.getSortButton();
            sortbtn.visible = selectedTab == ItemGroup.INVENTORY.getIndex();
        }
    }

    @Inject(method = "render", at = @At("TAIL"))
    private void invsort$render(MatrixStack matrixStack, int int_1, int int_2, float float_1, CallbackInfo callbackinfo) {
        if (InventorySorterMod.getConfig().displaySort) {
            SortButtonWidget sortbtn = this.getSortButton();
            sortbtn.visible = selectedTab == ItemGroup.INVENTORY.getIndex();
        }
    }
}

