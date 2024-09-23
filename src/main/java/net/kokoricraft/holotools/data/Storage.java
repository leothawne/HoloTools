package net.kokoricraft.holotools.data;

import net.kokoricraft.holotools.objects.players.HoloPlayer;

import java.util.UUID;

public interface Storage {
    HoloPlayer loadPlayer(UUID uuid);
    void savePlayer(HoloPlayer holoPlayer);
}
