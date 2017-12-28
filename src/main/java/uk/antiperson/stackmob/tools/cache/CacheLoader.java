package uk.antiperson.stackmob.tools.cache;

import uk.antiperson.stackmob.StackMob;

public class CacheLoader {

    private Cache cache;
    public CacheLoader(StackMob sm){
        if(sm.config.getCustomConfig().getBoolean("caching.mysql.enabled")){
            cache = new SQLCache(sm);
        }else{
            cache = new FlatCache(sm);
        }
    }

    public Cache getCache(){
        return cache;
    }

}

