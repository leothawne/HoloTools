package net.kokoricraft.holotools.commands;

import joptsimple.internal.Strings;
import net.kokoricraft.holotools.HoloTools;
import net.kokoricraft.holotools.objects.tooltip.TooltipDisplay;
import net.kokoricraft.holotools.utils.objects.HoloColor;
import net.kokoricraft.holotools.version.HoloItemDisplay;
import net.kokoricraft.holotools.version.HoloTextDisplay;
import net.kokoricraft.holotools.version.v1_21_R1;
import net.md_5.bungee.api.chat.BaseComponent;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Display;
import org.bukkit.entity.ItemDisplay;
import org.bukkit.entity.Player;
import org.bukkit.entity.TextDisplay;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;

import java.util.ArrayList;
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
            case "test2" -> test2(sender, args);
        }

        return true;
    }

    private void testCommand(CommandSender sender, String[] args) {
        Player player = (Player)sender;

        Location location = player.getLocation().clone();

        float size = 0.5f;

        int rows = 4;

        float translation_per_row = -0.015f;

        float y_translation = (translation_per_row * rows) - translation_per_row;

        HoloTextDisplay d1 = plugin.getCompatManager().createTextDisplay(List.of(player), location, 0, 0);
        d1.setText(getText(2));
        d1.setTextOpacity((byte) 10);
        d1.setLineWidth(999999999);
        d1.setColor(HoloColor.fromHex("#ee87d4", 255));
        d1.setTranslation(0, y_translation * size, -1.5f);
        d1.setScale(1 * size, (1 + (.45f / rows)) * size, 1 * size);
        d1.setAlignment(TextDisplay.TextAlignment.LEFT);
        d1.setBillboard(Display.Billboard.CENTER);
        d1.mount(player);
        d1.update();

        HoloTextDisplay d2 = plugin.getCompatManager().createTextDisplay(List.of(player), location, 0, 0);
        d2.setText(getText(0));
        d2.setLineWidth(999999999);
        d2.setColor(HoloColor.fromHex("#2e2b00", 255));
        d2.setTranslation(0, 0, -1.48f);
        d2.setAlignment(TextDisplay.TextAlignment.LEFT);
        d2.setBillboard(Display.Billboard.CENTER);
        d2.setScale(1 * size, 1 * size, 1 * size);
        d2.mount(player);
        d2.setShadowed(true);
        d2.update();


        HoloItemDisplay d3 = plugin.getCompatManager().createItemDisplay(List.of(player), location, 0, 0);
        ItemStack itemStack = new ItemStack(Material.PLAYER_HEAD);
        SkullMeta skullMeta = (SkullMeta)itemStack.getItemMeta();
        skullMeta.setOwningPlayer(player);
        itemStack.setItemMeta(skullMeta);
        d3.setItemStack(itemStack);
        d3.setTranslation(0, 0, -1.4f);
        d3.setScale(-1 * size, 1 * size, -1 * size);
        d3.mount(player);
        d3.setBillboard(Display.Billboard.CENTER);
        d3.setItemDisplayTransform(ItemDisplay.ItemDisplayTransform.HEAD);
        d3.setRotation(0, 5, 0);
        d3.update();

        sender.sendMessage("test");

        Bukkit.getScheduler().runTaskLater(plugin, () ->{
            d1.remove();
            d2.remove();
            d3.remove();
        }, 20 * 15);
    }

    public void test2(CommandSender sender, String[] args){

        float size = 6;
        float distance = -3f;
        float height = -2f;

        List<HoloTextDisplay> displays = new ArrayList<>();

        for(Player player : Bukkit.getOnlinePlayers()){
            HoloTextDisplay d1 = plugin.getCompatManager().createTextDisplay(List.of(player), player.getLocation(), 0, 0);
            d1.setColor(HoloColor.BLACK);
            d1.setText("aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa");
            d1.setLineWidth(120);
            d1.setTranslation(0, height, distance);
            d1.setScale(size, size, size);
            d1.mount(player);
            d1.setTextOpacity((byte)20);
            d1.update();

            HoloTextDisplay d2 = plugin.getCompatManager().createTextDisplay(List.of(player), player.getLocation(), 0, 90);
            d2.setColor(HoloColor.BLACK);
            d2.setText("aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa");
            d2.setLineWidth(120);
            d2.setTranslation(0, height, distance);
            d2.setScale(size, size, size);
            d2.mount(player);
            d2.setTextOpacity((byte)20);
            d2.update();

            HoloTextDisplay d3 = plugin.getCompatManager().createTextDisplay(List.of(player), player.getLocation(), 0, 180);
            d3.setColor(HoloColor.BLACK);
            d3.setText("aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa");
            d3.setLineWidth(120);
            d3.setTranslation(0, height, distance);
            d3.setScale(4, 4, 3);
            d3.mount(player);
            d3.setTextOpacity((byte)20);
            d3.update();

            HoloTextDisplay d4 = plugin.getCompatManager().createTextDisplay(List.of(player), player.getLocation(), 0, 270);
            d4.setColor(HoloColor.BLACK);
            d4.setText("aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa");
            d4.setLineWidth(120);
            d4.setTranslation(0, height, distance);
            d4.setScale(size, size, size);
            d4.mount(player);
            d4.setTextOpacity((byte)20);
            d4.update();

            HoloTextDisplay d5 = plugin.getCompatManager().createTextDisplay(List.of(player), player.getLocation(), 90, 0);
            d5.setColor(HoloColor.BLACK);
            d5.setText("aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa");
            d5.setLineWidth(120);
            d5.setTranslation(0, distance, -2);
            d5.setScale(size, size, size);
            d5.mount(player);
            d5.setTextOpacity((byte)20);
            d5.update();

            HoloTextDisplay d6 = plugin.getCompatManager().createTextDisplay(List.of(player), player.getLocation(), -90, 0);
            d6.setColor(HoloColor.BLACK);
            d6.setText("aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa");
            d6.setLineWidth(120);
            d6.setTranslation(0, distance, -2);
            d6.setScale(size, size, size);
            d6.mount(player);
            d6.setTextOpacity((byte)20);
            d6.update();

            displays.add(d1);
            displays.add(d2);
            displays.add(d3);
            displays.add(d4);
            displays.add(d5);
            displays.add(d6);
        }



        Bukkit.getScheduler().runTaskLater(plugin, () ->{
            displays.forEach(HoloTextDisplay::remove);
        }, 20 * 60);
    }

    public String getText(int i){
        List<String> list = new ArrayList<>();
        String points = Strings.repeat('.', i);
        if(i == 0)
            points = "";

        list.add(points+"&#2e2b00Esto solo es una &#2e2b00prueba&#2e2b00 para ver si"+points);
        list.add(points+"&#2e2b00Sirve hacer un plugin de cinematicas"+points);
        list.add(points+"&#2e2b00o abandonarlo y yas. de todas formas"+points);
        list.add(points+"&#2e2b00no pasa nada por probar asi que &o:cat:"+points);

        return plugin.getUtils().color(String.join("\n", list));
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
            case "holocrafter" -> item = plugin.getConfigManager().CRAFTER_ITEM.getItem("id", plugin.getDataManager().getStorage().getNextID());
            case "holowardrobe" -> item = plugin.getConfigManager().WARDROBE_ITEM.getItem("id", plugin.getDataManager().getStorage().getNextID());
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
