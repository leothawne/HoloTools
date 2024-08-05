package net.kokoricraft.holotools.objects.halowardrobe;

import org.bukkit.Color;
import org.bukkit.entity.Player;
import org.bukkit.entity.TextDisplay;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.util.Transformation;

public class HaloWardrobeSlot {
    private final Player player;
    private final int slot;
    private TextDisplay background;
    private final static Color dark = Color.fromARGB(150, 25, 167, 210);
    private final static  Color dark_selected = Color.fromARGB(200, 40, 186, 230);
    private final static Color white = Color.fromARGB(150, 36, 220, 229);
    private final static Color white_selected = Color.fromARGB(200, 91, 228, 236);
    private WardrobeContent content;

    public HaloWardrobeSlot(Player player, int slot){
        this.player = player;
        this.slot = slot;
    }

    public void spawnBackground(){
        background = player.getWorld().spawn(player.getLocation(), TextDisplay.class);
        Transformation transformation = background.getTransformation();
        transformation.getScale().set(14, 8, 10);
        transformation.getTranslation().set(-0.175, -1.8, -2.1);
        background.setTransformation(transformation);
        setInCursor(false);

        background.setRotation((270 + (slot * 45)), 0);
        player.addPassenger(background);
    }

    public void removeBackground(){
        if(background != null)
            background.remove();
    }

    public void spawnContent(){

    }

    public void setContent(WardrobeContent content){
        this.content = content;
    }

    public void setInCursor(boolean inCursor){
        boolean second = slot % 2 == 0;
        background.setBackgroundColor(inCursor ? second ? dark_selected : white_selected : second ? dark : white);
    }
}
