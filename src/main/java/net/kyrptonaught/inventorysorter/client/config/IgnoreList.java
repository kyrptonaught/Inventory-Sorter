package net.kyrptonaught.inventorysorter.client.config;


import blue.endless.jankson.Comment;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Sets;
import net.kyrptonaught.kyrptconfig.config.AbstractConfigFile;
import net.minecraft.screen.ScreenHandlerType;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

import java.util.HashSet;

public class IgnoreList implements AbstractConfigFile {
    @Comment("Inventories that should not be sorted")
    public HashSet<String> doNotSortList = new HashSet<>();

    @Comment("Inventories that should not display any sort buttons")
    public HashSet<String> hideSortBtnsList = new HashSet<>();

    public transient HashSet<Identifier> defaultHideSortBtnsList = Sets.newHashSet(
            new Identifier("guild:quest_screen")

    );

    public transient HashSet<Identifier> defaultDoNotSortList = Sets.newHashSet(
            Registry.SCREEN_HANDLER.getId(ScreenHandlerType.CRAFTING),
            new Identifier("adorn:trading_station"),
            new Identifier("guild:quest_screen")
    );

    public boolean isSortBlackListed(Identifier screenHandlerTypeID) {
        return isDisplayBlacklisted(screenHandlerTypeID) || doNotSortList.contains(screenHandlerTypeID.toString()) || defaultDoNotSortList.contains(screenHandlerTypeID);
    }

    public boolean isDisplayBlacklisted(Identifier screenHandlerTypeID) {
        return defaultHideSortBtnsList.contains(screenHandlerTypeID) || hideSortBtnsList.contains(screenHandlerTypeID.toString());
    }
    /*
    public transient ImmutableSet<String> hiddenList = ImmutableSet.of(
            "appeng.client.gui.implementations.CraftAmountScreen",
            "appeng.client.gui.implementations.PriorityScreen",
            "appeng.client.gui.implementations.InterfaceTerminalScreen",
            "dan200.computercraft.client.gui.GuiComputer"
    );

    public transient ImmutableSet<String> defaultBlacklist = ImmutableSet.of(
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

     */

}
