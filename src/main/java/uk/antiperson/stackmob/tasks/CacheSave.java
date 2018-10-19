package uk.antiperson.stackmob.tasks;

import org.bukkit.scheduler.BukkitRunnable;
import uk.antiperson.stackmob.StackMob;

public class CacheSave extends BukkitRunnable {

    private StackMob sm;
    public CacheSave(StackMob sm){
        this.sm = sm;
    }

    @Override
    public void run() {
        sm.storageManager.saveStorage();
    }
}
