package uk.antiperson.stackmob.api;

import lombok.AllArgsConstructor;
import org.bukkit.entity.Entity;
import uk.antiperson.stackmob.GlobalValues;
import uk.antiperson.stackmob.services.BukkitService;

@AllArgsConstructor
public class EntityManager {

    private BukkitService metadata;

    /**
     * A class that contains useful stacking tools.
     *
     * @param entity The entity stack to manipulate.
     * @return An instance of the StackedEntity class, to do above.
     */
    public StackedEntity getStackedEntity(Entity entity) {
        return new StackedEntity(entity, metadata);
    }

    /**
     * Check if the entity is a stacked mob.
     *
     * @param entity Entity to check.
     * @return If is stacked or not.
     */
    public boolean isStackedEntity(Entity entity) {
        return entity.hasMetadata(GlobalValues.METATAG) && entity.getMetadata(GlobalValues.METATAG).get(0).asInt() > 1;
    }

    /**
     * Adds the appropriate metadata to add an entity as a stack.
     *
     * @param entity The entity to set this metadata to.
     */
    public void addNewStack(Entity entity) {
        entity.setMetadata(GlobalValues.METATAG, metadata.fixedMetadata(1));
        entity.setMetadata(GlobalValues.NO_SPAWN_STACK, metadata.fixedMetadata(true));
    }

    /**
     * Adds the appropriate metadata to add an entity as a stack.
     *
     * @param entity The entity to set this metadata to.
     * @param size   The stack size of the stack.
     */
    public void addNewStack(Entity entity, int size) {
        entity.setMetadata(GlobalValues.METATAG, metadata.fixedMetadata(size));
        entity.setMetadata(GlobalValues.NO_SPAWN_STACK, metadata.fixedMetadata(true));
    }

}
