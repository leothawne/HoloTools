package net.kokoricraft.holotools.objects.halocrafter;

import net.kokoricraft.holotools.HoloTools;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.*;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import java.util.HashMap;
import java.util.Map;

public class HaloCrafter {
    private final HoloTools plugin;
    private final Map<Integer, HaloCrafterSlot> slots = new HashMap<>();
    private final Player player;
    private boolean visible = false;
    private ItemCraft lasted_craft;
    private BukkitTask task;
    private int last_slot = -1;
    private final HaloHover hover;

    public HaloCrafter(HoloTools plugin, Player player){
        this.plugin = plugin;

        this.player = player;
        for(int i = 0; i < 8; i++){
            slots.put(i, new HaloCrafterSlot(i, player, this));
        }

        testSelected();
        hover = new HaloHover();
    }

    private void testSelected() {
        Map<Integer, ItemStack> map = new HashMap<>();
        map.put(1, new ItemStack(Material.DIRT));
        map.put(2, new ItemStack(Material.SPONGE));
        map.put(3, new ItemStack(Material.SADDLE));
        map.put(4, new ItemStack(Material.GOLDEN_HELMET));
        map.put(5, new ItemStack(Material.DIAMOND));
        map.put(6, new ItemStack(Material.STONE));

        slots.get(5).setSelected(new ItemCraft(map, new ItemStack(Material.DRAGON_EGG)));
    }

    public void setVisible(boolean visible){
        if(!this.visible && visible){
            initTask();
        }

        this.visible = visible;
        if(visible){
            slots.values().forEach(HaloCrafterSlot::spawnBackground);
            hover.spawn(player);
        }else {
            if(task != null){
                task.cancel();
            }
            slots.values().forEach(slot ->{
                ItemCraft itemCraft = slot.getSelected();
                if(itemCraft != null)
                    itemCraft.removeEntities();

                slot.removeBackground();
            });
            if(hover != null)
                hover.removeEntities();
        }
    }

    public void onChangeSlot(int fromSlot, int toSlot) {
        Bukkit.getScheduler().runTask(JavaPlugin.getPlugin(HoloTools.class), () -> {
            HaloCrafterSlot from = slots.get(fromSlot);
            HaloCrafterSlot to = slots.get(toSlot);
            if(fromSlot == -1){
                player.playSound(player, Sound.BLOCK_RESPAWN_ANCHOR_CHARGE, 0.1f, 10);
            }
            if(from != to && fromSlot != -1){
                player.playSound(player, Sound.ENTITY_CHICKEN_STEP, 0.1f, -2);
            }

            if (from != null) {
                from.setInCursor(false);
                if(fromSlot != 0)
                    from.setText("");
            }

            if (to != null) {
                to.setInCursor(true);

                if(toSlot != 0){
                    if(to.hasSelected()){
                        to.setText("Click para craftear este item");
                    }else {
                        to.setText("Click para guardar esta receta aqui.");
                    }
                }
            }

            if (toSlot == 0) {
                hover.setAir();
                return;
            }
            hover.setAir();

            if(to.hasSelected()){
                hover.setIngredients(to.getSelected().getIngredients());
                hover.setResult(to.getSelected().getResult());
            }else if(getLastedCraft() != null){
                hover.setIngredients(getLastedCraft().getIngredients());
                hover.setResult(getLastedCraft().getResult());
            }
            if(!to.hasSelected() && getLastedCraft() == null){
                hover.setAir();
            }
        });
    }


    public void setLastedCraft(ItemCraft itemCraft){
        this.lasted_craft = itemCraft;
    }

    public ItemCraft getLastedCraft(){
        return lasted_craft;
    }

    private void initTask(){
        task = new BukkitRunnable() {
            @Override
            public void run() {
                if(player == null){
                    task = null;
                    cancel();
                    return;
                }

                int slot = getPlayerSlot();

                if(last_slot != slot){
                    onChangeSlot(last_slot, slot);
                    last_slot = slot;
                }
            }
        }.runTaskTimerAsynchronously(JavaPlugin.getPlugin(HoloTools.class), 0, 2);
    }

    public int getPlayerSlot(){
        float yaw = (player.getLocation().getYaw() - 90 + 360) % 360;

        return (int) (((yaw + 22.5) / 45) % 8);
    }


    public void forceUpdate() {
        onChangeSlot(getPlayerSlot(), getPlayerSlot());
    }

    public boolean isVisible(){
        return visible;
    }

    public void onClick(){
        int slot = getPlayerSlot();
        if(slot == 0){
            plugin.getManager().openHaloCrafting(player);
            return;
        }

        HaloCrafterSlot haloSlot = slots.get(slot);
        if(!haloSlot.hasSelected() && getLastedCraft() != null && !player.isSneaking()){
            haloSlot.setSelected(getLastedCraft());
            return;
        }

        if(haloSlot.hasSelected() && player.isSneaking()){
            haloSlot.setSelected(null);
            forceUpdate();
            return;
        }

        if(haloSlot.hasSelected()){
            //Craftea v:

        }
    }
}
