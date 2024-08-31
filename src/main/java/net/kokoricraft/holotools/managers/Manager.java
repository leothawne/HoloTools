package net.kokoricraft.holotools.managers;

import net.kokoricraft.holotools.HoloTools;
import org.bukkit.entity.Player;

import java.util.*;

public class Manager {
    private final HoloTools plugin;
    private final List<Player> usingHaloCrafting = new ArrayList<>();
    private int entity_index = 999999;

    public Manager(HoloTools plugin){
        this.plugin = plugin;
    }

    public void openHaloCrafting(Player player){
        if(!usingHaloCrafting.contains(player))
            usingHaloCrafting.add(player);


        player.openWorkbench(null, true);
    }

    public void closeHaloCrafting(Player player){
        usingHaloCrafting.remove(player);
        player.closeInventory();
    }

    public boolean inHaloCraftingInventory(Player player){
        return usingHaloCrafting.contains(player);
    }


    public  int getEntityIndex(){
        return entity_index++;
    }
}
