package uk.antiperson.stackmob.api;

import org.bukkit.Bukkit;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.metadata.FixedMetadataValue;
import uk.antiperson.stackmob.StackMob;
import uk.antiperson.stackmob.tools.extras.GlobalValues;

public class StackedEntity {

    private Entity entity;
    private StackMob sm;
    public StackedEntity(Entity entity, StackMob sm){
        this.entity = entity;
        this.sm = sm;
    }


    /**
     * Sets the stack size.
     * @return Returns the current size.
     */
    public int getSize(){
        return entity.getMetadata(GlobalValues.METATAG).get(0).asInt();
    }

    /**
     * Sets the stack size.
     * @param newSize The size that the current entity should have it's size changed to.
     */

    public void setSize(int newSize){
        entity.setMetadata(GlobalValues.METATAG, new FixedMetadataValue(sm, newSize));
    }

    /**
     * Entities can have special metadata added to them to prevent them from stacking.
     * @return Returns if this entity is currently ignored by the stacking task.
     */

    public boolean isStackingPrevented(){
        return entity.hasMetadata(GlobalValues.NO_STACK_ALL) &&
                entity.getMetadata(GlobalValues.NO_STACK_ALL).get(0).asBoolean();
    }

    /**
     * Entities can have special metadata added to them to prevent them from stacking.
     * @return Returns if this entity is currently ignored by the stacking task.
     */

    public boolean isStackingPreventedOnSpawn(){
        return entity.hasMetadata(GlobalValues.NO_SPAWN_STACK) &&
                entity.getMetadata(GlobalValues.NO_SPAWN_STACK).get(0).asBoolean();
    }

    /**
     * Entities can have special metadata added to them to prevent them from stacking.
     * @param value Boolean value for if entities should be excluded from stacking.
     */
    public void setPreventFromStacking(boolean value){
        entity.setMetadata(GlobalValues.NO_STACK_ALL, new FixedMetadataValue(sm, value));
    }

    /**
     * Entities can have special metadata added to them to prevent them from stacking on the spawn event only. Useful if special metadata needs to be added.
     * @param value Boolean value for if entities should be excluded from stacking on spawn.
     */
    public void setPreventStackingOnSpawn(boolean value){
        entity.setMetadata(GlobalValues.NO_SPAWN_STACK, new FixedMetadataValue(sm, value));
    }

    /**
     * Kills the entity, while only reducing the stack by one.
     */
    public void killSingleNaturally(){
        entity.setMetadata(GlobalValues.KILL_ONE_OFF, new FixedMetadataValue(sm, true));
        ((LivingEntity) entity).setHealth(0);
    }
}
