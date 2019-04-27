package uk.antiperson.stackmob.api.events;

import org.bukkit.event.Cancellable;
import uk.antiperson.stackmob.api.StackedEntity;

public class EntityStackEvent extends StackEvent implements Cancellable {

    private boolean cancelled;
    private StackedEntity nearby;
    public EntityStackEvent(StackedEntity original, StackedEntity nearby){
        super(original);
        this.nearby = nearby;
    }

    /**
     * Gets the nearby entity that is about to be merged.
     * @return StackedEntity that is going to be merged.
     */
    public StackedEntity getNearbyEntity() {
        return nearby;
    }

    @Override
    public boolean isCancelled() {
        return cancelled;
    }

    @Override
    public void setCancelled(boolean cancelled) {
        this.cancelled = cancelled;
    }

}
