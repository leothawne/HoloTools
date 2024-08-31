package net.kokoricraft.holotools.listeners;

import net.kokoricraft.holotools.HoloTools;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.player.PlayerQuitEvent;


public class PlayerListener implements Listener {
    private final HoloTools plugin;

    public PlayerListener(HoloTools plugin){
        this.plugin = plugin;
    }

    @EventHandler
    public void onInventoryClose(InventoryCloseEvent event){
        Player player = (Player) event.getPlayer();

        if(plugin.getManager().inHaloCraftingInventory(player))
            plugin.getManager().closeHaloCrafting(player);
    }

    @EventHandler
    public void onLeave(PlayerQuitEvent event){
        Player player = event.getPlayer();

        if(plugin.getManager().inHaloCraftingInventory(player))
            plugin.getManager().closeHaloCrafting(player);
    }


}
