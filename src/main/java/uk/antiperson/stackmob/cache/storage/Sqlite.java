package uk.antiperson.stackmob.cache.storage;

import uk.antiperson.stackmob.cache.StorageManager;

import java.sql.DriverManager;
import java.sql.SQLException;

public class Sqlite extends MySQL{

    public Sqlite(StorageManager storageManager){
        super(storageManager);
    }

    @Override
    public void makeConnection() throws SQLException{
        String folder = getStorageManager().getStackMob().getDataFolder().getPath();
        String url = "jdbc:sqlite:" + folder + "/cache.sqlite";
        connection = DriverManager.getConnection(url);
    }
}
