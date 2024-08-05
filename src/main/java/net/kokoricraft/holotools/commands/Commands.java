package net.kokoricraft.holotools.commands;

import net.kokoricraft.holotools.HoloTools;
import net.kokoricraft.holotools.objects.halocrafter.HaloCrafter;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;


public class Commands implements CommandExecutor {
    private final HoloTools plugin;

    public Commands(HoloTools plugin){
        this.plugin = plugin;
    }
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        Player player = (Player) sender;
        if(args.length == 0){
            player.sendMessage("Ingresa argumentos!");
            return true;
        }

        switch (args[0].toLowerCase()){
            case "test" ->{
               HaloCrafter haloCrafter = new HaloCrafter(plugin, player);
                plugin.getManager().setHaloCrafter(player, haloCrafter);
               haloCrafter.setVisible(true);
            }
            case "open" ->{
                plugin.getManager().openHaloCrafting(player);
                player.sendMessage("Debio abrir v:");
            }
        }

        return true;
    }
}
