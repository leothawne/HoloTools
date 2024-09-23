package net.kokoricraft.holotools.data.types;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import net.kokoricraft.holotools.HoloTools;
import net.kokoricraft.holotools.data.Storage;
import net.kokoricraft.holotools.enums.StorageMode;
import net.kokoricraft.holotools.objects.NekoConfig;
import net.kokoricraft.holotools.objects.players.HoloPlayer;
import org.bukkit.configuration.ConfigurationSection;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class YamlStorage implements Storage {
    protected final HoloTools plugin = HoloTools.getInstance();
    private final Map<UUID, NekoConfig> player_configs = new HashMap<>();
    private final NekoConfig global_config = new NekoConfig("data.yml", plugin);

    @Override
    public HoloPlayer loadPlayer(UUID uuid) {
        HoloPlayer holoPlayer;

        if(plugin.getConfigManager().STORAGE_MODE == StorageMode.PLAYER){
            holoPlayer = new HoloPlayer(uuid);
            NekoConfig config = player_configs.getOrDefault(uuid, new NekoConfig("userdata/"+uuid.toString() + ".yml", plugin));
            ConfigurationSection section = config.getConfigurationSection("holos");

            if(section != null){
                for(String id : section.getKeys(false)){
                    String stringJson = section.getString(id);
                    assert stringJson != null;
                    JsonObject jsonObject = JsonParser.parseString(stringJson).getAsJsonObject();
                    holoPlayer.setData(id, jsonObject);
                }
            }

            player_configs.put(uuid, config);
        }else {
            holoPlayer = new HoloPlayer(uuid);
        }

        return holoPlayer;
    }

    @Override
    public void savePlayer(HoloPlayer holoPlayer) {
        UUID uuid = holoPlayer.getUUID();

        if(plugin.getConfigManager().STORAGE_MODE == StorageMode.PLAYER){
            Map<String, JsonObject> data = holoPlayer.getData();

            NekoConfig config = player_configs.getOrDefault(uuid, new NekoConfig("userdata/"+uuid.toString() + ".yml", plugin));

            for(Map.Entry<String, JsonObject> entry : data.entrySet()){
                String id = entry.getKey();
                JsonObject jsonObject = entry.getValue();

                config.set("holos."+id, jsonObject.toString());
            }

            config.saveConfig();
            player_configs.put(uuid, config);
        }
    }

    @Override
    public int getNextID() {
        int id = global_config.getInt("id", 0) + 1;
        global_config.set("id", id);
        global_config.saveConfig();

        return id;
    }

    @Override
    public void saveHolo(int id, JsonObject jsonObject) {
        global_config.set("holos."+id, jsonObject.toString());
        global_config.saveConfig();
    }

    @Override
    public JsonObject getHolo(int id) {
        String stringJson = global_config.getString("holos."+id);
        if(stringJson == null) return new JsonObject();

        return JsonParser.parseString(stringJson).getAsJsonObject();
    }
}
