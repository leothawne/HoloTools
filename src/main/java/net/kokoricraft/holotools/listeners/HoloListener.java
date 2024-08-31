package net.kokoricraft.holotools.listeners;

import net.kokoricraft.holotools.HoloTools;
import net.kokoricraft.holotools.objects.halo.Holo;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityPickupItemEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.*;

import java.util.ArrayList;
import java.util.List;

public class HoloListener implements Listener {
    private final HoloTools plugin;

    public HoloListener(HoloTools plugin){
        this.plugin = plugin;
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        plugin.getHoloManager().check(event.getPlayer());
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        plugin.getHoloManager().disable(event.getPlayer());
    }

    @EventHandler
    public void onItemHeldChange(PlayerItemHeldEvent event) {
        Bukkit.getScheduler().runTaskLater(plugin, () -> plugin.getHoloManager().check(event.getPlayer()), 1L);
    }

    @EventHandler
    public void onSwapHandItems(PlayerSwapHandItemsEvent event) {
        Bukkit.getScheduler().runTaskLater(plugin, () -> plugin.getHoloManager().check(event.getPlayer()), 1L);
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        boolean currentIsHolo = plugin.getHoloManager().isHolo(event.getCurrentItem());
        boolean cursorIsHolo = plugin.getHoloManager().isHolo(event.getCursor());

        if(!currentIsHolo && !cursorIsHolo) return;

        if(event.getWhoClicked() instanceof Player player)
            Bukkit.getScheduler().runTaskLater(plugin, () -> plugin.getHoloManager().check(player), 1L);
    }

    @EventHandler
    public void onPlayerRespawn(PlayerRespawnEvent event) {
        Bukkit.getScheduler().runTaskLater(plugin, () -> plugin.getHoloManager().check(event.getPlayer()), 1L);
    }

    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent event) {
        plugin.getHoloManager().disable(event.getEntity());
    }

    @EventHandler
    public void onPlayerTeleport(PlayerTeleportEvent event) {
        Bukkit.getScheduler().runTaskLater(plugin, () -> plugin.getHoloManager().check(event.getPlayer()), 1L);
    }

    @EventHandler
    public void onPlayerGameModeChange(PlayerGameModeChangeEvent event) {
        Bukkit.getScheduler().runTaskLater(plugin, () -> plugin.getHoloManager().check(event.getPlayer()), 1L);
    }

    @EventHandler
    public void onPlayerDropItem(PlayerDropItemEvent event){
        Player player = event.getPlayer();

        if(event.isCancelled()) return;

        Bukkit.getScheduler().runTaskLater(plugin, () -> plugin.getHoloManager().check(player), 1L);
    }

    @EventHandler
    public void onPlayerPickupItem(EntityPickupItemEvent event){
        if(!(event.getEntity() instanceof Player player) || event.isCancelled()) return;
        Bukkit.getScheduler().runTaskLater(plugin, () -> plugin.getHoloManager().check(player), 2L);
    }

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event){
        Player player = event.getPlayer();
        if(!plugin.getHoloManager().isHolo(event.getItem())) return;
        Holo holo = plugin.getHoloManager().getHolo(player);

        if(holo == null || !holo.isVisible()) return;

        float pitch = player.getLocation().getPitch();

        if(pitch <= 38 && pitch > -15){
            event.setCancelled(true);
            event.setUseInteractedBlock(Event.Result.DENY);
            event.setUseItemInHand(Event.Result.DENY);
            holo.onClick();
        }
    }
}
