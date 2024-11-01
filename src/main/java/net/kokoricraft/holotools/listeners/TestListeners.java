package net.kokoricraft.holotools.listeners;

import net.kokoricraft.holotools.HoloTools;
import net.kokoricraft.holotools.enums.GlowColor;
import net.kokoricraft.holotools.objects.ItemGlow;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Item;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityPickupItemEvent;
import org.bukkit.event.entity.EntitySpawnEvent;
import org.bukkit.event.entity.ItemDespawnEvent;
import org.bukkit.event.entity.ItemMergeEvent;

import java.util.*;

public class TestListeners implements Listener {
    private final HoloTools plugin;
    private final Map<Entity, ItemGlow> glows = new HashMap<>();
    
    public TestListeners(HoloTools plugin){
        this.plugin = plugin;
    }
    
    @EventHandler
    public void onEntitySpawn(EntitySpawnEvent event){
        if(!(event.getEntity() instanceof Item item)) return;


        if(item.getItemStack().getType().equals(Material.ENCHANTED_GOLDEN_APPLE)){
            Bukkit.getScheduler().runTaskLater(plugin, () -> {
                glows.put(item, new ItemGlow(item, 249, 83, 221));
                GlowColor.LIGHT_PURPLE.setGlow(item);
            }, 1L);
            return;
        }

        Random random = new Random();

        int red = random.nextInt(255);
        int green = random.nextInt(255);
        int blue = random.nextInt(255);

        Bukkit.getScheduler().runTaskLater(plugin, () -> glows.put(item, new ItemGlow(item, red, green, blue)), 1L);
    }

    @EventHandler
    public void onItemPickup(EntityPickupItemEvent event){
        if(glows.containsKey(event.getItem()))
            glows.get(event.getItem()).remove();
    }

    @EventHandler
    public void onItemMerge(ItemMergeEvent event){
        boolean firstHas = glows.containsKey(event.getEntity());
        boolean secondHas = glows.containsKey(event.getTarget());

        if(firstHas && secondHas){
            glows.get(event.getEntity()).remove();
            glows.remove(event.getEntity());
        }else if(firstHas){
            ItemGlow glow = glows.get(event.getEntity());
            int red = glow.getRed();
            int green = glow.getGreen();
            int blue = glow.getBlue();
            glow.remove();
            glows.remove(event.getEntity());
            glows.put(event.getTarget(), new ItemGlow(event.getTarget(), red, green, blue));
        }
    }

    @EventHandler
    public void onItemDespawn(ItemDespawnEvent event){
        if(glows.containsKey(event.getEntity()))
            glows.get(event.getEntity()).remove();
    }
}
