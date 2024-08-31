package net.kokoricraft.holotools.managers;

import com.google.gson.JsonObject;
import net.kokoricraft.holotools.HoloTools;
import net.kokoricraft.holotools.objects.NekoConfig;
import net.kokoricraft.holotools.objects.NekoItem;

public class ConfigManager {
    private final HoloTools plugin;
    public String LANG = "en";
    public NekoItem CRAFTER_ITEM;
    public NekoItem WARDROBE_ITEM;

    public ConfigManager(HoloTools plugin){
        this.plugin = plugin;
        loadConfig();
    }

    public void loadConfig(){
        NekoConfig config = new NekoConfig("config.yml", plugin);

        LANG = config.getString("lang", "en");
        config.update();

        NekoConfig crafter = new NekoConfig("holocrafter.yml", plugin);
        CRAFTER_ITEM = new NekoItem(plugin, crafter.getConfigurationSection("item"));
        CRAFTER_ITEM.setTag("holo", "yes");
        CRAFTER_ITEM.setTag("holo_crafter", new JsonObject().toString());


        NekoConfig wardrobe = new NekoConfig("holowardrobe.yml", plugin);
        WARDROBE_ITEM = new NekoItem(plugin, wardrobe.getConfigurationSection("item"));
        WARDROBE_ITEM.setTag("holo", "yes");
        WARDROBE_ITEM.setTag("holo_wardrobe", new JsonObject().toString());
    }
}
