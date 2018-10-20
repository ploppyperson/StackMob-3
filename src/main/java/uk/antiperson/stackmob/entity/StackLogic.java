package uk.antiperson.stackmob.entity;

import org.bukkit.entity.Entity;
import org.bukkit.metadata.FixedMetadataValue;
import uk.antiperson.stackmob.StackMob;
import uk.antiperson.stackmob.tools.GeneralTools;
import uk.antiperson.stackmob.tools.extras.GlobalValues;

public class StackLogic {

    private StackMob sm;
    public StackLogic(StackMob sm){
        this.sm = sm;
    }

    public boolean attemptMerge(Entity original, Entity nearby){
        if(original.getType() != nearby.getType()){
            return false;
        }
        if(GeneralTools.hasInvalidMetadata(nearby)){
            return false;
        }
        if(sm.getTools().notTaskSuitable(original) || sm.getTools().notTaskSuitable(nearby)){
            return false;
        }
        if(sm.getTools().notMatching(original, nearby)) {
            return false;
        }

        int nearbySize = nearby.getMetadata(GlobalValues.METATAG).get(0).asInt();
        int maxSize = sm.getCustomConfig().getInt("stack-max");
        if (sm.config.getCustomConfig().isInt("custom." + nearby.getType() + ".stack-max")) {
            maxSize =  sm.getCustomConfig().getInt("custom." + nearby.getType() + ".stack-max");
        }
        if(nearbySize == maxSize){
            return false;
        }

        int originalSize = 1;
        if(!(GeneralTools.hasInvalidMetadata(original))){
            originalSize = original.getMetadata(GlobalValues.METATAG).get(0).asInt();
        }

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

    public void sliceEntity(Entity entity){

    }

    public Entity duplicate(Entity original, int duplicateSize){
        return  null;
    }
}
