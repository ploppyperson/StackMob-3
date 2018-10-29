package uk.antiperson.stackmob.tools;

import org.bukkit.entity.Entity;
import uk.antiperson.stackmob.StackMob;
import uk.antiperson.stackmob.tools.extras.GlobalValues;

public class StackTools {

    private StackMob sm;
    public StackTools(StackMob sm){
        this.sm = sm;
    }

    public boolean hasValidStackData(Entity entity){
        return hasValidData(entity) &&
                getSize(entity) >= 1;
    }

    public boolean hasNotEnoughNear(Entity entity){
        return hasValidData(entity) &&
                getSize(entity) == -1;
    }

    public boolean hasValidData(Entity entity){
        return sm.getStorageManager().getAmounts().containsKey(entity.getUniqueId());
    }

    public static boolean hasValidMetadata(Entity entity, String metaTag){
        return entity.hasMetadata(metaTag) &&
                entity.getMetadata(metaTag).size() != 0;
    }

    public int getSize(Entity entity){
        return sm.getStorageManager().getAmounts().get(entity.getUniqueId());
    }

    public void setSize(Entity entity, int newSize){
        sm.getStorageManager().getAmounts().put(entity.getUniqueId(), newSize);
    }

    public void removeSize(Entity entity){
        sm.getStorageManager().getAmounts().remove(entity.getUniqueId());
    }

}
