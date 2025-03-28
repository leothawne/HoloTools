package net.kokoricraft.holotools.utils;

import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Scanner;
import java.util.function.Consumer;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;

import net.kokoricraft.holotools.HoloTools;

// From: https://www.spigotmc.org/wiki/creating-an-update-checker-that-checks-for-updates
public class UpdateChecker {

    private final HoloTools plugin;

    public UpdateChecker(HoloTools plugin) {
        this.plugin = plugin;
    }

    public void getVersion(final Consumer<Integer> consumer) {
        Bukkit.getScheduler().runTaskAsynchronously(this.plugin, () -> {
            try (InputStream is = new URI("https://jenkins.gmj.net.br/job/LTItemMail/lastSuccessfulBuild/buildNumber").toURL().openStream(); Scanner scann = new Scanner(is)) {
                if (scann.hasNext()) {
                    consumer.accept(Integer.valueOf(scann.next()));
                }
            } catch (IOException | URISyntaxException e) {
                plugin.getLogger().info("Unable to check for updates: " + e.getMessage());
            }
        });
    }

    public void sendMessage(CommandSender sender, boolean updated, int build){
        if(updated || !plugin.getConfigManager().UPDATE_CHECKER) return;

        sender.sendMessage(plugin.getUtils().color(String.format("&eA new build is available! [#%s]", build)));
        sender.sendMessage(plugin.getUtils().color(String.format("&eLink: https://jenkins.gmj.net.br/job/HoloTools/%s/", build)));
    }
}
