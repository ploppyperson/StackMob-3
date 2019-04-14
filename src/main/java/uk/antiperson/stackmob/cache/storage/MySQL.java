package uk.antiperson.stackmob.cache.storage;

import uk.antiperson.stackmob.cache.DisableCleanup;
import uk.antiperson.stackmob.cache.StackStorage;
import uk.antiperson.stackmob.cache.StorageManager;

import java.sql.*;
import java.util.Map;
import java.util.UUID;

public class MySQL extends StackStorage implements DisableCleanup {

    private String hostname;
    private int port;
    private String dbName;
    private String username;
    private String password;
    private Connection connection;
    public MySQL(StorageManager storageManager){
        super(storageManager);
        hostname = getStackMob().getCustomConfig().getString("storage.database.ip");
        port = getStackMob().getCustomConfig().getInt("storage.database.port");
        dbName = getStackMob().getCustomConfig().getString("storage.database.name");
        username = getStackMob().getCustomConfig().getString("storage.database.username");
        password = getStackMob().getCustomConfig().getString("storage.database.password");
    }

    @Override
    public void loadStorage(){
        getStackMob().getLogger().info("Connecting to database...");
        try {
            makeConnection();
            getStackMob().getLogger().info("Database connection successful!");
            connection.prepareStatement("CREATE TABLE IF NOT EXISTS stackmob (UUID CHAR(36) NOT NULL UNIQUE, Size INT NOT NULL)").execute();
            ResultSet rs = connection.prepareStatement("SELECT * FROM stackmob").executeQuery();
            while (rs.next()){
                getStorageManager().getAmountCache().put(UUID.fromString(rs.getString(1)), rs.getInt(2));
            }
            convert();
        }catch (SQLException e){
            getStackMob().getLogger().warning("An issue occurred while connecting to the database.");
            getStackMob().getLogger().warning("Please make sure that your database details are correct.");
            e.printStackTrace();
        }

    }

    @Override
    public void saveStorage(Map<UUID, Integer> values){
        try {
            connection.prepareStatement("TRUNCATE TABLE stackmob").execute();
            PreparedStatement statement = connection.prepareStatement("INSERT INTO stackmob VALUES (?, ?)");
            for(Map.Entry<UUID, Integer> entry : values.entrySet()){
                statement.setString(1, entry.getKey().toString());
                statement.setInt(2, entry.getValue());
                statement.addBatch();
            }
            statement.executeBatch();
        }catch (SQLException e){
            e.printStackTrace();
        }
    }

    @Override
    public void onDisable(){
        closeConnection();
    }

    public void makeConnection() throws SQLException{
        String url = "jdbc:mysql://" + hostname + ":" + port + "/" + dbName + "?useSSL=false&rewriteBatchedStatements=true";
        connection = DriverManager.getConnection(url, username, password);
    }

    private void closeConnection(){
        try {
            connection.close();
        }catch (SQLException e){
            e.printStackTrace();
        }
    }

    private void convert(){
        FlatFile ff = new FlatFile(getStorageManager());
        if(ff.getFile().exists()){
            getStackMob().getLogger().info("Converting FLATFILE cache to MySQL...");
            ff.loadStorage();
            saveStorage(getStorageManager().getAmountCache());
            ff.getFile().delete();
        }
    }
}
