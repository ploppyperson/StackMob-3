package uk.antiperson.stackmob.tasks;

import org.bukkit.scheduler.BukkitRunnable;
import uk.antiperson.stackmob.StackMob;

public class CacheTask extends BukkitRunnable {

    private StackMob sm;
    public CacheTask(StackMob sm){
        this.sm = sm;
    }

    @Override
    public void run() {
        sm.getServer().getScheduler().runTask(sm, () -> sm.getStorageManager().getStackStorage().cacheWorldData());
        sm.getStorageManager().getStackStorage().saveStorage();
    }
}
