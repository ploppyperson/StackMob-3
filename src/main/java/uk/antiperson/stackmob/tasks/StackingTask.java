package uk.antiperson.stackmob.tasks;

import org.bukkit.scheduler.BukkitRunnable;
import uk.antiperson.stackmob.StackMob;

public abstract class StackingTask extends BukkitRunnable {

    private StackMob sm;
    public StackingTask(StackMob sm){
        this.sm = sm;
    }

    public StackMob getStackMob() {
        return sm;
    }
}
