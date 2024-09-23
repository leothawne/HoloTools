package net.kokoricraft.holotools.data;

import com.google.gson.JsonObject;
import net.kokoricraft.holotools.objects.players.HoloPlayer;

import java.util.UUID;

public interface Storage {
    HoloPlayer loadPlayer(UUID uuid);
    void savePlayer(HoloPlayer holoPlayer);
    int getNextID();
    void saveHolo(int id, JsonObject jsonObject);
    JsonObject getHolo(int id);
}
