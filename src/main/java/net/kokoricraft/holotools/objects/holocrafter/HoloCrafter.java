package net.kokoricraft.holotools.objects.holocrafter;

import net.kokoricraft.holotools.HoloTools;
import net.kokoricraft.holotools.enums.HoloColors;
import net.kokoricraft.holotools.interfaces.HoloBase;
import net.kokoricraft.holotools.objects.halo.HaloSlot;
import net.kokoricraft.holotools.objects.halo.Holo;
import org.bukkit.entity.Player;
import org.bukkit.inventory.InventoryView;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.Recipe;

import java.util.HashMap;
import java.util.Map;

public class HoloCrafter extends Holo implements HoloBase {
    private final HoloTools plugin = HoloTools.getInstance();
    private final Map<Integer, HoloCrafterSlot> crafterSlots = new HashMap<>();
    private final HoloHover hover;
    private Recipe lasted_recipe;

    public HoloCrafter(Player player, Map<Integer, Recipe> recipeMap, ItemStack itemStack) {
        super(8, -1.5f, itemStack);
        this.player = player;

        for(int key : slots.keySet()){
            HaloSlot slot = slots.get(key);
            boolean second = key % 2 == 0;
            slot.setColor(second ? HoloColors.DARK.getColor() : HoloColors.WHITE.getColor());

            HoloCrafterSlot crafterSlot = new HoloCrafterSlot(slot, recipeMap.get(key), player, second);

            crafterSlots.put(key, crafterSlot);
        }

        hover = new HoloHover();
    }

    @Override
    public void onChangeSlot(int fromSlot, int toSlot) {
        super.onChangeSlot(fromSlot, toSlot);

        HaloSlot from = slots.get(fromSlot);
        HaloSlot to = slots.get(toSlot);

        if(from != null){
            HoloCrafterSlot holoCrafterSlot = crafterSlots.get(fromSlot);
            from.setColor(holoCrafterSlot.isSecond() ? HoloColors.DARK.getColor() : HoloColors.WHITE.getColor());
            if(fromSlot != 0){
                hover.setAir();
                holoCrafterSlot.setText(" ");
            }
        }

        if(to != null){
            HoloCrafterSlot holoCrafterSlot = crafterSlots.get(toSlot);
            to.setColor(holoCrafterSlot.isSecond() ? HoloColors.DARK_SELECTED.getColor() : HoloColors.WHITE_SELECTED.getColor());
            if(toSlot != 0){
                if(holoCrafterSlot.getRecipe() != null){
                    hover.setRecipe(holoCrafterSlot.getRecipe());
                    holoCrafterSlot.setText(plugin.getUtils().color(plugin.getLangManager().CRAFTER_CLICK_TO_CRAFT));
                }else if(lasted_recipe != null){
                    hover.setRecipe(lasted_recipe);
                    holoCrafterSlot.setText(plugin.getUtils().color(plugin.getLangManager().CRAFTER_CLICK_TO_SAVE));
                }
            }
        }

        if(toSlot == 0)
            hover.setAir();
    }

    @Override
    public void onClick() {
        super.onClick();
        int slot = getSlot();
        if(slot == 0){
            InventoryView view = player.openWorkbench(null, true);
            return;
        }

        HoloCrafterSlot crafterSlot = crafterSlots.get(slot);

        if(crafterSlot.getRecipe() != null && !player.isSneaking()){
            plugin.getCraftItemsUtils().craftItems(player, crafterSlot.getRecipe(), 1);
            return;
        }

        if(crafterSlot.getRecipe() != null && player.isSneaking()){
            crafterSlot.setRecipe(null);
            hover.setAir();
            plugin.getDataManager().saveHoloCrafter(itemStack, this, "remove recipe");
            if(lasted_recipe != null){
                crafterSlot.setText(plugin.getUtils().color(plugin.getLangManager().CRAFTER_CLICK_TO_SAVE));
            }else{
                crafterSlot.setText(" ");
            }

            return;
        }

        if(lasted_recipe != null && crafterSlot.getRecipe() == null){
            crafterSlot.setRecipe(lasted_recipe);
            hover.setRecipe(lasted_recipe);
            crafterSlot.setText(plugin.getUtils().color(plugin.getLangManager().CRAFTER_CLICK_TO_CRAFT));
            plugin.getDataManager().saveHoloCrafter(itemStack, this, "save recipe");
        }
    }

    @Override
    public int getSlot() {
        return getPlayerSlot();
    }

    @Override
    public void setVisible(boolean visible) {
        if(!this.visible && visible)
            plugin.getTickManager().addTickable(this);

        this.visible = visible;
        if(visible){
            spawn(player);
            hover.spawn(player);
            crafterSlots.values().forEach(HoloCrafterSlot::spawn);
        }else {
            this.shouldRemove = true;
            remove("holo crafter set visible false");

            if(hover != null)
                hover.removeEntities();
        }
    }

    @Override
    public boolean isVisible() {
        return super.visible;
    }

    @Override
    public void tick() {
        super.tick();
    }

    @Override
    public boolean shouldRemove() {
        return super.shouldRemove();
    }

    public Map<Integer, HoloCrafterSlot> getCrafterSlots(){
        return crafterSlots;
    }

    public void setLasted(Recipe recipe) {
        this.lasted_recipe = recipe;
        onChangeSlot(getPlayerSlot(), getPlayerSlot());
    }
}
