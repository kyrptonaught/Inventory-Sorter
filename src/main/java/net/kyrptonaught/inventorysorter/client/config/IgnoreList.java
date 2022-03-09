package net.kyrptonaught.inventorysorter.client.config;


import blue.endless.jankson.Comment;
import blue.endless.jankson.Jankson;
import com.google.common.collect.Sets;
import net.kyrptonaught.kyrptconfig.config.AbstractConfigFile;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.toast.SystemToast;
import net.minecraft.client.toast.Toast;
import net.minecraft.screen.ScreenHandlerType;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import org.apache.commons.io.IOUtils;

import java.net.URL;
import java.util.HashSet;

public class IgnoreList implements AbstractConfigFile {
    public static final String DOWNLOAD_URL = "https://raw.githubusercontent.com/kyrptonaught/Inventory-Sorter/1.18/DownloadableBlacklist.json5";

    @Comment("URL for blacklist to be downloaded from")
    public String blacklistDownloadURL = DOWNLOAD_URL;

    public void downloadList() {
        try {
            URL url = new URL(blacklistDownloadURL);
            String downloaded =  IOUtils.toString(url.openStream());
            IgnoreList newList = Jankson.builder().build().fromJson(downloaded, IgnoreList.class);

            doNotSortList.addAll(newList.doNotSortList);
            hideSortBtnsList.addAll(newList.hideSortBtnsList);
            SystemToast.add(MinecraftClient.getInstance().getToastManager(), SystemToast.Type.TUTORIAL_HINT, new TranslatableText("modmenu.dropSuccessful.line1"), new TranslatableText("modmenu.dropSuccessful.line2"));
        } catch (Exception e) {
            SystemToast.add(MinecraftClient.getInstance().getToastManager(), SystemToast.Type.TUTORIAL_HINT, new TranslatableText("modmenu.dropSuccessful.line1"), new TranslatableText("modmenu.dropSuccessful.line2"));
            System.out.println(e);
        }
    }

    @Comment("The opened inventory's sort button will not be visible, only the player's when this inventory is opened")
    public HashSet<String> doNotSortList = new HashSet<>();

    @Comment("Neither the opened inventory, nor your player inventory will be sortable when this inventory is opened")
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

}
