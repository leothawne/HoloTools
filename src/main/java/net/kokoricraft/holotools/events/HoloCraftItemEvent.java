package net.kokoricraft.holotools.events;

import org.bukkit.event.inventory.PrepareItemCraftEvent;
import org.bukkit.inventory.CraftingInventory;
import org.bukkit.inventory.InventoryView;
import org.jetbrains.annotations.NotNull;

public class HoloCraftItemEvent extends PrepareItemCraftEvent {
    public HoloCraftItemEvent(@NotNull CraftingInventory what, @NotNull InventoryView view, boolean isRepair) {
        super(what, view, isRepair);
    }
}
