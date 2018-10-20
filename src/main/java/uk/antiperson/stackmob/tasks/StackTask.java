package uk.antiperson.stackmob.tasks;

import org.bukkit.World;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.metadata.FixedMetadataValue;
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
            if(getStackMob().getTools().notTaskSuitable(entity)){
                continue;
            }
            if(!(GeneralTools.hasInvalidMetadata(entity, GlobalValues.NOT_ENOUGH_NEAR))
                    && entity.getMetadata(GlobalValues.NOT_ENOUGH_NEAR).get(0).asBoolean()) {
                getStackMob().getTools().notEnoughNearby(entity);
            }
            // don't need a check because of the entity might have Not-enough-near
            int maxSize = getMaxStackSize();
            if(getStackMob().getCustomConfig().isInt("custom." + entity.getType() + ".stack-max")){
                maxSize = getStackMob().getCustomConfig().getInt("custom." + entity.getType() + ".stack-max");
            }
            // If the first entity has metatag, check if it is maxSize to save performance
            if(!(GeneralTools.hasInvalidMetadata(entity))){
                if(entity.getMetadata(GlobalValues.METATAG).get(0).asInt() == maxSize){
                    continue;
                }
            }

            for(Entity nearby : entity.getNearbyEntities(getX(), getY(), getZ())){
                //Checks on nearby
                if(entity.getType() != nearby.getType()){
                    continue;
                }

                if(GeneralTools.hasInvalidMetadata(nearby)){
                    continue;
                }

                if(getStackMob().getTools().notTaskSuitable(nearby)){
                    continue;
                }

                if(!(getStackMob().getLogic().attemptMerge(entity, nearby))){
                    continue;
                }
                break;
            }
        }
    }
}
