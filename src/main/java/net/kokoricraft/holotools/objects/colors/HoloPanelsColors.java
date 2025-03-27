package net.kokoricraft.holotools.objects.colors;

import java.util.Map;

import org.bukkit.entity.Player;

import net.kokoricraft.holotools.managers.ConfigManager;

public class HoloPanelsColors {
    private final String name;
    private final Map<String, DualColor> colors;
    private final boolean need_permission;
    private final String permission;

    public HoloPanelsColors(String name, Map<String, DualColor> colors, boolean need_permission, String permission){
        this.name = name;
        this.colors = colors;
        this.need_permission = need_permission;
        this.permission = permission;
    }

    public boolean canUse(Player player){
        return !need_permission || player.hasPermission(getPermission());
    }

    public String getPermission(){
        if(permission == null) return "holotools.permission.color."+name;
        return permission;
    }

    public DualColor getColor(int slot){
        DualColor color = colors.get("slot_"+slot);
        if(color != null) return color;

        DualColor defaultColor = colors.get("default_slot");
        if(defaultColor != null) return defaultColor;

        return ConfigManager.DUAL_COLOR_DEF;
    }

    public String getName() {
        return name;
    }
}
