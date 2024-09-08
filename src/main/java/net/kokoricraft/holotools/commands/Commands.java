package net.kokoricraft.holotools.commands;

import net.kokoricraft.holotools.HoloTools;
import net.kokoricraft.holotools.version.HoloItemDisplay;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.List;

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
        }

        return true;
    }

    private HoloItemDisplay holo;

    private void testCommand(CommandSender sender, String[] args) {
//        Player player = (Player)sender;
//        v1_21_R1 test = (v1_21_R1) plugin.getCompatManager().getCompat();
//        boolean enable = (args.length != 1);
//
//        if(enable){
//            if(holo != null){
//                holo.remove();
//            }
//            holo = plugin.getCompatManager().createItemDisplay(List.of(player), player.getLocation(), 0, 0);
//            holo.setScale(.3f, .3f, .3f);
//            holo.setItemStack(new ItemStack(Material.DIAMOND_HELMET));
//            holo.setBillboard(Display.Billboard.VERTICAL);
//            holo.setItemDisplayTransform(ItemDisplay.ItemDisplayTransform.HEAD);
//            holo.setTranslation(0f, 0f, 1.3f);
//            holo.update();
//        }else {
//            holo.remove();
//            holo = null;
//        }
//        test.test(player, args.length == 1, holo);
//
//
//        sender.sendMessage("yap creative: "+ (args.length == 1));

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
