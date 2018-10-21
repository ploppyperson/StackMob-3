package uk.antiperson.stackmob.tasks;

import org.bukkit.World;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import uk.antiperson.stackmob.StackMob;
import uk.antiperson.stackmob.tools.GeneralTools;
import uk.antiperson.stackmob.tools.extras.GlobalValues;

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
            if(GeneralTools.hasNotEnoughNear(entity)) {
                if(getStackMob().getTools().notEnoughNearby(entity)){
                    continue;
                }
            }
            if(!(GeneralTools.hasValidStackData(entity))){
                continue;
            }
            if(getStackMob().getTools().notTaskSuitable(entity)){
                continue;
            }
            int stackSize = entity.getMetadata(GlobalValues.METATAG).get(0).asInt();
            if(getStackMob().getTools().checkIfMaximumSize(entity, stackSize)){
                continue;
            }
            for(Entity nearby : entity.getNearbyEntities(getX(), getY(), getZ())){
                if(!(getStackMob().getLogic().attemptMerge(entity, nearby))){
                    continue;
                }
                break;
            }
        }
    }
}
