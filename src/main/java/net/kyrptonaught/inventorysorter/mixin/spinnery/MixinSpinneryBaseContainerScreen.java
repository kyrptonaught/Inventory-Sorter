package net.kyrptonaught.inventorysorter.mixin.spinnery;

import net.kyrptonaught.inventorysorter.client.SortButtonWidget;
import net.kyrptonaught.inventorysorter.client.SortableContainerScreen;
import net.minecraft.client.gui.Element;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.ingame.ContainerScreen;
import net.minecraft.container.Container;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import spinnery.common.BaseContainer;
import spinnery.common.BaseContainerScreen;
import spinnery.widget.WAbstractWidget;
import spinnery.widget.WInterface;
import spinnery.widget.WStaticText;

@Mixin(BaseContainerScreen.class)
public abstract class MixinSpinneryBaseContainerScreen extends ContainerScreen implements SortableContainerScreen {

    @Shadow
    @Final
    protected WInterface clientInterface;

    public MixinSpinneryBaseContainerScreen(Container container, PlayerInventory playerInventory, Text name) {
        super(container, playerInventory, name);
    }

    @Inject(method = "render", at = @At("TAIL"))
    public void invsort$render(int mouseX, int mouseY, float tickDelta, CallbackInfo ci) {
        for (Element child : ((ContainerScreen) (Object) this).children()) {
            if (child instanceof SortButtonWidget)
                ((SortButtonWidget) child).render(mouseX, mouseY, tickDelta);
        }

    }

    @Inject(method = "mouseClicked", at = @At("TAIL"))
    public void mouseClicked(double mouseX, double mouseY, int mouseButton, CallbackInfoReturnable<Boolean> cir) {
        super.mouseClicked(mouseX, mouseY, mouseButton);
    }

    @Inject(method = "keyPressed", at = @At("HEAD"), cancellable = true)
    private void keyPressed(int keycode, int scancode, int modifiers, CallbackInfoReturnable<Boolean> callbackInfoReturnable) {
        super.keyPressed(keycode, scancode, modifiers);
    }

    @Override
    public int getMiddleHeight() {
        for (WAbstractWidget widget : this.clientInterface.getAllWidgets())
            if (widget instanceof WStaticText)
                if (!((WStaticText) widget).getText().asString().equals("Inventory"))
                    this.getSortButton().y = widget.getY();
        for (WAbstractWidget widget : this.clientInterface.getAllWidgets())
            if (widget instanceof WStaticText)
                if (((WStaticText) widget).getText().asString().equals("Inventory"))
                    return widget.getY() - 37;
        return 0;
    }
}
