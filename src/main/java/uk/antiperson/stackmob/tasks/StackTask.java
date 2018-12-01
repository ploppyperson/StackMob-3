package uk.antiperson.stackmob.tasks;

import org.bukkit.World;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import uk.antiperson.stackmob.StackMob;
import uk.antiperson.stackmob.entity.StackTools;

public class StackTask extends BukkitRunnable {

    private World world;
    private StackMob sm;
    public StackTask(StackMob sm, World world){
        this.sm = sm;
        this.world = world;
    }

    @Override
    public void run(){
        for(Entity entity : world.getLivingEntities()){
            if(entity instanceof ArmorStand || entity instanceof Player){
                continue;
            }
            if(StackTools.hasNotEnoughNear(entity)) {
                if(sm.getLogic().notEnoughNearby(entity)){
                    continue;
                }
            }
            if(sm.getLogic().notSuitableForStacking(entity)){
                continue;
            }
            sm.getLogic().foundMatch(entity);
        }
    }
}
