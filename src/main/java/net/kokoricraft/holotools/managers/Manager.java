package net.kokoricraft.holotools.managers;

import net.kokoricraft.holotools.HoloTools;
import org.bukkit.entity.Player;

import java.util.*;

public class Manager {
    private final HoloTools plugin;

    public Manager(HoloTools plugin){
        this.plugin = plugin;
    }

    public List<Player> getHoloPlayerView(Player player){
        if(!plugin.getConfigManager().HOLO_VISIBLE_FOR_EVERYONE)
            return List.of(player);

        return plugin.getUtils().getNearbyPlayers(player.getLocation(), 250);
    }
}
