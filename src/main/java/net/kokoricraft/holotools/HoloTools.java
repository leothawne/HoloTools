package net.kokoricraft.holotools;

import net.kokoricraft.holotools.commands.Commands;
import net.kokoricraft.holotools.interfaces.Tickable;
import net.kokoricraft.holotools.listeners.HoloCrafterListener;
import net.kokoricraft.holotools.listeners.HoloListener;
import net.kokoricraft.holotools.listeners.PlayerListener;
import net.kokoricraft.holotools.managers.*;
import net.kokoricraft.holotools.objects.halo.Holo;
import net.kokoricraft.holotools.objects.holocrafter.HoloCrafter;
import net.kokoricraft.holotools.utils.CraftItemsUtils;
import net.kokoricraft.holotools.utils.Utils;
import org.bukkit.Bukkit;
import org.bukkit.command.PluginCommand;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

public final class HoloTools extends JavaPlugin {

    public static Holo test;
    private TickManager tickManager;
    private Manager manager;
    private CompatManager compatManager;
    private Utils utils;
    private CraftItemsUtils craftItemsUtils;
    private HoloManager holoManager;
    private DataManager dataManager;
    private ConfigManager configManager;
    private LangManager langManager;

    @Override
    public void onEnable() {
        // Plugin startup logic
        initCommands();
        initClass();
        initListeners();
        checkPlayers();
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
        for(Tickable tickable : tickManager.getTickableList()){
            if(tickable instanceof Holo holo){
                holo.setVisible(false);
            }
        }
    }

    private void initCommands(){
        PluginCommand command = getCommand("holotools");
        if(command == null) return;
        command.setExecutor(new Commands(this));
    }

    private void initClass(){
        tickManager = new TickManager(this);
        manager = new Manager(this);
        compatManager = new CompatManager(this);
        utils = new Utils(this);
        craftItemsUtils = new CraftItemsUtils(this);
        holoManager = new HoloManager(this);
        dataManager = new DataManager(this);
        configManager = new ConfigManager(this);
        langManager = new LangManager(this);
    }

    private void initListeners(){
        PluginManager pm = Bukkit.getPluginManager();
        pm.registerEvents(new PlayerListener(this), this);
        pm.registerEvents(new HoloListener(this), this);
        pm.registerEvents(new HoloCrafterListener(this), this);
    }

    private void checkPlayers(){
        Bukkit.getOnlinePlayers().forEach(player -> holoManager.check(player));
    }
    public void reloadConfig(){
        configManager.loadConfig();
        langManager.loadMessages();
    }

    public TickManager getTickManager(){
        return tickManager;
    }
    public Manager getManager() {
        return manager;
    }
    public CompatManager getCompatManager(){
        return compatManager;
    }
    public Utils getUtils() {
        return utils;
    }
    public CraftItemsUtils getCraftItemsUtils(){
        return craftItemsUtils;
    }
    public HoloManager getHoloManager(){
        return holoManager;
    }
    public DataManager getDataManager(){
        return dataManager;
    }
    public ConfigManager getConfigManager(){
        return configManager;
    }
    public LangManager getLangManager() {
        return langManager;
    }

    public static HoloTools getInstance(){
        return JavaPlugin.getPlugin(HoloTools.class);
    }
}
