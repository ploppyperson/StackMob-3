package uk.antiperson.stackmob.api;

import org.bukkit.entity.Entity;
import uk.antiperson.stackmob.GlobalValues;
import uk.antiperson.stackmob.StackMob;
import uk.antiperson.stackmob.services.BukkitService;

public class EntityManager {

    private BukkitService bukkitService;

    public EntityManager(StackMob stackMob) {
        bukkitService = stackMob.getBukkitService();
    }

    /**
     * A class that contains useful stacking tools.
     *
     * @param entity The entity stack to manipulate.
     * @return An instance of the StackedEntity class, to do above.
     */
    public StackedEntity getStackedEntity(Entity entity) {
        return new StackedEntity(entity, bukkitService);
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
     * Adds the appropriate bukkitService to add an entity as a stack.
     *
     * @param entity The entity to set this bukkitService to.
     */
    public void addNewStack(Entity entity) {
        bukkitService.setMetadata(entity, GlobalValues.METATAG, 1);
        bukkitService.setMetadata(entity, GlobalValues.NO_SPAWN_STACK, true);
    }

    /**
     * Adds the appropriate bukkitService to add an entity as a stack.
     *
     * @param entity The entity to set this bukkitService to.
     * @param size   The stack size of the stack.
     */
    public void addNewStack(Entity entity, int size) {
        bukkitService.setMetadata(entity, GlobalValues.METATAG, size);
        bukkitService.setMetadata(entity, GlobalValues.NO_SPAWN_STACK, true);
    }

}
