package net.kokoricraft.holotools.enums;

import org.bukkit.Color;

public enum HoloColors {
    DARK(Color.fromARGB(150, 25, 167, 210)),
    DARK_SELECTED(Color.fromARGB(200, 40, 186, 230)),
    WHITE(Color.fromARGB(150, 36, 220, 229)),
    WHITE_SELECTED(Color.fromARGB(200, 91, 228, 236));

    private final Color color;
    HoloColors(Color color){
        this.color = color;
    }

    public Color getColor(){
        return color;
    }
}
