package uk.antiperson.stackmob.listeners.entity;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.SlimeSplitEvent;
import uk.antiperson.stackmob.tools.GeneralTools;
import uk.antiperson.stackmob.tools.extras.GlobalValues;

import java.util.concurrent.ThreadLocalRandom;

public class SlimeEvent implements Listener {

    @EventHandler
    public void onSlimeEvent(SlimeSplitEvent e) {
        if(GeneralTools.hasValidStackData(e.getEntity())){
            int stackSize = e.getEntity().getMetadata(GlobalValues.METATAG).get(0).asInt() - 1;
            int randomAmount = ThreadLocalRandom.current().nextInt(2,4);
            e.setCount(e.getCount() + (stackSize * randomAmount));
        }
    }
}
