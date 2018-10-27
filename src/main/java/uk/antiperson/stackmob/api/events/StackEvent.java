package uk.antiperson.stackmob.api.events;

import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import uk.antiperson.stackmob.api.StackedEntity;

public class StackEvent extends Event {

    private StackedEntity stackedEntity;
    private static final HandlerList handlers = new HandlerList();
    public StackEvent(StackedEntity entity){
        stackedEntity = entity;
    }

    /**
     * Gets the stacked entity involved in the event.
     * @return entity involved.
     */
    public StackedEntity getStackedEntity() {
        return stackedEntity;
    }

    @Override
    public HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }

}
