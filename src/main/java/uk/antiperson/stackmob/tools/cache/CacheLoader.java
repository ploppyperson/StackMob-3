package uk.antiperson.stackmob.tools.cache;

import uk.antiperson.stackmob.StackMob;

public class CacheLoader {

    private boolean flatFileCache = true;
    private Cache cache;
    private StackMob sm;
    public CacheLoader(StackMob sm){
        this.sm = sm;
        if(flatFileCache){
            cache = new FlatCache(sm);
        }else{
            cache = new SQLCache();
        }
    }

    public Cache getCache(){
        return cache;
    }
}
