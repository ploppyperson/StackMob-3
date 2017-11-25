package uk.antiperson.stackmob.api;

import lombok.AllArgsConstructor;
import org.bukkit.entity.Entity;
import uk.antiperson.stackmob.GlobalValues;
import uk.antiperson.stackmob.services.BukkitService;

@AllArgsConstructor
public class StackedEntity {

    private Entity entity;
    private BukkitService metadata;

    /**
     * Sets the stack size.
     *
     * @return Returns the current size.
     */
    public int getSize() {
        return entity.getMetadata(GlobalValues.METATAG).get(0).asInt();
    }

    /**
     * Sets the stack size.
     *
     * @param newSize The size that the current entity should have it's size changed to.
     */

    public void setSize(int newSize) {
        entity.setMetadata(GlobalValues.METATAG, metadata.fixedMetadata(newSize));
    }

    /**
     * Entities can have special metadata added to them to prevent them from stacking.
     *
     * @return Returns if this entity is currently ignored by the stacking task.
     */

    public boolean isStackingPrevented() {
        return entity.hasMetadata(GlobalValues.NO_STACK_ALL) &&
                entity.getMetadata(GlobalValues.NO_STACK_ALL).get(0).asBoolean();
    }

    /**
     * Entities can have special metadata added to them to prevent them from stacking.
     *
     * @return Returns if this entity is currently ignored by the stacking task.
     */

    public boolean isStackingPreventedOnSpawn() {
        return entity.hasMetadata(GlobalValues.NO_SPAWN_STACK) &&
                entity.getMetadata(GlobalValues.NO_SPAWN_STACK).get(0).asBoolean();
    }

    /**
     * Entities can have special metadata added to them to prevent them from stacking.
     *
     * @param value Boolean value for if entities should be excluded from stacking.
     */
    public void setPreventFromStacking(boolean value) {
        entity.setMetadata(GlobalValues.NO_STACK_ALL, metadata.fixedMetadata(value));
    }

    /**
     * Entities can have special metadata added to them to prevent them from stacking on the spawn event only. Useful if special metadata needs to be added.
     *
     * @param value Boolean value for if entities should be excluded from stacking on spawn.
     */
    public void setPreventStackingOnSpawn(boolean value) {
        entity.setMetadata(GlobalValues.NO_SPAWN_STACK, metadata.fixedMetadata(value));
    }

}
