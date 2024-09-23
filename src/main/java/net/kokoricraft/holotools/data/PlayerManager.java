package net.kokoricraft.holotools.data;

import net.kokoricraft.holotools.HoloTools;
import net.kokoricraft.holotools.objects.players.HoloPlayer;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class PlayerManager {
    private final HoloTools plugin;
    private final Map<UUID, HoloPlayer> players = new HashMap<>();

    public PlayerManager(HoloTools plugin){
        this.plugin = plugin;
    }

    public HoloPlayer getPlayer(UUID uuid){
        return players.computeIfAbsent(uuid, target -> plugin.getDataManager().getStorage().loadPlayer(uuid));
    }

    public HoloPlayer getPlayer(Player player){
        return getPlayer(player.getUniqueId());
    }
}
