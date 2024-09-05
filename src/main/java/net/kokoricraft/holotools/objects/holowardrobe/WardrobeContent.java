package net.kokoricraft.holotools.objects.holowardrobe;

import com.google.gson.JsonObject;
import net.kokoricraft.holotools.HoloTools;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

public class WardrobeContent {
    private static final HoloTools plugin = JavaPlugin.getPlugin(HoloTools.class);
    private ItemStack helmet;
    private ItemStack chestplate;
    private ItemStack leggings;
    private ItemStack boots;

    public WardrobeContent(){}

    public WardrobeContent(ItemStack helmet, ItemStack chestplate, ItemStack leggings, ItemStack boots){
        this.helmet = helmet;
        this.chestplate = chestplate;
        this.leggings = leggings;
        this.boots = boots;
    }

    public void setHelmet(ItemStack helmet){
        this.helmet = helmet;
    }

    public void setChestplate(ItemStack chestplate){
        this.chestplate = chestplate;
    }

    public void setLeggings(ItemStack leggings){
        this.leggings = leggings;
    }

    public void setBoots(ItemStack boots){
        this.boots = boots;
    }

    public ItemStack getHelmet(){
        return helmet;
    }

    public ItemStack getChestplate(){
        return chestplate;
    }

    public ItemStack getLeggings() {
        return leggings;
    }

    public ItemStack getBoots() {
        return boots;
    }

    public JsonObject getToJson(){
        JsonObject jsonObject = new JsonObject();
        if(helmet != null && helmet.getType() != Material.AIR)
            jsonObject.addProperty("helmet", plugin.getUtils().serializeItemStack(helmet));

        if(chestplate != null && chestplate.getType() != Material.AIR)
            jsonObject.addProperty("chestplate", plugin.getUtils().serializeItemStack(chestplate));

        if(leggings != null && leggings.getType() != Material.AIR)
            jsonObject.addProperty("leggings", plugin.getUtils().serializeItemStack(leggings));

        if(boots != null && boots.getType() != Material.AIR)
            jsonObject.addProperty("boots", plugin.getUtils().serializeItemStack(boots));

        return jsonObject;
    }

    public void apply(Player player){
        player.getInventory().setHelmet(helmet);
        player.getInventory().setChestplate(chestplate);
        player.getInventory().setLeggings(leggings);
        player.getInventory().setBoots(boots);
        player.updateInventory();
    }

    public boolean isEmpty(){
        return isNull(helmet) && isNull(chestplate) && isNull(leggings) && isNull(boots);
    }

    private boolean isNull(ItemStack itemStack){
        return itemStack == null || itemStack.getType() == Material.AIR;
    }

    public static WardrobeContent fromJson(JsonObject jsonObject){
        WardrobeContent wardrobeContent = new WardrobeContent();

        if(jsonObject.has("helmet"))
            wardrobeContent.setHelmet(plugin.getUtils().deserializeItemStack(jsonObject.get("helmet").getAsString()));

        if(jsonObject.has("chestplate"))
            wardrobeContent.setChestplate(plugin.getUtils().deserializeItemStack(jsonObject.get("chestplate").getAsString()));

        if(jsonObject.has("leggings"))
            wardrobeContent.setLeggings(plugin.getUtils().deserializeItemStack(jsonObject.get("leggings").getAsString()));

        if(jsonObject.has("boots")){
            ItemStack loadedBoots = plugin.getUtils().deserializeItemStack(jsonObject.get("boots").getAsString());
            wardrobeContent.setBoots(loadedBoots);
        }

        return wardrobeContent;
    }

    public static WardrobeContent fromPlayer(Player player){
        WardrobeContent wardrobeContent = new WardrobeContent();

        wardrobeContent.setHelmet(player.getInventory().getHelmet());
        wardrobeContent.setChestplate(player.getInventory().getChestplate());
        wardrobeContent.setLeggings(player.getInventory().getLeggings());
        wardrobeContent.setBoots(player.getInventory().getBoots());

        return wardrobeContent;
    }
}
