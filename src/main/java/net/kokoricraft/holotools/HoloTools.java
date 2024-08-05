package net.kokoricraft.holotools;

import net.kokoricraft.holotools.commands.Commands;
import net.kokoricraft.holotools.listeners.PlayerListener;
import net.kokoricraft.holotools.managers.Manager;
import net.kokoricraft.holotools.objects.halocrafter.HaloCrafter;
import net.kokoricraft.holotools.utils.enums.Utils;
import org.bukkit.Bukkit;
import org.bukkit.command.PluginCommand;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

public final class HoloTools extends JavaPlugin {

    public static HaloCrafter test;
    private Manager manager;
    private Utils utils;

    @Override
    public void onEnable() {
        // Plugin startup logic
        initCommands();
        initClass();
        initListeners();
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
        for(HaloCrafter haloCrafter : manager.getAllHaloCrafter()){
            haloCrafter.setVisible(false);
        }
    }

    private void initCommands(){
        PluginCommand command = getCommand("holotools");
        if(command == null) return;
        command.setExecutor(new Commands(this));
    }

    private void initClass(){
        manager = new Manager(this);
        utils = new Utils(this);
    }

    private void initListeners(){
        PluginManager pm = Bukkit.getPluginManager();
        pm.registerEvents(new PlayerListener(this), this);
    }

    public Manager getManager() {
        return manager;
    }

    public Utils getUtils() {
        return utils;
    }
}
