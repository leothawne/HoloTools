package net.kokoricraft.holotools.enums;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Entity;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;

import java.util.Objects;

public enum GlowColor {
    RED("holotools_red", ChatColor.RED),
    GREEN("holotools_green", ChatColor.GREEN),
    BLUE("holotools_blue", ChatColor.BLUE),
    YELLOW("holotools_yellow", ChatColor.YELLOW),
    AQUA("holotools_aqua", ChatColor.AQUA),
    DARK_AQUA("holotools_dark_aqua", ChatColor.DARK_AQUA),
    DARK_BLUE("holotools_dark_blue", ChatColor.DARK_BLUE),
    DARK_GRAY("holotools_dark_gray", ChatColor.DARK_GRAY),
    DARK_GREEN("holotools_dark_green", ChatColor.DARK_GREEN),
    DARK_PURPLE("holotools_dark_purple", ChatColor.DARK_PURPLE),
    DARK_RED("holotools_dark_red", ChatColor.DARK_RED),
    GOLD("holotools_gold", ChatColor.GOLD),
    GRAY("holotools_gray", ChatColor.GRAY),
    LIGHT_PURPLE("holotools_light_purple", ChatColor.LIGHT_PURPLE),
    WHITE("holotools_white", ChatColor.WHITE),
    BLACK("holotools_black", ChatColor.BLACK);

    private final String glow_name;
    private final Team team;

    GlowColor(String name, ChatColor color){
        this.glow_name = name;
        Scoreboard scoreboard = Objects.requireNonNull(Bukkit.getScoreboardManager()).getMainScoreboard();
        if(scoreboard.getTeam(name) != null){
            team = scoreboard.getTeam(name);
            return;
        }

        team = scoreboard.registerNewTeam(name);
        team.setColor(color);
    }

    public String getGlowName(){
        return glow_name;
    }

    public Team getTeam(){
        return team;
    }

    public void setGlow(Entity entity){
        team.addEntry(entity.getUniqueId().toString());
        entity.setGlowing(true);
    }

    public void removeGlow(Entity entity){
        team.removeEntry(entity.getUniqueId().toString());
        entity.setGlowing(false);
    }

    public static void unregisterAll(){
        for (GlowColor color : values()) {
            color.getTeam().unregister();
        }
    }
}
