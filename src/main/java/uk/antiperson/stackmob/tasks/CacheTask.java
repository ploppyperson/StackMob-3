package uk.antiperson.stackmob.tasks;

import org.bukkit.scheduler.BukkitRunnable;
import uk.antiperson.stackmob.StackMob;
import uk.antiperson.stackmob.tools.cache.SQLCache;

public class CacheTask extends BukkitRunnable {

    private StackMob sm;
    public CacheTask(StackMob sm){
        this.sm = sm;
    }

    @Override
    public void run(){
        if(sm.cache.getCache() instanceof SQLCache){
            sm.cache.getCache().close();
            sm.cache.getCache().load();
        }else{
            sm.cache.getCache().close();
        }
    }
}
