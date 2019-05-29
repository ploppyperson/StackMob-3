package uk.antiperson.stackmob.tasks;

import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Mob;
import org.bukkit.scheduler.BukkitRunnable;
import uk.antiperson.stackmob.api.IStackMob;
import uk.antiperson.stackmob.api.entity.StackTools;

public class StackTask extends BukkitRunnable {

    private World world;
    private IStackMob sm;
    public StackTask(IStackMob sm, World world){
        this.sm = sm;
        this.world = world;
    }

    @Override
    public void run(){
        for(Entity entity : world.getLivingEntities()){
            if(!(entity instanceof Mob)){
                continue;
            }
            if(sm.getLogic().incrementWaiting(entity)){
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
