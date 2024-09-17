package net.kokoricraft.holotools.utils;

import net.kokoricraft.holotools.HoloTools;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Scanner;
import java.util.function.Consumer;

// From: https://www.spigotmc.org/wiki/creating-an-update-checker-that-checks-for-updates
public class UpdateChecker {

    private final HoloTools plugin;
    private final int resourceId;

    public UpdateChecker(HoloTools plugin, int resourceId) {
        this.plugin = plugin;
        this.resourceId = resourceId;
    }

    public void getVersion(final Consumer<String> consumer) {
        Bukkit.getScheduler().runTaskAsynchronously(this.plugin, () -> {
            try (InputStream is = new URL("https://api.spigotmc.org/legacy/update.php?resource=" + this.resourceId + "/~").openStream(); Scanner scann = new Scanner(is)) {
                if (scann.hasNext()) {
                    consumer.accept(scann.next());
                }
            } catch (IOException e) {
                plugin.getLogger().info("Unable to check for updates: " + e.getMessage());
            }
        });
    }

    public void sendMessage(CommandSender sender, boolean updated, String version){
        if(updated || !plugin.getConfigManager().UPDATE_CHECKER) return;

        sender.sendMessage(plugin.getUtils().color(String.format("&eA new version is available! [%s]", version)));
        sender.sendMessage(plugin.getUtils().color("&eLink: https://www.spigotmc.org/resources/holotools-new-version."+resourceId));
    }
}
