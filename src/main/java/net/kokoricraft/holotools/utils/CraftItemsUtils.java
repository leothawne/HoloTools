package net.kokoricraft.holotools.utils;

import net.kokoricraft.holotools.HoloTools;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CraftItemsUtils {

    private final HoloTools plugin;

    public CraftItemsUtils(HoloTools plugin){
        this.plugin = plugin;
    }

    public void craftItems(Player player, Recipe recipe, int amount){
        List<ItemStack> ingredients = new ArrayList<>();

        if(recipe instanceof ShapedRecipe shapedRecipe){
            ingredients = getShapedIngredients(shapedRecipe);
        }

        if(recipe instanceof ShapelessRecipe shapelessRecipe){
            ingredients = getShapelessIngredients(shapelessRecipe);
        }

        int max = getMaxCrafts(player, ingredients);

        if(max == 0 || ingredients.isEmpty()) return;

        if(amount != -1)
            max = Math.min(max, amount);

        for(ItemStack ingredient : ingredients){
            ingredient.setAmount(ingredient.getAmount() * max);
            player.getInventory().removeItem(ingredient);
            //Bukkit.broadcastMessage(String.format("remove: %s, amount: %s", ingredient.getType().name(), ingredient.getAmount()));
        }

        ItemStack result = recipe.getResult().clone();
        result.setAmount(result.getAmount() * max);

        int remainingResult = result.getAmount();

        while(remainingResult > 0){
            ItemStack currentResultStack = new ItemStack(result.getType(), Math.min(remainingResult, result.getMaxStackSize()));
            remainingResult -= currentResultStack.getAmount();
            giveItemToPlayer(player, currentResultStack);
            //Bukkit.broadcastMessage(String.format("give: %s, amount: %s", currentResultStack.getType().name(), currentResultStack.getAmount()));
        }
    }

    public void giveItemToPlayer(Player player, ItemStack itemStack) {
        if(itemStack == null) return;

        Inventory inventory = player.getInventory();
        HashMap<Integer, ItemStack> remainingItems = inventory.addItem(itemStack);

        if(remainingItems.isEmpty()) return;

        for(ItemStack item : remainingItems.values())
            player.getWorld().dropItem(player.getLocation(), item);
    }

    private int getMaxCrafts(Player player, List<ItemStack> ingredients){
        int crafts = Integer.MAX_VALUE;

        for(ItemStack ingredient : ingredients){
            if(ingredient != null && ingredient.getType() != Material.AIR){
                int totalAmountInInventory = 0;

                for(ItemStack slot : player.getInventory().getContents()){
                    if(slot != null && slot.isSimilar(ingredient))
                        totalAmountInInventory += slot.getAmount();
                }

                int ingredientCrafts = totalAmountInInventory / ingredient.getAmount();

                crafts = Math.min(crafts, ingredientCrafts);
            }
        }

        return crafts == Integer.MAX_VALUE ? 0 : crafts;
    }


    private int getFreeSpace(Player player, ItemStack result){
        int free_space = 0;
        for(int i = 0; i < 36; i++){
            ItemStack slot = player.getInventory().getItem(i);
            if(slot == null || slot.getType() == Material.AIR){
                free_space += result.getMaxStackSize();
                continue;
            }

            if(slot.isSimilar(result)){
                free_space += result.getMaxStackSize() - slot.getAmount();
            }
        }

        return free_space;
    }

    private List<ItemStack> getShapedIngredients(ShapedRecipe recipe){
        Map<ItemStack, Integer> ingredientCountMap = new HashMap<>();
        List<ItemStack> ingredients = new ArrayList<>();

        for(ItemStack ingredient : recipe.getIngredientMap().values()){
            if(ingredient != null) {
                boolean found = false;

                for (ItemStack existing : ingredientCountMap.keySet()){
                    if (existing.isSimilar(ingredient)) {
                        ingredientCountMap.put(existing, ingredientCountMap.get(existing) + 1);
                        found = true;
                        break;
                    }
                }
                if(!found)
                    ingredientCountMap.put(ingredient.clone(), 1);
            }
        }

        for(Map.Entry<ItemStack, Integer> entry : ingredientCountMap.entrySet()) {
            ItemStack ingredient = entry.getKey().clone();
            ingredient.setAmount(entry.getValue());
            ingredients.add(ingredient);
        }

        return ingredients;
    }

    private List<ItemStack> getShapelessIngredients(ShapelessRecipe recipe){
        return recipe.getIngredientList();
    }
}
