package uk.antiperson.stackmob.entity;

import org.bukkit.Bukkit;
import org.bukkit.entity.Entity;
import org.bukkit.metadata.FixedMetadataValue;
import uk.antiperson.stackmob.StackMob;
import uk.antiperson.stackmob.api.StackedEntity;
import uk.antiperson.stackmob.api.events.EntityStackEvent;
import uk.antiperson.stackmob.tools.GeneralTools;
import uk.antiperson.stackmob.tools.extras.GlobalValues;

import java.util.HashSet;
import java.util.UUID;

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
        int nearbySize = nearby.getMetadata(GlobalValues.METATAG).get(0).asInt();
        int originalSize = original.getMetadata(GlobalValues.METATAG).get(0).asInt();
        if(nearbySize > originalSize && sm.config.getCustomConfig().getBoolean("big-priority")){
            Entity holder = nearby;
            nearby = original;
            original = holder;
        }

        // Continue to stack together
        int amountTotal = nearbySize + originalSize;
        if(amountTotal > maxSize){
            original.setMetadata(GlobalValues.METATAG, new FixedMetadataValue(sm, maxSize));
            nearby.setMetadata(GlobalValues.METATAG, new FixedMetadataValue(sm, amountTotal - maxSize));
        }else{
            original.setMetadata(GlobalValues.METATAG, new FixedMetadataValue(sm, amountTotal));
            sm.tools.onceStacked(nearby);
            nearby.remove();
        }
        return true;
    }

    public boolean notEnoughNearby(Entity original){
        if(sm.getCustomConfig().getInt("dont-stack-until") > 0){
            HashSet<UUID> entities = new HashSet<>();
            entities.add(original.getUniqueId());
            for(Entity nearby : original.getNearbyEntities(getX(), getY(), getZ())){
                if(original.getType() == nearby.getType()) {
                    if (GeneralTools.hasNotEnoughNear(nearby)) {
                        if (sm.getTools().notMatching(original, nearby)) {
                            continue;
                        }
                        entities.add(nearby.getUniqueId());
                    }
                }
            }
            if(entities.size() >= sm.config.getCustomConfig().getInt("dont-stack-until")){
                for(UUID uuid : entities){
                    Entity nearby = Bukkit.getEntity(uuid);
                    if(nearby == null){
                        entities.remove(uuid);
                        return true;
                    }else{
                        nearby.setMetadata(GlobalValues.METATAG, new FixedMetadataValue(sm, 1));
                    }
                }
            }else{
                return true;
            }
        }
        return false;
    }

    public boolean notSuitableForStacking(Entity entity){
        if(!(GeneralTools.hasValidStackData(entity))){
            return true;
        }
        if(entity.isDead()){
            return true;
        }
        if(GeneralTools.hasValidMetadata(entity, GlobalValues.NO_STACK_ALL) &&
                entity.getMetadata(GlobalValues.NO_STACK_ALL).get(0).asBoolean()){
            return true;
        }
        int stackSize = entity.getMetadata(GlobalValues.METATAG).get(0).asInt();
        return (getMaxSize(entity) == stackSize);
    }

    private int getMaxSize(Entity entity){
        int maxStackSize = sm.getCustomConfig().getInt("stack-max");
        if (sm.config.getCustomConfig().isInt("custom." + entity.getType() + ".stack-max")) {
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
