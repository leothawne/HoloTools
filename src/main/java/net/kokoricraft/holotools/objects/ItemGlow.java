package net.kokoricraft.holotools.objects;

import net.kokoricraft.holotools.HoloTools;
import net.kokoricraft.holotools.utils.objects.HoloColor;
import net.kokoricraft.holotools.version.HoloTextDisplay;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Display;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class ItemGlow {
    private final List<HoloTextDisplay> entities = new ArrayList<>();
    HoloTools plugin = HoloTools.getInstance();
    private final Entity entity;
    private final int red;
    private final int green;
    private final int blue;

    public ItemGlow(Entity entity, int red, int green, int blue){
        this.entity = entity;
        this.red = red;
        this.green = green;
        this.blue = blue;

        Location location = entity.getLocation();

        List<Player> players = new ArrayList<>(Bukkit.getOnlinePlayers());

        float scale = 0.75f;

        HoloTextDisplay main = getHolo(players, location, entity);

        main.setText(".\n.\n.\n.\n.\n.\n.\n.\n.\n.\n.\n.\n.\n.\n.");
        main.setColor(HoloColor.fromARGB(255, red, green, blue));
        main.setScale(0.3f, 1 * scale, .3f);
        main.setTranslation(0, 0, -0.05f);
        main.update();

        HoloTextDisplay main_top = getHolo(players, location, entity);

        main_top.setText(".");
        main_top.setColor(HoloColor.fromARGB(200, red, green, blue));
        main_top.setScale(0.3f, 0.1f * scale, .3f);
        main_top.setTranslation(0, 3.775f * scale, -0.05f);
        main_top.update();

        HoloTextDisplay main_top_2 = getHolo(players, location, entity);

        main_top_2.setText(".");
        main_top_2.setColor(HoloColor.fromARGB(100, red, green, blue));
        main_top_2.setScale(0.3f, 0.1f * scale, .3f);
        main_top_2.setTranslation(0, 3.803f * scale, -0.05f);
        main_top_2.update();

        HoloTextDisplay left = getHolo(players, location, entity);

        left.setText(".\n.\n.\n.\n.\n.\n.\n.\n.\n.\n.\n.\n.\n.\n.");
        left.setColor(HoloColor.fromARGB(200, red, green, blue));
        left.setTranslation(-0.012f, 0, -0.05f);
        left.setScale(.1f, 1 * scale, .1f);
        left.update();

        HoloTextDisplay right = getHolo(players, location, entity);

        right.setText(".\n.\n.\n.\n.\n.\n.\n.\n.\n.\n.\n.\n.\n.\n.");
        right.setColor(HoloColor.fromARGB(200, red, green, blue));
        right.setTranslation(0.017f, 0, -0.05f);
        right.setScale(.1f, 1 * scale, .1f);
        right.update();

        HoloTextDisplay left_2 = getHolo(players, location, entity);

        left_2.setText(".\n.\n.\n.\n.\n.\n.\n.\n.\n.\n.\n.\n.\n.");
        left_2.setColor(HoloColor.fromARGB(100, red, green, blue));
        left_2.setTranslation(-0.0195f, 0, -0.05f);
        left_2.setScale(.1f, 1 * scale, .1f);
        left_2.update();

        HoloTextDisplay right_2 = getHolo(players, location, entity);

        right_2.setText(".\n.\n.\n.\n.\n.\n.\n.\n.\n.\n.\n.\n.\n.");
        right_2.setColor(HoloColor.fromARGB(100, red, green, blue));
        right_2.setTranslation(0.0245f, 0, -0.05f);
        right_2.setScale(.1f, 1 * scale, .1f);
        right_2.update();

        HoloTextDisplay left_3 = getHolo(players, location, entity);

        left_3.setText(".\n.\n.\n.\n.\n.\n.\n.\n.\n.\n.\n.\n.");
        left_3.setColor(HoloColor.fromARGB(60, red, green, blue));
        left_3.setTranslation(-0.027f, 0, -0.05f);
        left_3.setScale(.1f, 1 * scale, .1f);
        left_3.update();

        HoloTextDisplay right_3 = getHolo(players, location, entity);

        right_3.setText(".\n.\n.\n.\n.\n.\n.\n.\n.\n.\n.\n.\n.");
        right_3.setColor(HoloColor.fromARGB(60, red, green, blue));
        right_3.setTranslation(0.032f, 0, -0.05f);
        right_3.setScale(.1f, 1 * scale, .1f);
        right_3.update();

        HoloTextDisplay left_4 = getHolo(players, location, entity);

        left_4.setText(".\n.\n.\n.\n.\n.\n.\n.\n.\n.\n.\n.");
        left_4.setColor(HoloColor.fromARGB(40, red, green, blue));
        left_4.setTranslation(-0.0345f, 0, -0.05f);
        left_4.setScale(.1f, 1 * scale, .1f);
        left_4.update();

        HoloTextDisplay right_4 = getHolo(players, location, entity);

        right_4.setText(".\n.\n.\n.\n.\n.\n.\n.\n.\n.\n.\n.");
        right_4.setColor(HoloColor.fromARGB(40, red, green, blue));
        right_4.setTranslation(0.0395f, 0, -0.05f);
        right_4.setScale(.1f, 1 * scale, .1f);
        right_4.update();

        HoloTextDisplay left_5 = getHolo(players, location, entity);

        left_5.setText(".\n.\n.\n.\n.\n.\n.\n.\n.\n.\n.");
        left_5.setColor(HoloColor.fromARGB(26, red, green, blue));
        left_5.setTranslation(-0.042f, 0, -0.05f);
        left_5.setScale(.1f, 1 * scale, .1f);
        left_5.update();

        HoloTextDisplay right_5 = getHolo(players, location, entity);

        right_5.setText(".\n.\n.\n.\n.\n.\n.\n.\n.\n.\n.");
        right_5.setColor(HoloColor.fromARGB(26, red, green, blue));
        right_5.setTranslation(0.047f, 0, -0.05f);
        right_5.setScale(.1f, 1 * scale, .1f);
        right_5.update();

        entities.add(main_top);
        entities.add(main_top_2);
        entities.add(main);
        entities.add(left);
        entities.add(right);
        entities.add(left_2);
        entities.add(right_2);
        entities.add(right_3);
        entities.add(left_3);
        entities.add(right_4);
        entities.add(left_4);
        entities.add(left_5);
        entities.add(right_5);
    }

    private HoloTextDisplay getHolo(List<Player> players, Location location, Entity entity){
        HoloTextDisplay main = plugin.getCompatManager().createTextDisplay(players, location, 0, 0);
        main.setBillboard(Display.Billboard.VERTICAL);
        main.setTextOpacity((byte) 20);
        main.setBrightness(new Display.Brightness(15, 15));
        main.mount(entity);

        return main;
    }

    public void remove(){
        entities.forEach(HoloTextDisplay::remove);
    }

    public Entity getEntity() {
        return entity;
    }

    public int getRed() {
        return red;
    }

    public int getGreen() {
        return green;
    }

    public int getBlue() {
        return blue;
    }
}
