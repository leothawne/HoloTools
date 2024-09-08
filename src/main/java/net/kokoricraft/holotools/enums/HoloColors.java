package net.kokoricraft.holotools.enums;


import net.kokoricraft.holotools.utils.objects.HoloColor;

import java.awt.*;

public enum HoloColors {
    DARK(HoloColor.fromARGB(150, 25, 167, 210)),
    DARK_SELECTED(HoloColor.fromARGB(200, 40, 186, 230)),
    WHITE(HoloColor.fromARGB(150, 36, 220, 229)),
    WHITE_SELECTED(HoloColor.fromARGB(200, 91, 228, 236));

    private final HoloColor color;
    HoloColors(HoloColor color){
        this.color = color;
    }

    public HoloColor getColor(){
        return color;
    }
}
