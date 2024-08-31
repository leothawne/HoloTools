package net.kokoricraft.holotools.managers;

import net.kokoricraft.holotools.HoloTools;
import net.kokoricraft.holotools.interfaces.Tickable;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import java.util.ArrayList;
import java.util.List;

public class TickManager {
    private final HoloTools plugin;
    private final List<Tickable> tickableList = new ArrayList<>();
    private final List<Tickable> pendingRemovalList = new ArrayList<>();
    private BukkitTask task;
    public TickManager(HoloTools plugin){
        this.plugin = plugin;
    }

    public void addTickable(Tickable tickable){
        tickableList.add(tickable);
        init();
    }

    public void init(){
        if(!tickableList.isEmpty() && task != null && !task.isCancelled()) return;
        task = new BukkitRunnable() {
            @Override
            public void run() {
                if(tickableList.isEmpty()){
                    task.cancel();
                    task = null;
                    return;
                }

                for(Tickable tickable : tickableList){
                    if(tickable.shouldRemove()){
                        pendingRemovalList.add(tickable);
                        continue;
                    }

                    tickable.tick();
                }

                if(!pendingRemovalList.isEmpty()){
                    tickableList.removeAll(pendingRemovalList);
                    pendingRemovalList.clear();
                }
            }
        }.runTaskTimerAsynchronously(plugin, 0, 0);
    }

    public List<Tickable> getTickableList(){
        return tickableList;
    }
}
