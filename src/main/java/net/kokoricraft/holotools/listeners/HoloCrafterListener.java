package net.kokoricraft.holotools.listeners;

import net.kokoricraft.holotools.HoloTools;
import net.kokoricraft.holotools.objects.halo.Holo;
import net.kokoricraft.holotools.objects.holocrafter.HoloCrafter;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.CraftItemEvent;


public class HoloCrafterListener implements Listener {
    private HoloTools plugin;

    public HoloCrafterListener(HoloTools plugin){
        this.plugin = plugin;
    }

    @EventHandler
    public void onPlayerCraftItem(CraftItemEvent event){
        Player player = (Player) event.getWhoClicked();
        Holo holo = plugin.getHoloManager().getHolo(player);

        if((!(holo instanceof HoloCrafter holoCrafter))) return;

        holoCrafter.setLasted(event.getRecipe());
    }
}
