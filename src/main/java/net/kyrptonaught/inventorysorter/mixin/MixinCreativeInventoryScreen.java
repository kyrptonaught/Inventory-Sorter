package net.kyrptonaught.inventorysorter.mixin;

import net.kyrptonaught.inventorysorter.*;
import net.minecraft.client.gui.screen.ingame.AbstractInventoryScreen;
import net.minecraft.client.gui.screen.ingame.CreativeInventoryScreen;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemGroup;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(CreativeInventoryScreen.class)
public abstract class MixinCreativeInventoryScreen extends AbstractInventoryScreen<CreativeInventoryScreen.CreativeContainer> {
    public MixinCreativeInventoryScreen(CreativeInventoryScreen.CreativeContainer container_1, PlayerInventory playerInventory_1, Text text_1) {
        super(container_1, playerInventory_1, text_1);
    }

    @Shadow
    private static int selectedTab;

    @Inject(method = "init", at = @At("TAIL"), cancellable = true)
    private void invsort$init(CallbackInfo callbackinfo) {
        SortButtonWidget sortbtn = ((SortableContainerScreen) this.minecraft.currentScreen).getSortButton();
        sortbtn.visible = selectedTab == ItemGroup.INVENTORY.getIndex();
    }

    @Inject(method = "render", at = @At("TAIL"))
    private void invsort$render(int int_1, int int_2, float float_1, CallbackInfo callbackinfo) {
        SortButtonWidget sortbtn = ((SortableContainerScreen) this.minecraft.currentScreen).getSortButton();
        sortbtn.visible = selectedTab == ItemGroup.INVENTORY.getIndex();
    }
}

