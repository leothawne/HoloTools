package net.kokoricraft.holotools.listeners;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import net.kokoricraft.holotools.HoloTools;

public class PlayerListener implements Listener {
    private final HoloTools plugin;

    public PlayerListener(HoloTools plugin){
        this.plugin = plugin;
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event){
        if(!event.getPlayer().isOp() && !event.getPlayer().hasPermission("*")) return;
        plugin.getUpdateChecker().sendMessage(event.getPlayer(), plugin.UPDATED, plugin.BUILD);
    }

    @EventHandler
    public void onPlayerPreJoin(AsyncPlayerPreLoginEvent event){
        plugin.getPlayerManager().getPlayer(event.getUniqueId());
    }

    @EventHandler
    public void onPlayerLeave(PlayerQuitEvent event){
        plugin.getPlayerManager().remove(event.getPlayer().getUniqueId());
    }
}
