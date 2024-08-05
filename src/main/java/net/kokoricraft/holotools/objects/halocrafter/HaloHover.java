package net.kokoricraft.holotools.objects.halocrafter;

import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.entity.Display;
import org.bukkit.entity.Entity;
import org.bukkit.entity.ItemDisplay;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Transformation;

import java.util.HashMap;
import java.util.Map;

public class HaloHover {
    private Map<Integer, ItemDisplay> ingredients = new HashMap<>();
    private ItemDisplay result;
    private final float size = 1 / 8f;

    public void spawn(Player player){
        for(int i = 1; i < 10; i++){
            ItemDisplay display = getOrCreate(player, i);
            player.addPassenger(display);
            ingredients.put(i, display);
        }

        result = player.getWorld().spawn(player.getLocation(), ItemDisplay.class);
        Transformation transformation = result.getTransformation();
        transformation.getTranslation().set(getSlotTranslation(0));
        transformation.getScale().set(-size, size, -size);
        result.setBillboard(Display.Billboard.VERTICAL);
        result.setTransformation(transformation);
        result.setItemDisplayTransform(ItemDisplay.ItemDisplayTransform.GUI);
        result.setRotation(90 + (45 * 4), 0);
        result.setBrightness(new Display.Brightness(15, 15));
        player.addPassenger(result);
        Bukkit.broadcastMessage("spawn hover");
    }

    public void setIngredients(Map<Integer, ItemStack> ingredients){
        for(int slot : this.ingredients.keySet()){
            this.ingredients.get(slot).setItemStack(ingredients.get(slot));
        }
    }

    public void setResult(ItemStack itemStack){
        result.setItemStack(itemStack);
    }

    private ItemDisplay getOrCreate(Player player, int slot){
        ItemDisplay display = player.getWorld().spawn(player.getLocation(), ItemDisplay.class);
        Transformation transformation = display.getTransformation();
        transformation.getTranslation().set(getSlotTranslation(slot));
        transformation.getScale().set(-size, size, -size);
        display.setTransformation(transformation);
        display.setItemDisplayTransform(ItemDisplay.ItemDisplayTransform.GUI);
        display.setBillboard(Display.Billboard.VERTICAL);
        display.setRotation(270 + (45 * 4), 0);
        display.setBrightness(new Display.Brightness(15, 15));
        display.setGlowColorOverride(Color.fromRGB(111, 190, 235));
        //display.setGlowing(true);
        return display;
    }

    private float[] getSlotTranslation(int slot) {
        float separation = 0.03f;
        float rowHeight = 0.15f;
        float colWidth = 0.15f;
        float baseDistance = -1.9f;

        float centered_width = 3 * colWidth / 2;

        if (slot == 0) {
            return new float[]{((3 * colWidth + 2 * separation) - centered_width), -(rowHeight + separation) - 0.2f, baseDistance};
        } else {
            int row = (slot - 1) / 3;
            int col = (slot - 1) % 3;
            return new float[]{((col * colWidth + separation) - centered_width), -(row * rowHeight + separation) - 0.2f, baseDistance};
        }
    }

    public void removeEntities() {
        result.remove();
        ingredients.values().forEach(Entity::remove);
        ingredients.clear();
    }

    public void setAir(){
        for(int i : ingredients.keySet()){
            ingredients.get(i).setItemStack(new ItemStack(Material.AIR));
        }

        result.setItemStack(new ItemStack(Material.AIR));
    }
}
