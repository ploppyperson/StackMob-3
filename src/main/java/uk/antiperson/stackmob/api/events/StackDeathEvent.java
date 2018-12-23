package uk.antiperson.stackmob.api.events;

import org.bukkit.event.entity.EntityDeathEvent;
import uk.antiperson.stackmob.api.StackedEntity;

public class StackDeathEvent extends StackEvent {

    private int deathStep;
    private EntityDeathEvent deathEvent;
    public StackDeathEvent(StackedEntity entity, EntityDeathEvent deathEvent, int deathStep){
        super(entity);
        this.deathEvent = deathEvent;
        this.deathStep = deathStep;
    }

    /**
     * Gets the EntityDeathEvent that caused this event.
     * @return The EntityDeathEvent that caused this event.
     */
    public EntityDeathEvent getDeathEvent() {
        return deathEvent;
    }

    /**
     * Gets the amount of entities from this stack that have been killed.
     * @return the amount of entities that have been killed.
     */
    public int getDeathStep() {
        return deathStep;
    }

}
