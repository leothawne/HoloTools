package net.kokoricraft.holotools.data;

import net.kokoricraft.holotools.HoloTools;
import net.kokoricraft.holotools.enums.StorageMode;
import net.kokoricraft.holotools.objects.NekoConfig;
import net.kokoricraft.holotools.objects.players.HoloPlayer;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class YamlStorage implements Storage{
    protected final HoloTools plugin = HoloTools.getInstance();
    private final Map<UUID, NekoConfig> player_configs = new HashMap<>();
    private final NekoConfig global_config = new NekoConfig("data.yml", plugin);

    @Override
    public HoloPlayer loadPlayer(UUID uuid) {
        HoloPlayer holoPlayer;

        if(plugin.getConfigManager().STORAGE_MODE == StorageMode.PLAYER){
            holoPlayer = new HoloPlayer(uuid);
            NekoConfig config = player_configs.getOrDefault(uuid, new NekoConfig(uuid.toString() + ".yml", plugin));
            
        }else {
            holoPlayer = new HoloPlayer(uuid);
        }

        return holoPlayer;
    }

    @Override
    public void savePlayer(HoloPlayer holoPlayer) {

    }
}
