package uk.antiperson.stackmob.tasks;

import org.bukkit.entity.LivingEntity;
import uk.antiperson.stackmob.StackMob;
import uk.antiperson.stackmob.entity.StackTools;
import uk.antiperson.stackmob.tools.extras.GlobalValues;

public class SpawnTask extends StackingTask {

    private LivingEntity entity;
    public SpawnTask(StackMob sm, LivingEntity entity){
        super(sm);
        this.entity = entity;
    }

    @Override
    public void run(){
        if(StackTools.hasValidStackData(entity)){
            return;
        }
        // Set metadata to we can stack.
        StackTools.setSize(entity,1);
        // Find nearby stacks to merge with, return if found.
        if(getStackMob().getLogic().foundMatch(entity)){
            return;
        }

        // A match was not found, so we will set the appropriate metadata.
        if(getStackMob().getCustomConfig().getInt("dont-stack-until") > 0){
            if(getStackMob().getLogic().notEnoughNearby(entity)){
                StackTools.setSize(entity, GlobalValues.NOT_ENOUGH_NEAR);
            }
        }

        // Set traits.
        getStackMob().getTools().setTraits(entity);
    }
}
