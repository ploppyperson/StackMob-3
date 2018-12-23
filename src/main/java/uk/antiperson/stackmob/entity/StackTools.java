package uk.antiperson.stackmob.entity;

import org.bukkit.entity.Entity;
import uk.antiperson.stackmob.tools.GlobalValues;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class StackTools {

    private static Map<UUID, Integer> currentEntities = new HashMap<>();
    private static Map<UUID, Integer> waiting = new HashMap<>();

    public static void addWaiting(Entity entity, int time){
        waiting.put(entity.getUniqueId(), time);
    }

    public static boolean hasValidStackData(Entity entity){
        return hasValidData(entity) &&
                getSize(entity) >= 1;
    }

    public static boolean hasSizeMoreThanOne(Entity entity){
        return hasValidData(entity) &&
                getSize(entity) > 1;
    }

    public static boolean hasNotEnoughNear(Entity entity){
        return hasValidData(entity) &&
                getSize(entity) == GlobalValues.NOT_ENOUGH_NEAR;
    }

    public static boolean hasValidData(Entity entity){
        return currentEntities.containsKey(entity.getUniqueId());
    }

    public static boolean hasValidMetadata(Entity entity, String metaTag){
        return entity.hasMetadata(metaTag) &&
                entity.getMetadata(metaTag).size() != 0;
    }

    public static boolean isWaiting(Entity entity){
        return waiting.containsKey(entity.getUniqueId());
    }

    public static void incrementWaiting(Entity entity){
        waiting.put(entity.getUniqueId(), getWaitingTime(entity) - 1);
        if(getWaitingTime(entity) == 0){
            waiting.remove(entity.getUniqueId());
        }
    }

    public static int getWaitingTime(Entity entity){
        return waiting.get(entity.getUniqueId());
    }

    public static int getSize(Entity entity){
        return currentEntities.get(entity.getUniqueId());
    }

    public static void setSize(Entity entity, int newSize){
        currentEntities.put(entity.getUniqueId(), newSize);
    }

    public static void removeSize(Entity entity){
        currentEntities.remove(entity.getUniqueId());
        removeTag(entity);
    }

    public static void makeSingle(Entity entity){
        setSize(entity, 1);
        removeTag(entity);
    }

    private static void removeTag(Entity entity){
        entity.setCustomNameVisible(false);
        entity.setCustomName(null);
    }

    public static Map<UUID, Integer> getEntries(){
        return currentEntities;
    }

}
