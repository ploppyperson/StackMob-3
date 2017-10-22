package uk.antiperson.stackmob.api;

import org.bukkit.entity.Entity;
import org.bukkit.metadata.FixedMetadataValue;
import uk.antiperson.stackmob.StackMob;
import uk.antiperson.stackmob.tools.extras.GlobalValues;

public class EntityManager {

    private StackMob sm;
    public EntityManager (StackMob sm){
        this.sm = sm;
    }

    /**
     * A class that contains useful stacking tools.
     * @param entity The entity stack to manipulate.
     * @return An instance of the StackedEntity class, to do above.
     */
    public StackedEntity getStackedEntity(Entity entity){
        return new StackedEntity(entity, sm);
    }

    /**
     * Check if the entity is a stacked mob.
     * @param entity Entity to check.
     * @return If is stacked or not.
     */
    public boolean isStackedEntity(Entity entity){
        return entity.hasMetadata(GlobalValues.METATAG) && entity.getMetadata(GlobalValues.METATAG).get(0).asInt() > 1;
    }

    /**
     * Adds the appropriate metadata to add an entity as a stack.
     * @param entity The entity to set this metadata to.
     */
    public void addNewStack(Entity entity){
        entity.setMetadata(GlobalValues.METATAG, new FixedMetadataValue(sm, 1));
        entity.setMetadata(GlobalValues.NO_SPAWN_STACK, new FixedMetadataValue(sm, true));
    }

    /**
     * Adds the appropriate metadata to add an entity as a stack.
     * @param entity The entity to set this metadata to.
     * @param size The stack size of the stack.
     */
    public void addNewStack(Entity entity, int size){
        entity.setMetadata(GlobalValues.METATAG, new FixedMetadataValue(sm, size));
        entity.setMetadata(GlobalValues.NO_SPAWN_STACK, new FixedMetadataValue(sm, true));
    }

    /**
     * Adds metadata to stop the entity from becoming a stack on the CreatureSpawnEvent.
     * @param entity Entity to set the metadata in.
     */
    public void preventFromStacking(Entity entity){
        entity.setMetadata(GlobalValues.NO_SPAWN_STACK, new FixedMetadataValue(sm, true));
    }
}
