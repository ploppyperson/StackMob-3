package uk.antiperson.stackmob.tasks;

import org.bukkit.entity.LivingEntity;
import org.bukkit.scheduler.BukkitRunnable;
import uk.antiperson.stackmob.StackMob;
import uk.antiperson.stackmob.entity.StackTools;
import uk.antiperson.stackmob.tools.GlobalValues;

public class SpawnTask extends BukkitRunnable {

    private LivingEntity entity;
    private StackMob sm;
    public SpawnTask(StackMob sm, LivingEntity entity){
        this.sm = sm;
        this.entity = entity;
    }

    @Override
    public void run(){
        if(StackTools.hasValidStackData(entity)){
            return;
        }
        if(sm.getHookManager().cantStack(entity)){
            return;
        }
        // Set metadata to we can stack.
        StackTools.setSize(entity,1);
        // Find nearby stacks to merge with, return if found.
        if(sm.getLogic().foundMatch(entity)){
            return;
        }

        // A match was not found, so we will set the appropriate metadata.
        if(sm.getCustomConfig().getInt("dont-stack-until") > 0){
            if(sm.getLogic().notEnoughNearby(entity)){
                StackTools.setSize(entity, GlobalValues.NOT_ENOUGH_NEAR);
            }
        }
        // Set traits.
        sm.getTools().setTraits(entity);
    }
}
