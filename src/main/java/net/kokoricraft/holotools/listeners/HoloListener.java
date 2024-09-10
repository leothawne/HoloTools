package net.kokoricraft.holotools.listeners;

import net.kokoricraft.holotools.HoloTools;
import net.kokoricraft.holotools.enums.HoloActionType;
import net.kokoricraft.holotools.events.InventoryUpdateEvent;
import net.kokoricraft.holotools.objects.halo.Holo;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityPickupItemEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.*;

public class HoloListener implements Listener {
    private final HoloTools plugin;

    public HoloListener(HoloTools plugin){
        this.plugin = plugin;
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Bukkit.getScheduler().runTaskLater(plugin, () -> {
            plugin.getCompatManager().getCompat().initPacketsRegister(event.getPlayer());
            plugin.getHoloManager().update(event.getPlayer(), 1L);
        }, 3L);
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        plugin.getHoloManager().disable(event.getPlayer());
    }

    @EventHandler
    public void onItemHeldChange(PlayerItemHeldEvent event) {
        plugin.getHoloManager().update(event.getPlayer(), 1L);
    }

    @EventHandler
    public void onSwapHandItems(PlayerSwapHandItemsEvent event) {
        plugin.getHoloManager().update(event.getPlayer(), 1L);
    }

    @EventHandler
    public void onPlayerRespawn(PlayerRespawnEvent event) {
        plugin.getHoloManager().update(event.getPlayer(), 1L);
    }

    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent event) {
        plugin.getHoloManager().disable(event.getEntity());
    }

    @EventHandler
    public void onPlayerTeleport(PlayerTeleportEvent event) {
        if(event.getPlayer().getName().equalsIgnoreCase("FavioMC19"))
            plugin.getUtils().debug(String.format("TeleportEvent| cancelled: %s, player: %s, isAsync: %s", event.isCancelled(), event.getPlayer().getName(), event.isAsynchronous()));
        plugin.getHoloManager().update(event.getPlayer(), 1L);
    }

    @EventHandler
    public void onPlayerGameModeChange(PlayerGameModeChangeEvent event) {
        plugin.getHoloManager().update(event.getPlayer(), 1L);
    }

    @EventHandler
    public void onPlayerDropItem(PlayerDropItemEvent event){
        if(event.isCancelled()) return;
        plugin.getHoloManager().update(event.getPlayer(), 1L);
    }

    @EventHandler
    public void onPlayerPickupItem(EntityPickupItemEvent event){
        if(!(event.getEntity() instanceof Player player) || event.isCancelled()) return;
        plugin.getHoloManager().update(player, 1L);
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

    @EventHandler
    public void onPlaceBlock(BlockPlaceEvent event){
        if(plugin.getHoloManager().isHolo(event.getItemInHand())) event.setCancelled(true);
    }

    @EventHandler
    public void onInteractEntity(PlayerInteractEntityEvent event){
        if(plugin.getHoloManager().isHolo(event.getPlayer().getItemInUse())) event.setCancelled(true);
    }

    @EventHandler
    public void onInteractAtEntity(PlayerInteractAtEntityEvent event){
        plugin.getHoloManager().update(event.getPlayer(), 1L);
    }

    @EventHandler
    public void onInventoryUpdate(InventoryUpdateEvent event){
        plugin.getHoloManager().update(event.getPlayer(), 1L);
    }
    @EventHandler
    public void onPlayerSneak(PlayerToggleSneakEvent event){
        Holo holo = plugin.getHoloManager().getHolo(event.getPlayer());
        if(holo == null) return;
        holo.onAction(HoloActionType.SNEAK);
    }

    @EventHandler
    public void onPlayerJump(PlayerStatisticIncrementEvent event){
        Holo holo = plugin.getHoloManager().getHolo(event.getPlayer());
        if(holo == null || !event.getStatistic().name().equals("JUMP")) return;

        holo.onAction(HoloActionType.JUMP);
    }
}
