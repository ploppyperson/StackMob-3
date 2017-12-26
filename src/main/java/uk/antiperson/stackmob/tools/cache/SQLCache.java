package uk.antiperson.stackmob.tools.cache;

import java.util.Set;
import java.util.UUID;

// Template for future SQL caching.

public class SQLCache implements Cache {

    public int read(UUID uuid){
        return 0;
    }

    public void write(UUID uuid, int value){

    }

    public boolean contains(UUID uuid){
        return false;
    }

    public void remove(UUID uuid){

    }

    public void load(){

    }

    public void close(){

    }

    public Set<UUID> getKeys(){
        return null;
    }
}
