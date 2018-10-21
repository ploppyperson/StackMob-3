package uk.antiperson.stackmob.cache.storage;

import uk.antiperson.stackmob.cache.StorageManager;

public class Sqlite extends MySQL{

    public Sqlite(StorageManager storageManager){
        super(storageManager);
    }

    @Override
    public void makeConnection(){
        throw new UnsupportedOperationException("Sqlite not implemented!");
    }
}
