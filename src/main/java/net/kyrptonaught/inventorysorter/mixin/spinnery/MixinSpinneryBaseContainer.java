package net.kyrptonaught.inventorysorter.mixin.spinnery;

import net.kyrptonaught.inventorysorter.SortableContainer;
import net.minecraft.inventory.Inventory;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import spinnery.common.BaseContainer;

@Mixin(BaseContainer.class)
public abstract class MixinSpinneryBaseContainer implements SortableContainer {
    @Shadow public abstract Inventory getInventory(int inventoryNumber);

    @Override
    public Inventory getInventory() {
        return this.getInventory(1);
    }
}
