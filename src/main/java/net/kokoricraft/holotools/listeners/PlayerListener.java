package net.kokoricraft.holotools.listeners;

import net.kokoricraft.holotools.HoloTools;
import net.kokoricraft.holotools.objects.halocrafter.HaloCrafter;
import net.kokoricraft.holotools.objects.halocrafter.ItemCraft;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.CraftItemEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.Map;

public class PlayerListener implements Listener {
    private final HoloTools plugin;

    public PlayerListener(HoloTools plugin){
        this.plugin = plugin;
    }

    @EventHandler
    public void onPlayerCraft(CraftItemEvent event){
        Player player = (Player) event.getWhoClicked();
        if(!plugin.getManager().inHaloCraftingInventory(player)) return;
        HaloCrafter haloCrafter = plugin.getManager().getHaloCrafter(player);
        Map<Integer, ItemStack> ingredients = new HashMap<>();

        for(int i = 0; i < event.getInventory().getContents().length; i++){
            if(i == 0) continue;
            ingredients.put(i, event.getInventory().getContents()[i].clone());
        }

        ItemCraft itemCraft = new ItemCraft(ingredients, event.getRecipe().getResult().clone());
        haloCrafter.setLastedCraft(itemCraft);
        Bukkit.broadcastMessage("se lasted");

        Bukkit.getScheduler().runTaskLater(plugin, haloCrafter::forceUpdate, 1);
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

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event){
        Player player = event.getPlayer();
        HaloCrafter haloCrafter = plugin.getManager().getHaloCrafter(player);
        if(haloCrafter != null && haloCrafter.isVisible()){
            float pitch = player.getLocation().getPitch();
            if(pitch <= 38 && pitch > -15){
                event.setCancelled(true);
                event.setUseInteractedBlock(Event.Result.DENY);
                event.setUseItemInHand(Event.Result.DENY);
                haloCrafter.onClick();
            }
        }
    }
}
