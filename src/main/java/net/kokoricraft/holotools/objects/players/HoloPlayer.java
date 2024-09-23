package net.kokoricraft.holotools.objects.players;

import com.google.gson.JsonObject;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class HoloPlayer {
    private final UUID uuid;
    private final Map<String, JsonObject> data = new HashMap<>();

    public HoloPlayer(UUID uuid){
        this.uuid = uuid;
    }

    public void setData(String id, JsonObject jsonObject){
        this.data.put(id, jsonObject);
    }

    public Map<String, JsonObject> getData(){
        return data;
    }

    public UUID getUUID(){
        return uuid;
    }
}
