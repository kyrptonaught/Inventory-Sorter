package net.kyrptonaught.inventorysorter.mixin;

import net.kyrptonaught.inventorysorter.SortableContainer;
import net.minecraft.inventory.Inventory;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.slot.Slot;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import java.util.List;

@Mixin(ScreenHandler.class)
public class MixinContainer implements SortableContainer {
    @Shadow
    @Final
    public List<Slot> slots;

    @Override
    public Inventory getInventory() {
        return this.slots.get(0).inventory;
    }
}
