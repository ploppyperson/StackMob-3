package uk.antiperson.stackmob.listeners.entity;

import lombok.NoArgsConstructor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityExplodeEvent;
import uk.antiperson.stackmob.GlobalValues;

@NoArgsConstructor
public class ExplodeListener implements Listener {

    @EventHandler(ignoreCancelled = true, priority = EventPriority.HIGHEST)
    public void onExplode(EntityExplodeEvent event) {
        if (event.getEntity().hasMetadata(GlobalValues.METATAG)) {
            int stackSize = event.getEntity().getMetadata(GlobalValues.METATAG).get(0).asInt();
            event.setYield(stackSize * 2);
        }
    }
}
