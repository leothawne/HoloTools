package net.kokoricraft.holotools.version;

import net.kokoricraft.holotools.utils.objects.HoloColor;
import org.bukkit.Location;
import org.bukkit.entity.Display;
import org.bukkit.entity.Player;
import org.bukkit.entity.TextDisplay;

public interface HoloTextDisplay {
    void update(Location location);
    void remove();
    void setText(String text);
    void setColor(HoloColor color);
    void setGlowing(boolean glowing);
    void setScale(float x, float y, float z);
    void setRotation(float x, float y, float z);
    void setRotation(float v, float v2);
    void setSeeThrough(boolean seeThrough);
    void setLineWidth(int width);
    void setOpacity(byte opacity);
    void setShadowed(boolean shadowed);
    void setAlignment(TextDisplay.TextAlignment alignment);
    void setTranslation(float x, float y, float z);
    void update();
    void setBillboard(Display.Billboard billboard);
    void mount(Player player);
    Location getLocation();
    void setBrightness(Display.Brightness brightness);
}
