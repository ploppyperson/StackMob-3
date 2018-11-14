package uk.antiperson.stackmob.api;

import org.bukkit.entity.Entity;
import org.bukkit.metadata.FixedMetadataValue;
import uk.antiperson.stackmob.StackMob;
import uk.antiperson.stackmob.entity.StackTools;
import uk.antiperson.stackmob.tools.extras.GlobalValues;

public class StackedEntity {

    private Entity entity;
    private StackMob sm;
    public StackedEntity(Entity entity, StackMob sm){
        this.entity = entity;
        this.sm = sm;
    }

    /**
     * Gets the bukkit entity.
     * @return Returns the entity.
     */
    public Entity getEntity() {
        return entity;
    }

    /**
     * Sets the stack size.
     * @return Returns the current size.
     */
    public int getSize(){
        return StackTools.getSize(entity);
    }

    /**
     * Sets the stack size.
     * @param newSize The size that the current entity should have it's size changed to.
     */

    public void setSize(int newSize){
        StackTools.setSize(entity, newSize);
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
     * @deprecated This is no longer required.
     */
    @Deprecated
    public boolean isStackingPreventedOnSpawn(){
        return false;
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
     * @deprecated This is no longer required.
     */
    @Deprecated
    public void setPreventStackingOnSpawn(boolean value){

    }

    /**
     * Adds metadata so when the entity is killed, only one is removed from the stack.
     */
    public void setSingleDeath(){
        entity.setMetadata(GlobalValues.KILL_ONE_OFF, new FixedMetadataValue(sm, true));
    }
}
