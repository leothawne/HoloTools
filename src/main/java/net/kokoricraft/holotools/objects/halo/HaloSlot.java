package net.kokoricraft.holotools.objects.halo;

import com.google.common.collect.Lists;
import net.kokoricraft.holotools.HoloTools;
import net.kokoricraft.holotools.utils.objects.HoloColor;
import net.kokoricraft.holotools.version.HoloItemDisplay;
import net.kokoricraft.holotools.version.HoloTextDisplay;
import org.bukkit.*;
import org.bukkit.entity.Display;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class HaloSlot {
    private final int slot;
    private final double x;
    private final double y;
    private final double z;
    private final float x_size;
    private final float y_size;
    private final float z_size;
    private final float rotation;
    private HoloTextDisplay background;
    private final Map<String, HoloItemDisplay> itemDisplayMap = new HashMap<>();
    private final Map<String, HoloTextDisplay> textDisplayMap = new HashMap<>();
    private final HoloTools plugin = JavaPlugin.getPlugin(HoloTools.class);
    private HoloColor color = HoloColor.fromARGB(150, 25, 167, 210);
    private final Holo holo;

    public HaloSlot(int slot, double x, double y, double z, float x_size, float y_size, float z_size, float rotation, Holo holo) {
        this.slot = slot;
        this.x = x;
        this.y = y;
        this.z = z;
        this.x_size = x_size;
        this.y_size = y_size;
        this.z_size = z_size;
        this.rotation = rotation;
        this.holo = holo;
    }

    public void spawn(Player player){
        Location location = player.getLocation();
        World world = location.getWorld();
        if(world == null) return;

        background = plugin.getCompatManager().createTextDisplay(Lists.newArrayList(player), location, 0, rotation + holo.getInitialYaw() - 90);
        background.setTranslation((float) x, (float) y, (float) z);
        background.setScale(x_size, y_size, z_size);
        background.setText(" ");
        background.setBrightness(new Display.Brightness(15, 15));
        background.setColor(color);
        background.update();
        background.mount(player);
    }

    public void remove(String reason){
        if(background != null)
            background.remove();

        itemDisplayMap.values().forEach(HoloItemDisplay::remove);
        textDisplayMap.values().forEach(HoloTextDisplay::remove);
    }

    public void setColor(HoloColor color){
        if(background == null){
            this.color = color;
            return;
        }

        background.setColor(color);
        background.update();
    }

    public void addItemDisplay(String key, HoloItemDisplay display){
        itemDisplayMap.put(key, display);
    }

    public void addTextDisplay(String key, HoloTextDisplay display){
        textDisplayMap.put(key, display);
    }

    public HoloItemDisplay getItemDisplay(String key){
        return itemDisplayMap.get(key);
    }

    public HoloTextDisplay getTextDisplay(String key){
        return textDisplayMap.get(key);
    }

    public int getSlot(){
        return slot;
    }
    public float getInitialYaw(){
        return holo.getInitialYaw();
    }
}
