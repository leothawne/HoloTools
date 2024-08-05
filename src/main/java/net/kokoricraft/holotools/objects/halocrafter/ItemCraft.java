package net.kokoricraft.holotools.objects.halocrafter;

import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.entity.Display;
import org.bukkit.entity.ItemDisplay;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Transformation;

import java.util.HashMap;
import java.util.Map;

public class ItemCraft {
    private final Map<Integer, ItemStack> ingredients;
    private final ItemStack result;
    private final Map<Integer, ItemDisplay> entities = new HashMap<>();

    public ItemCraft(Map<Integer, ItemStack> ingredients, ItemStack result) {
        this.ingredients = ingredients;
        this.result = result;
    }

    public ItemStack getIngredient(int slot) {
        return ingredients.get(slot);
    }

    public Map<Integer, ItemStack> getIngredients(){
        return ingredients;
    }

    public ItemStack getResult() {
        return result;
    }

    public void removeEntities(){
        for (ItemDisplay display : entities.values()) {
            display.remove();
        }

        entities.clear();
    }

    private float[] getSlotTranslation(int slot) {
        float separation = 0.03f;
        float rowHeight = 0.15f;
        float colWidth = 0.15f;
        float baseDistance = -1.9f;

        float centered_width = 3 * colWidth / 2;

        if (slot == 0) {
            return new float[]{((3 * colWidth + 2 * separation) - centered_width), -(rowHeight + separation), baseDistance};
        } else {
            int row = (slot - 1) / 3;
            int col = (slot - 1) % 3;
            return new float[]{((col * colWidth + separation) - centered_width), -(row * rowHeight + separation), baseDistance};
        }
    }

    public void spawnEntities(Player player, int haloSlot) {
        float size = 1 / 8f;
        boolean isCraftingTable = ingredients == null;

        if(!isCraftingTable){
            for (int slot : ingredients.keySet()) {
                ItemStack ingredient = ingredients.get(slot);
                ItemDisplay display = player.getWorld().spawn(player.getLocation(), ItemDisplay.class);
                Transformation transformation = display.getTransformation();
                transformation.getTranslation().set(getSlotTranslation(slot));
                transformation.getScale().set(-size, size, -size);
                display.setTransformation(transformation);
                display.setItemDisplayTransform(ItemDisplay.ItemDisplayTransform.GUI);
                display.setBillboard(Display.Billboard.VERTICAL);
                display.setRotation(270 + (45 * haloSlot), 0);
                display.setBrightness(new Display.Brightness(15, 15));
                display.setGlowColorOverride(Color.fromRGB(111, 190, 235));
                display.setGlowing(true);
                entities.put(slot, display);
                display.setItemStack(ingredient);
                player.addPassenger(display);
            }
        }

        ItemDisplay resultDisplay = player.getWorld().spawn(player.getLocation(), ItemDisplay.class);
        Transformation transformation = resultDisplay.getTransformation();

        if(isCraftingTable){
            transformation.getTranslation().set(0, -1, 1.95);
            transformation.getScale().set(0.8, 0.8, 0.8);
        }else {
            transformation.getTranslation().set(getSlotTranslation(0));
            transformation.getScale().set(-size, size, -size);
            resultDisplay.setBillboard(Display.Billboard.VERTICAL);
        }

        resultDisplay.setTransformation(transformation);
        resultDisplay.setItemDisplayTransform(ItemDisplay.ItemDisplayTransform.GUI);
        resultDisplay.setRotation(90 + (45 * haloSlot), 0);
        resultDisplay.setBrightness(new Display.Brightness(15, 15));
        entities.put(0, resultDisplay);
        resultDisplay.setItemStack(result);
        player.addPassenger(resultDisplay);
    }
}
