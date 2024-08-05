package net.kokoricraft.holotools.objects.halocrafter;

import net.kokoricraft.holotools.HoloTools;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.entity.ItemDisplay;
import org.bukkit.entity.Player;
import org.bukkit.entity.TextDisplay;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.util.Transformation;

public class HaloCrafterSlot {
    private boolean visible = false;
    private final int slot;
    private TextDisplay background;
    private ItemCraft selected;
    private final Player player;
    private final HaloCrafter haloCrafter;
    private ItemCraft craftingTable;
    private ItemDisplay selectedDisplay;
    private TextDisplay textDisplay;
    private final static Color dark = Color.fromARGB(150, 25, 167, 210);
    private final static  Color dark_selected = Color.fromARGB(200, 40, 186, 230);
    private final static Color white = Color.fromARGB(150, 36, 220, 229);
    private final static Color white_selected = Color.fromARGB(200, 91, 228, 236);
    private final  static float text_y_crafting_table = 0f;
    private final  static float text_y = 0.2f;
    public HaloCrafterSlot(int slot, Player player, HaloCrafter haloCrafter){
        this.slot = slot;
        this.player = player;
        this.haloCrafter = haloCrafter;
    }

    public void spawnBackground(){
        background = player.getWorld().spawn(player.getLocation(), TextDisplay.class);
        background.setText(" ");
        Transformation transformation = background.getTransformation();
        transformation.getScale().set(14, 8, 10);
        transformation.getTranslation().set(-0.175, -1.8, -2.1);
        background.setTransformation(transformation);
        setInCursor(false);

        background.setRotation((270 + (slot * 45)), 0);
        player.addPassenger(background);

        if(slot == 0){
            craftingTable = new ItemCraft(null, new ItemStack(Material.CRAFTING_TABLE));
            craftingTable.spawnEntities(player, 0);
        }

        spawnSelected();
        textDisplay = player.getWorld().spawn(player.getLocation(), TextDisplay.class);
        Transformation textTransformation = textDisplay.getTransformation();
        if(slot == 0){
            setText("Click para abrir mesa de trabajo");
        }
        textTransformation.getScale().set(0.3, 0.3, 0.3);
        textTransformation.getTranslation().set(0, 0, -1.9);
        textDisplay.setTransformation(textTransformation);
        textDisplay.setDefaultBackground(false);
        textDisplay.setBackgroundColor(Color.fromARGB(0, 0, 0, 0));
        textDisplay.setRotation((270 + (slot * 45)), 0);
        textDisplay.setLineWidth(200);
        player.addPassenger(textDisplay);
    }

    public void setText(String text){
        if(textDisplay == null) return;
        textDisplay.setText(text);

        int height_movement = 0;
        if(text != null && !text.isEmpty()){
            int textSize = JavaPlugin.getPlugin(HoloTools.class).getUtils().getTextLength(text);
            height_movement = (int) Math.ceil((double) textSize / textDisplay.getLineWidth());
        }

        if(height_movement != 0)
            height_movement--;

        Transformation transformation = textDisplay.getTransformation();
        transformation.getTranslation().set(transformation.getTranslation().x, (slot == 0 ? text_y_crafting_table : text_y) - (height_movement * 0.075), transformation.getTranslation().z);
        textDisplay.setTransformation(transformation);
    }

    public void spawnSelected(){
        if(selectedDisplay != null) return;

        selectedDisplay = player.getWorld().spawn(player.getLocation(), ItemDisplay.class);
        Transformation transformation = selectedDisplay.getTransformation();
        transformation.getScale().set(0.5, 0.5, 0.5);
        transformation.getTranslation().set(0, -1, -2);
        selectedDisplay.setTransformation(transformation);
        selectedDisplay.setRotation(270 + (slot * 45), 0);
        if(selected != null)
            selectedDisplay.setItemStack(selected.getResult());

        player.addPassenger(selectedDisplay);
    }
    public void removeBackground(){
        if(background != null)
            background.remove();

        if(craftingTable != null)
            craftingTable.removeEntities();

        if(selectedDisplay != null)
            selectedDisplay.remove();

        if(textDisplay != null)
            textDisplay.remove();
    }

    public void setSelected(ItemCraft itemCraft){
        if(selectedDisplay == null) spawnSelected();
        selected = itemCraft;
        selectedDisplay.setItemStack(itemCraft == null ? new ItemStack(Material.AIR) : itemCraft.getResult());
    }

    public ItemCraft getSelected(){
        return selected;
    }

    public int getSlot(){
        return slot;
    }

    public boolean isVisible(){
        return visible;
    }

    public boolean hasSelected(){
        return selected != null;
    }

    public void setInCursor(boolean inCursor){
        boolean second = slot % 2 == 0;
        background.setBackgroundColor(inCursor ? second ? dark_selected : white_selected : second ? dark : white);
    }
}
