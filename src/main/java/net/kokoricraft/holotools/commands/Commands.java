package net.kokoricraft.holotools.commands;

import net.kokoricraft.holotools.HoloTools;
import net.kokoricraft.holotools.objects.tooltip.TooltipDisplay;
import net.kokoricraft.holotools.version.HoloItemDisplay;
import net.kokoricraft.holotools.version.v1_21_R1;
import net.md_5.bungee.api.chat.BaseComponent;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class Commands implements CommandExecutor {
    private final HoloTools plugin;

    public Commands(HoloTools plugin){
        this.plugin = plugin;
    }
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args){
        Player player = (Player) sender;
        if(args.length == 0){
            player.sendMessage(plugin.getUtils().color(String.format("&cUse %s give / reload", label)));
            return true;
        }

        switch (args[0].toLowerCase()){
            case "give" -> giveCommand(sender, args);
            case "reload" -> reloadCommand(sender);
            case "debug" -> debugCommand(sender);
            case "test" -> testCommand(sender, args);
            case "test2" -> test2(sender, args);
        }

        return true;
    }

    private TooltipDisplay tooltipDisplay = null;
    private void testCommand(CommandSender sender, String[] args) {
        Player player = (Player)sender;
        boolean isNull = tooltipDisplay == null;

        if(tooltipDisplay == null){
            tooltipDisplay = new TooltipDisplay(player);
            tooltipDisplay.spawn();
        }else{
            tooltipDisplay.setItemStack(player.getInventory().getItemInMainHand());
        }

        if(isNull)
            Bukkit.getScheduler().runTaskLater(plugin, () ->{
                tooltipDisplay.remove();
                tooltipDisplay = null;
            }, 20 * 30);

        sender.sendMessage("test");
    }

    public void test2(CommandSender sender, String[] args){
        Player player = (Player)sender;

        ItemStack itemStack = player.getInventory().getItemInMainHand();
//        PotionMeta meta = (PotionMeta) itemStack.getItemMeta();

        //Bukkit.broadcastMessage(String.format("%s, %s, %s", meta.getBasePotionData().getType(), meta.getBasePotionData().isExtended(), meta.getBasePotionData().isUpgraded()));


    }

    private void debugCommand(CommandSender sender) {
        if(!(sender.hasPermission("holotools.command.debug"))){
            sender.sendMessage(plugin.getUtils().color(plugin.getLangManager().NO_PERMISSION));
            return;
        }

        boolean enabled = plugin.getUtils().toggleDebug(sender);
        sender.sendMessage(plugin.getUtils().color(enabled ? "&aDebug mode enabled!" : "&cDebug mode disabled!"));
    }

    private void giveCommand(CommandSender sender, String[] args){
        if(!sender.hasPermission("holotools.command.give")){
            sender.sendMessage(plugin.getUtils().color(plugin.getLangManager().NO_PERMISSION));
            return;
        }

        if(args.length < 2 || !(sender instanceof Player) && args.length < 3){
            sender.sendMessage(plugin.getUtils().color("&cIncorrect usage. Use: /holotools give <item> [player] [amount]"));
            return;
        }

        String itemName = args[1].toLowerCase();
        Player target = args.length == 3 ? plugin.getServer().getPlayer(args[2]) : (Player) sender;
        int amount = args.length == 4 ? Integer.parseInt(args[3]) : args.length == 3 && target != sender ? 1 : args.length == 2 ? 1 : Integer.parseInt(args[2]);

        if(target == null){
            sender.sendMessage(plugin.getUtils().color("&cPlayer not found."));
            return;
        }

        ItemStack item;
        switch(itemName){
            case "holocrafter" -> item = plugin.getConfigManager().CRAFTER_ITEM.getItem();
            case "holowardrobe" -> item = plugin.getConfigManager().WARDROBE_ITEM.getItem();
            default -> {
                sender.sendMessage(plugin.getUtils().color("&cTool does not exist."));
                return;
            }
        }

        item.setAmount(amount);

        target.getInventory().addItem(item);
        target.sendMessage(plugin.getUtils().color(String.format("&aYou have received %d %s(s).", amount, itemName)));
        if(sender != target) sender.sendMessage(plugin.getUtils().color(String.format("&aYou have given %d %s(s) to %s.", amount, itemName, target.getName())));
    }

    private void reloadCommand(CommandSender sender){
        if(!sender.hasPermission("holotools.command.reload")){
            sender.sendMessage(plugin.getUtils().color(plugin.getLangManager().NO_PERMISSION));
            return;
        }

        plugin.reloadConfig();
        sender.sendMessage(plugin.getUtils().color(plugin.getLangManager().CONFIG_RELOADED));
    }
}
