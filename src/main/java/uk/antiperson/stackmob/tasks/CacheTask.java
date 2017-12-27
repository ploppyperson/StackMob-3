package uk.antiperson.stackmob.tasks;

import org.bukkit.scheduler.BukkitRunnable;
import uk.antiperson.stackmob.StackMob;
import uk.antiperson.stackmob.tools.cache.CacheType;

public class CacheTask extends BukkitRunnable {

    private StackMob sm;
    public CacheTask(StackMob sm){
        this.sm = sm;
    }

    @Override
    public void run(){
        if(sm.cache.getCacheType() == CacheType.YAML){
            sm.cache.getCache().close();
        }
    }
}
