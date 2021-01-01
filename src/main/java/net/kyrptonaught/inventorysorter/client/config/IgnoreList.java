package net.kyrptonaught.inventorysorter.client.config;

import blue.endless.jankson.Comment;
import com.google.common.collect.ImmutableSet;
import net.kyrptonaught.kyrptconfig.config.AbstractConfigFile;
import net.minecraft.client.gui.screen.ingame.*;

import java.util.HashSet;

public class IgnoreList implements AbstractConfigFile {
    @Comment("Inventories that should not be sorted")
    public HashSet<String> blacklistedInventories = new HashSet<>();

    public transient ImmutableSet<String> hiddenList = ImmutableSet.of(
            "appeng.client.gui.implementations.CraftAmountScreen",
            "appeng.client.gui.implementations.PriorityScreen",
            "appeng.client.gui.implementations.InterfaceTerminalScreen",
            "dan200.computercraft.client.gui.GuiComputer"
    );

    public transient ImmutableSet<String> defaultBlacklist = ImmutableSet.of(
            BeaconScreen.class.getName(), AnvilScreen.class.getName(), EnchantmentScreen.class.getName(),
            GrindstoneScreen.class.getName(), AbstractFurnaceScreen.class.getName(), LoomScreen.class.getName(),
            CraftingScreen.class.getName(), BrewingStandScreen.class.getName(), MerchantScreen.class.getName(),
            InventoryScreen.class.getName(), CreativeInventoryScreen.class.getName(), HorseScreen.class.getName(),
            StonecutterScreen.class.getName(), SmokerScreen.class.getName(), BlastFurnaceScreen.class.getName(),
            FurnaceScreen.class.getName(),
            "techreborn.client.gui.GuiIndustrialSawmill",
            "techreborn.client.gui.GuiIndustrialGrinder",
            "techreborn.client.gui.GuiBlastFurnace",
            "techreborn.client.gui.GuiGreenhouseController",
            "techreborn.client.gui.GuiMatterFabricator",
            "techreborn.client.gui.GuiIndustrialElectrolyzer",
            "techreborn.client.gui.GuiDistillationTower",
            "techreborn.client.gui.GuiAutoCrafting",
            "techreborn.client.gui.GuiRollingMachine",
            "techreborn.client.gui.GuiCentrifuge",
            "appeng.client.gui.implementations.InterfaceTerminalScreen",
            "appeng.client.gui.implementations.WirelessTermScreen",
            "appeng.client.gui.implementations.MEMonitorableScreen",
            "appeng.client.gui.implementations.CraftingTermScreen",
            "appeng.client.gui.implementations.PatternTermScreen",
            "appeng.client.gui.implementations.DriveScreen",
            "appeng.client.gui.implementations.InterfaceScreen",
            "appeng.client.gui.implementations.SecurityStationScreen",
            "appeng.client.gui.implementations.IOPortScreen",
            "appeng.client.gui.implementations.MolecularAssemblerScreen",
            "appeng.client.gui.implementations.MEPortableCellScreen",
            "badasintended.slotlink.client.gui.screen.RequestScreen",
            "dan200.computercraft.client.gui.GuiTurtle",
            "com.kqp.ezpas.client.screen.FilteredPipeScreen",
            "com.github.briansemrau.cosmeticarmorslots.client.gui.screen.ingame.CosmeticArmorInventoryScreen",
            "me.marnic.extrabows.client.gui.BowUpgradeGui");

}
