package net.kokoricraft.holotools.objects.holowardrobe;

import net.kokoricraft.holotools.HoloTools;
import net.kokoricraft.holotools.enums.HoloColors;
import net.kokoricraft.holotools.enums.HoloType;
import net.kokoricraft.holotools.interfaces.HoloBase;
import net.kokoricraft.holotools.objects.colors.HoloPanelsColors;
import net.kokoricraft.holotools.objects.halo.HaloSlot;
import net.kokoricraft.holotools.objects.halo.Holo;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.Map;

public class HoloWardrobe extends Holo implements HoloBase {
    private final HoloTools plugin = HoloTools.getInstance();
    private final Map<Integer, HoloWardrobeSlot> wardrobeSlots = new HashMap<>();
    private final HoloPanelsColors colors;
    public HoloWardrobe(Player player, ItemStack itemStack, Map<Integer, WardrobeContent> contentMap) {
        super(8, -1.5f, itemStack);
        this.player = player;
        this.colors = plugin.getHoloManager().getHoloColor(player, HoloType.HOLOWARDROBE);

        for(int key : slots.keySet()){
            HaloSlot slot = slots.get(key);
            slot.setColor(colors.getColor(key).unselected());

            HoloWardrobeSlot wardrobeSlot = new HoloWardrobeSlot(slot, contentMap.get(key), player);

            wardrobeSlots.put(key, wardrobeSlot);
        }
    }

    @Override
    public void onChangeSlot(int fromSlot, int toSlot){
        super.onChangeSlot(fromSlot, toSlot);

        HaloSlot from = slots.get(fromSlot);
        HaloSlot to = slots.get(toSlot);

        if(from != null){
            from.setColor(colors.getColor(fromSlot).unselected());
        }

        if(to != null){
            to.setColor(colors.getColor(toSlot).selected());
        }
    }

    @Override
    public void onClick(){
        super.onClick();
        int slot = getPlayerSlot();

        HoloWardrobeSlot wardrobeSlot = wardrobeSlots.get(slot);

        WardrobeContent saveArmor = WardrobeContent.fromPlayer(player);

        if(wardrobeSlot.getContent() == null && !saveArmor.isEmpty()){
            player.getInventory().setHelmet(null);
            player.getInventory().setChestplate(null);
            player.getInventory().setLeggings(null);
            player.getInventory().setBoots(null);
            player.updateInventory();
            wardrobeSlot.setContent(saveArmor);
            plugin.getDataManager().saveHoloWardrobe(itemStack, this, "save wardrobe armor");
        }else{
            WardrobeContent loadArmor = wardrobeSlot.getContent();
            if((loadArmor == null || loadArmor.isEmpty()) && saveArmor.isEmpty()) return;
            wardrobeSlot.setContent(saveArmor);
            if(loadArmor != null)
                loadArmor.apply(player);
            plugin.getDataManager().saveHoloWardrobe(itemStack, this, "swap wardrobe armor");
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
            wardrobeSlots.values().forEach(HoloWardrobeSlot::spawnContent);
        }else {
            this.shouldRemove = true;
            remove("remove set visible false wardrobe");
        }
    }

    @Override
    public boolean isVisible() {
        return visible;
    }

    @Override
    public void tick(){
        super.tick();
    }

    public Map<Integer, HoloWardrobeSlot> getWardrobeSlots(){
        return wardrobeSlots;
    }
}
