package net.kokoricraft.holotools.objects.holocrafter;

import net.kokoricraft.holotools.HoloTools;
import net.kokoricraft.holotools.version.HoloItemDisplay;
import org.bukkit.Material;
import org.bukkit.entity.Display;
import org.bukkit.entity.ItemDisplay;
import org.bukkit.entity.Player;
import org.bukkit.inventory.*;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HoloHover {
    private final HoloTools plugin = JavaPlugin.getPlugin(HoloTools.class);
    private final Map<Integer, HoloItemDisplay> entities = new HashMap<>();
    private final float size = 1 / 8f;
    private Recipe recipe;
    public void spawn(Player player){

        for(int i = 1; i < 10; i++){
            HoloItemDisplay display = getOrCreate(player, i);
            display.mount(player);
            entities.put(i, display);
        }

        HoloItemDisplay result = plugin.getCompatManager().createItemDisplay(List.of(player), player.getLocation(), 90 + (45 * 4), 0);
        result.setTranslation(getSlotTranslation(0)[0], getSlotTranslation(0)[1], getSlotTranslation(0)[2]);
        result.setScale(-size, size, -size);
        result.setBillboard(Display.Billboard.VERTICAL);
        result.setItemDisplayTransform(ItemDisplay.ItemDisplayTransform.GUI);
        result.setBrightness(new Display.Brightness(15, 15));
        result.update();
        result.mount(player);

        entities.put(0, result);
    }

    public void setRecipe(Recipe recipe){
        this.recipe = recipe;

        Map<Integer, ItemStack> slots = new HashMap<>();

        if(recipe instanceof ShapedRecipe shapedRecipe)
           slots = getShapedRecipeSlots(shapedRecipe);

        if(recipe instanceof ShapelessRecipe shapelessRecipe){
            slots = getShapelessRecipeSlots(shapelessRecipe);
        }

        for(int key = 0; key < 10; key++){
            if(!slots.containsKey(key)) continue;
            HoloItemDisplay display = entities.get(key);
            ItemStack itemStack = slots.get(key);
            if(itemStack == null || itemStack.getType() == Material.AIR){
                display.setViewRange(0);
                display.update();
            }else {
                display.setItemStack(slots.get(key));
                display.setViewRange(20);
                display.update();
            }
        }
    }

    private HoloItemDisplay getOrCreate(Player player, int slot){
        HoloItemDisplay display = plugin.getCompatManager().createItemDisplay(List.of(player), player.getLocation(), 270 + (45 * 4), 0);
        display.setScale(-size, size, -size);
        display.setTranslation(getSlotTranslation(slot)[0], getSlotTranslation(slot)[1], getSlotTranslation(slot)[2]);
        display.setItemDisplayTransform(ItemDisplay.ItemDisplayTransform.GUI);
        display.setBillboard(Display.Billboard.VERTICAL);
        display.setRotation(270 + (45 * 4), 0);
        display.setBrightness(new Display.Brightness(15, 15));
        display.update();
        return display;
    }

    public void removeEntities(){
        entities.values().forEach(HoloItemDisplay::remove);
    }

    public void setAir(){
        entities.values().forEach(display ->{
            display.setViewRange(0);
            display.update();
        });
    }

    public Recipe getRecipe(){
        return recipe;
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

    private Map<Integer, ItemStack> getShapedRecipeSlots(ShapedRecipe recipe) {
        Map<Integer, ItemStack> slotMap = new HashMap<>();
        String[] shape = recipe.getShape();
        Map<Character, ItemStack> ingredientMap = recipe.getIngredientMap();

        for(int row = 0; row < shape.length; row++){
            for(int col = 0; col < shape[row].length(); col++){
                char symbol = shape[row].charAt(col);
                ItemStack ingredient = ingredientMap.get(symbol);

                int slot = row * 3 + col + 1;

                if(ingredient != null && !ingredient.getType().isAir())
                    slotMap.put(slot, ingredient);
            }
        }

        slotMap.put(0, recipe.getResult());
        return slotMap;
    }

    private Map<Integer, ItemStack> getShapelessRecipeSlots(ShapelessRecipe shapelessRecipe){
        Map<Integer, ItemStack> slotMap = new HashMap<>();
        for(int i = 0; i < shapelessRecipe.getIngredientList().size(); i++)
            slotMap.put(i+1, shapelessRecipe.getIngredientList().get(i));

        slotMap.put(0, shapelessRecipe.getResult());
        return slotMap;
    }
}
