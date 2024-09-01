package net.kokoricraft.holotools.events;

import org.bukkit.event.inventory.PrepareItemCraftEvent;
import org.bukkit.inventory.CraftingInventory;
import org.bukkit.inventory.InventoryView;

public class HoloCraftItemEvent extends PrepareItemCraftEvent {
    public HoloCraftItemEvent(CraftingInventory what, InventoryView view, boolean isRepair) {
        super(what, view, isRepair);
    }
}
