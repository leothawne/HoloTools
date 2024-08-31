package net.kokoricraft.holotools.version;


import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.List;

public interface Compat {
    HoloTextDisplay createTextDisplay(List<Player> players, Location location, float yaw, float pitch);
    HoloItemDisplay createItemDisplay(List<Player> players, Location location, float yaw, float pitch);
}
