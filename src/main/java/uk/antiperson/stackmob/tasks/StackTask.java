package uk.antiperson.stackmob.tasks;

import org.bukkit.World;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import uk.antiperson.stackmob.StackMob;
import uk.antiperson.stackmob.tools.StackTools;

public class StackTask extends StackingTask {

    private World world;
    public StackTask(StackMob sm, World world){
        super(sm);
        this.world = world;
    }

    @Override
    public void run(){
        for(Entity entity : world.getLivingEntities()){
            if(entity instanceof ArmorStand || entity instanceof Player){
                continue;
            }
            if(getStackMob().getStackTools().hasNotEnoughNear(entity)) {
                if(getStackMob().getLogic().notEnoughNearby(entity)){
                    continue;
                }
            }
            if(getStackMob().getLogic().notSuitableForStacking(entity)){
                continue;
            }
            getStackMob().getLogic().foundMatch(entity);
        }
    }
}
