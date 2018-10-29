package uk.antiperson.stackmob.entity;

import org.bukkit.entity.Entity;
import org.bukkit.metadata.FixedMetadataValue;
import uk.antiperson.stackmob.StackMob;
import uk.antiperson.stackmob.api.StackedEntity;
import uk.antiperson.stackmob.api.events.EntityStackEvent;
import uk.antiperson.stackmob.tools.StackTools;
import uk.antiperson.stackmob.tools.extras.GlobalValues;

import java.util.HashSet;
import java.util.List;

public class StackLogic {

    private StackMob sm;
    private double xRadius;
    private double yRadius;
    private double zRadius;
    public StackLogic(StackMob sm){
        this.sm = sm;
        xRadius = sm.getCustomConfig().getDouble("check-area.x");
        yRadius = sm.getCustomConfig().getDouble("check-area.y");
        zRadius = sm.getCustomConfig().getDouble("check-area.z");
    }

    public boolean foundMatch(Entity original){
        for(Entity nearby : original.getNearbyEntities(getX(), getY(), getZ())){
            if(original.getType() != nearby.getType()){
                continue;
            }
            if(notSuitableForStacking(nearby)){
                continue;
            }
            if(sm.getTools().notMatching(original, nearby)) {
                continue;
            }
            if(callEvent(original, nearby)){
                continue;
            }
            if(attemptMerge(original, nearby)){
                return true;
            }
        }
        return false;
    }

    public boolean attemptMerge(Entity original, Entity nearby){
        int maxSize = getMaxSize(original);
        int nearbySize = sm.getStackTools().getSize(nearby);
        int originalSize = sm.getStackTools().getSize(original);
        if(nearbySize > originalSize && sm.getCustomConfig().getBoolean("big-priority")){
            Entity holder = nearby;
            nearby = original;
            original = holder;
        }

        // Continue to stack together
        int amountTotal = nearbySize + originalSize;
        if(amountTotal > maxSize){
            sm.getStackTools().setSize(original, maxSize);
            sm.getStackTools().setSize(nearby,amountTotal - maxSize);
        }else{
            sm.getStackTools().setSize(original, amountTotal);
            sm.tools.onceStacked(nearby);
            nearby.remove();
        }
        return true;
    }

    public boolean notEnoughNearby(Entity original){
        int dontStackTill = sm.getCustomConfig().getInt("dont-stack-until");
        if(dontStackTill > 0){
            List<Entity> nearbyEntities = original.getNearbyEntities(getX(), getY(), getZ());
            if(nearbyEntities.size() < dontStackTill &&
                    nearbyEntities.stream().noneMatch(entity -> sm.getStackTools().hasValidStackData(entity))){
                return true;
            }
            HashSet<Entity> entities = new HashSet<>();
            for(Entity nearby : nearbyEntities){
                if(original.getType() != nearby.getType()){
                    continue;
                }
                if(!(sm.getStackTools().hasValidData(nearby))){
                    continue;
                }
                if(sm.getTools().notMatching(original, nearby)){
                    continue;
                }
                if(sm.getStackTools().hasValidStackData(nearby)){
                    sm.getStackTools().setSize(original, 1);
                    return false;
                }
                entities.add(nearby);
            }
            entities.add(original);
            if(entities.size() < dontStackTill){
                return true;
            }
            for(Entity entity : entities){
                sm.getStackTools().setSize(entity,1);
            }
        }
        return false;
    }

    public boolean notSuitableForStacking(Entity entity){
        if(!(sm.getStackTools().hasValidStackData(entity))){
            return true;
        }
        if(entity.isDead()){
            return true;
        }
        if(StackTools.hasValidMetadata(entity, GlobalValues.NO_STACK_ALL) &&
                entity.getMetadata(GlobalValues.NO_STACK_ALL).get(0).asBoolean()){
            return true;
        }
        int stackSize = sm.getStackTools().getSize(entity);
        return (getMaxSize(entity) == stackSize);
    }

    private int getMaxSize(Entity entity){
        int maxStackSize = sm.getCustomConfig().getInt("stack-max");
        if (sm.getCustomConfig().isInt("custom." + entity.getType() + ".stack-max")) {
            maxStackSize =  sm.getCustomConfig().getInt("custom." + entity.getType() + ".stack-max");
        }
        return maxStackSize;
    }

    public void sliceEntity(Entity entity){

    }

    public Entity duplicate(Entity original, int duplicateSize){
        return  null;
    }

    private double getX(){
        return xRadius;
    }

    private double getY() {
        return yRadius;
    }

    private double getZ() {
        return zRadius;
    }

    public boolean callEvent(Entity original, Entity nearby){
        StackedEntity entity1 = new StackedEntity(original, sm);
        StackedEntity entity2 = new StackedEntity(nearby, sm);
        EntityStackEvent event = new EntityStackEvent(entity1, entity2);
        sm.getServer().getPluginManager().callEvent(event);
        return event.isCancelled();
    }
}
