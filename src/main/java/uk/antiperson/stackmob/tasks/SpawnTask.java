package uk.antiperson.stackmob.tasks;

import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.metadata.FixedMetadataValue;
import uk.antiperson.stackmob.StackMob;
import uk.antiperson.stackmob.tools.extras.GlobalValues;

public class SpawnTask extends StackingTask {

    private LivingEntity entity;
    public SpawnTask(StackMob sm, LivingEntity entity){
        super(sm);
        this.entity = entity;
    }

    @Override
    public void run(){
        if(entity.hasMetadata(GlobalValues.NO_SPAWN_STACK) && entity.getMetadata(GlobalValues.NO_SPAWN_STACK).get(0).asBoolean()){
            entity.removeMetadata(GlobalValues.NO_SPAWN_STACK, getStackMob());
            return;
        }
        // Set metadata to we can stack.
        entity.setMetadata(GlobalValues.METATAG, new FixedMetadataValue(getStackMob(), 1));
        // Find nearby stacks to merge with.
        for(Entity nearby : entity.getNearbyEntities(getX(), getY(), getZ())){
            if(getStackMob().getLogic().attemptMerge(entity, nearby)){
                return;
            }
        }

        // A match was not found, so we will set the appropriate metadata.
        if(getStackMob().getCustomConfig().getInt("dont-stack-until") > 0){
            if(getStackMob().tools.notEnoughNearby(entity)){
                entity.setMetadata(GlobalValues.METATAG, new FixedMetadataValue(getStackMob(), GlobalValues.NOT_ENOUGH_NEAR));
            }
        }

        // Set other plugin traits.
        getStackMob().getHookManager().onEntityClone(entity);
        // Set noAI
        getStackMob().getTools().setAi(entity);
    }
}
