package net.kyrptonaught.inventorysorter.interfaces;

import net.kyrptonaught.inventorysorter.SortCases;

public interface InvSorterPlayer {
    SortCases.SortType getSortType();

    void setSortType(SortCases.SortType sortType);

    boolean getMiddleClick();

    void setMiddleClick(boolean middleClick);

    boolean getDoubleClickSort();

    void setDoubleClickSort(boolean doubleClick);
}
