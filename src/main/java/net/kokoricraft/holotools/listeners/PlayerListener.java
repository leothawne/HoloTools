package net.kokoricraft.holotools.listeners;

import net.kokoricraft.holotools.HoloTools;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class PlayerListener implements Listener {
    private final HoloTools plugin;

    public PlayerListener(HoloTools plugin){
        this.plugin = plugin;
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event){
        if(!event.getPlayer().isOp() && !event.getPlayer().hasPermission("*")) return;
        plugin.getUpdateChecker().sendMessage(event.getPlayer(), plugin.UPDATED, plugin.VERSION);
    }
}
