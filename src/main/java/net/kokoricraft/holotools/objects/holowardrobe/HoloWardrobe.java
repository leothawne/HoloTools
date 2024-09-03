package net.kokoricraft.holotools.objects.holowardrobe;

import net.kokoricraft.holotools.HoloTools;
import net.kokoricraft.holotools.enums.HoloColors;
import net.kokoricraft.holotools.interfaces.HoloBase;
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
    public HoloWardrobe(Player player, ItemStack itemStack, Map<Integer, WardrobeContent> contentMap) {
        super(8, -1.5f, itemStack);
        this.player = player;

        for(int key : slots.keySet()){
            HaloSlot slot = slots.get(key);
            boolean second = key % 2 == 0;
            slot.setColor(second ? HoloColors.DARK.getColor() : HoloColors.WHITE.getColor());

            HoloWardrobeSlot wardrobeSlot = new HoloWardrobeSlot(slot, contentMap.get(key), player, second);

            wardrobeSlots.put(key, wardrobeSlot);
        }
    }

    @Override
    public void onChangeSlot(int fromSlot, int toSlot){
        super.onChangeSlot(fromSlot, toSlot);

        HaloSlot from = slots.get(fromSlot);
        HaloSlot to = slots.get(toSlot);

        if(from != null){
            HoloWardrobeSlot holoCrafterSlot = wardrobeSlots.get(fromSlot);
            from.setColor(holoCrafterSlot.isSecond() ? HoloColors.DARK.getColor() : HoloColors.WHITE.getColor());

        }

        if(to != null){
            HoloWardrobeSlot holoCrafterSlot = wardrobeSlots.get(toSlot);
            to.setColor(holoCrafterSlot.isSecond() ? HoloColors.DARK_SELECTED.getColor() : HoloColors.WHITE_SELECTED.getColor());
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
