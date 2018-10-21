package uk.antiperson.stackmob.tasks;

import org.bukkit.Bukkit;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import uk.antiperson.stackmob.StackMob;
import uk.antiperson.stackmob.compat.PluginCompat;
import uk.antiperson.stackmob.compat.hooks.ProtocolLibHook;
import uk.antiperson.stackmob.tools.GeneralTools;

import java.util.HashSet;
import java.util.List;

public class ShowTagNearby extends BukkitRunnable {

    public StackMob sm;
    private int x;
    private int y;
    private int z;
    public ShowTagNearby(StackMob sm){
        this.sm = sm;
        x = sm.getCustomConfig().getInt("tag.show-player-nearby.x");
        y = sm.getCustomConfig().getInt("tag.show-player-nearby.y");
        z = sm.getCustomConfig().getInt("tag.show-player-nearby.z");
    }

    @Override
    public void run(){
        ProtocolLibHook plh = (ProtocolLibHook) sm.hookManager.getHook(PluginCompat.PROCOTOLLIB);
        for(Player player : Bukkit.getOnlinePlayers()){
            List<Entity> entities = player.getNearbyEntities(x, y, z);
            for(Entity entity : entities) {
                if(!(entity instanceof LivingEntity)) {
                    continue;
                }
                if(!(GeneralTools.hasValidStackData(entity))){
                    continue;
                }
                plh.sendPacket(player, entity, true);
            }
            HashSet<Entity> entities1 = sm.worldTools.getLoadedEntitiesNearby(player);
            entities1.removeAll(entities);
            for(Entity entity : entities1){
                plh.sendPacket(player, entity, false);
            }
        }
    }
}
