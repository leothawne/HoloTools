package net.kokoricraft.holotools.managers;

import net.kokoricraft.holotools.HoloTools;
import net.kokoricraft.holotools.objects.halocrafter.HaloCrafter;
import org.bukkit.entity.Player;

import java.util.*;

public class Manager {
    private final HoloTools plugin;
    private final List<Player> usingHaloCrafting = new ArrayList<>();
    private final Map<Player, HaloCrafter> haloCrafterMap = new HashMap<>();

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

    public HaloCrafter getHaloCrafter(Player player){
        return haloCrafterMap.get(player);
    }

    public void setHaloCrafter(Player player, HaloCrafter haloCrafter){
        this.haloCrafterMap.put(player, haloCrafter);
    }

    public Collection<HaloCrafter> getAllHaloCrafter(){
        return haloCrafterMap.values();
    }
}
