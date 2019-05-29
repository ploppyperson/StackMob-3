package uk.antiperson.stackmob.tasks;

import org.bukkit.Bukkit;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Mob;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import uk.antiperson.stackmob.api.IStackMob;
import uk.antiperson.stackmob.api.compat.PluginCompat;
import uk.antiperson.stackmob.compat.hooks.ProtocolLibHook;
import uk.antiperson.stackmob.api.entity.StackTools;

import java.util.HashSet;
import java.util.List;

public class ShowTagTask extends BukkitRunnable {

    public IStackMob sm;
    private int x;
    private int y;
    private int z;
    public ShowTagTask(IStackMob sm){
        this.sm = sm;
        x = sm.getCustomConfig().getInt("tag.show-player-nearby.x");
        y = sm.getCustomConfig().getInt("tag.show-player-nearby.y");
        z = sm.getCustomConfig().getInt("tag.show-player-nearby.z");
    }

    @Override
    public void run(){
        ProtocolLibHook plh = (ProtocolLibHook) sm.getHookManager().getHook(PluginCompat.PROCOTOLLIB);
        for(Player player : Bukkit.getOnlinePlayers()){
            // Get all entities in range that the tag can be shown for.
            HashSet<Entity> entities = new HashSet<>();
            for(Entity entity : player.getNearbyEntities(x, y, z)) {
                if(!(entity instanceof Mob)) {
                    continue;
                }
                if(!(StackTools.hasSizeMoreThanOne(entity))){
                    continue;
                }
                plh.sendPacket(player, entity, true);
                entities.add(entity);
            }
            // Prevent showing of tags for entities not in range.
            List<Entity> entities1 = player.getNearbyEntities(x * 1.5, y * 1.5, z * 1.5);
            entities1.removeAll(entities);
            for(Entity entity : entities1){
                if(StackTools.hasValidStackData(entity)){
                    plh.sendPacket(player, entity, false);
                }
            }
        }
    }
}
