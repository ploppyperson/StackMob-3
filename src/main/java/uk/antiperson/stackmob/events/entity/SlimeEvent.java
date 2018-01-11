package uk.antiperson.stackmob.events.entity;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.SlimeSplitEvent;
import uk.antiperson.stackmob.StackMob;
import uk.antiperson.stackmob.tools.extras.GlobalValues;

import java.util.concurrent.ThreadLocalRandom;

public class SlimeEvent implements Listener {

    private StackMob sm;

    public SlimeEvent(StackMob sm) {
        this.sm = sm;
    }

    @EventHandler
    public void onSlimeEvent(SlimeSplitEvent e) {
        if(e.getEntity().hasMetadata(GlobalValues.METATAG)){
            int stackSize = e.getEntity().getMetadata(GlobalValues.METATAG).get(0).asInt() - 1;
            int randomAmount = ThreadLocalRandom.current().nextInt(2,4);
            e.setCount(e.getCount() + (stackSize * randomAmount));
        }
    }
}
