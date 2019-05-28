package uk.antiperson.stackmob.tasks;

import org.bukkit.scheduler.BukkitRunnable;
import uk.antiperson.stackmob.StackMobPlugin;

import java.util.Map;
import java.util.UUID;

public class CacheTask extends BukkitRunnable {

    private StackMobPlugin sm;
    public CacheTask(StackMobPlugin sm){
        this.sm = sm;
    }

    @Override
    public void run() {
        Map<UUID, Integer> values = sm.getStorageManager().getCombinedMap();
        sm.getServer().getScheduler().runTaskAsynchronously(sm, new Runnable() {
            @Override
            public void run() {
                sm.getStorageManager().getStackStorage().saveStorage(values);
            }
        });
    }
}
