package uk.antiperson.stackmob.tools;

import org.bukkit.Bukkit;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

import java.util.HashSet;

public class WorldTools {

    public static HashSet<Entity> getLoadedEntities() {
        HashSet<Entity> loadedEntities = new HashSet<>();
        for(Player player : Bukkit.getOnlinePlayers()){
            loadedEntities.addAll(player.getNearbyEntities(20, 20, 20));
        }
        return loadedEntities;
    }
}
