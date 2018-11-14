package uk.antiperson.stackmob.listeners.entity;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.SlimeSplitEvent;
import uk.antiperson.stackmob.StackMob;
import uk.antiperson.stackmob.entity.StackTools;

import java.util.concurrent.ThreadLocalRandom;

public class SlimeEvent implements Listener {

    private StackMob sm;
    public SlimeEvent(StackMob sm){
        this.sm = sm;
    }

    @EventHandler
    public void onSlimeEvent(SlimeSplitEvent e) {
        if(StackTools.hasValidStackData(e.getEntity())){
            int stackSize = StackTools.getSize(e.getEntity()) - 1;
            int randomAmount = ThreadLocalRandom.current().nextInt(2,4);
            e.setCount(e.getCount() + (stackSize * randomAmount));
        }
    }
}
