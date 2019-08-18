package net.kyrptonaught.inventorysorter.mixin;

import net.kyrptonaught.inventorysorter.SortButtonWidget;
import net.kyrptonaught.inventorysorter.SortableContainerScreen;
import net.minecraft.client.gui.screen.ingame.AbstractInventoryScreen;
import net.minecraft.client.gui.screen.ingame.InventoryScreen;
import net.minecraft.container.PlayerContainer;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(InventoryScreen.class)
public abstract class MixinInventoryScreen extends AbstractInventoryScreen<PlayerContainer> {

    public MixinInventoryScreen(PlayerContainer container_1, PlayerInventory playerInventory_1, Text text_1) {
        super(container_1, playerInventory_1, text_1);
    }

    @Inject(method = "init", at = @At("TAIL"), cancellable = true)
    private void invsort$init(CallbackInfo callbackinfo) {
        SortButtonWidget sortbtn = ((SortableContainerScreen) this.minecraft.currentScreen).getSortButton();
        sortbtn.x = this.left + this.containerWidth - 20;
    }

    @Inject(method = "render", at = @At("TAIL"))
    private void invsort$render(int int_1, int int_2, float float_1, CallbackInfo callbackinfo) {
        SortButtonWidget sortbtn = ((SortableContainerScreen) this.minecraft.currentScreen).getSortButton();
        sortbtn.x = this.left + this.containerWidth - 20;
    }
}
