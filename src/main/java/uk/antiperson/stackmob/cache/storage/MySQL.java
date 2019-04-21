package uk.antiperson.stackmob.cache.storage;

import uk.antiperson.stackmob.cache.DisableCleanup;
import uk.antiperson.stackmob.cache.StackStorage;
import uk.antiperson.stackmob.cache.StorageManager;
import uk.antiperson.stackmob.tools.UuidUtil;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map;
import java.util.UUID;

public class MySQL extends StackStorage implements DisableCleanup {

    private String hostname;
    private int port;
    private String dbName;
    private String username;
    private String password;
    private Connection connection;

    public MySQL(StorageManager storageManager) {
        super(storageManager);
        hostname = getStackMob().getCustomConfig().getString("storage.database.ip");
        port = getStackMob().getCustomConfig().getInt("storage.database.port");
        dbName = getStackMob().getCustomConfig().getString("storage.database.name");
        username = getStackMob().getCustomConfig().getString("storage.database.username");
        password = getStackMob().getCustomConfig().getString("storage.database.password");
    }

    @Override
    public void loadStorage() {
        getStackMob().getLogger().info("Connecting to database...");
        try {
            makeConnection();
            getStackMob().getLogger().info("Database connection successful!");

            // Convert existing CHAR column UUIDs to BINARY type
            if(getUUIDStorageType() == 1) {
                convertToBinaryUUIDStorage();
            }
            else {
                connection.prepareStatement("CREATE TABLE IF NOT EXISTS stackmob (uuid BINARY(16) NOT NULL UNIQUE, size INT NOT NULL, primary key (uuid))").execute();
                try (ResultSet rs = connection.prepareStatement("SELECT HEX(uuid) as uuid, size FROM stackmob").executeQuery()) {
                    while (rs.next()) {
                        int size = rs.getInt("size");
                        if (size <= 1) continue;

                        getStorageManager().getAmountCache().put(
                                UuidUtil.fromString(rs.getString("uuid")),
                                size
                        );
                    }
                }
            }
            convert();
        } catch (SQLException e) {
            getStackMob().getLogger().warning("An issue occurred while connecting to the database.");
            getStackMob().getLogger().warning("Please make sure that your database details are correct.");
            e.printStackTrace();
        }
    }

    private int getUUIDStorageType() {
        try {
            ResultSet rs = connection.prepareStatement("SELECT * FROM stackmob LIMIT 0").executeQuery();
            return rs.getMetaData().getColumnType(1);
        } catch (SQLException e) {
            return 0;
        }
    }

    private void convertToBinaryUUIDStorage() throws SQLException {
        getStackMob().getLogger().info("Converting existing database to use BINARY type UUIDs");
        try {
            ResultSet rs = connection.prepareStatement("SELECT UUID, size FROM stackmob").executeQuery();
            while (rs.next()){
                getStorageManager().getAmountCache().put(UUID.fromString(rs.getString(1)), rs.getInt(2));
            }
            connection.prepareStatement("DROP TABLE IF EXISTS stackmob").executeQuery();
            connection.prepareStatement("CREATE TABLE IF NOT EXISTS stackmob (uuid BINARY(16) NOT NULL UNIQUE, size INT NOT NULL, primary key (uuid))").executeQuery();
            saveStorage(getStorageManager().getAmountCache());
        }catch (SQLException e){
            getStackMob().getLogger().warning("An error occurred while converting existing database.");
            e.printStackTrace();
        }
    }

    @Override
    public void saveStorage(Map<UUID, Integer> values) {
        try {
            PreparedStatement statement = connection.prepareStatement(
                    "INSERT INTO stackmob (uuid, size) VALUES (UNHEX(?), ?)"
            );
            for (Map.Entry<UUID, Integer> entry : values.entrySet()) {
                if (entry.getValue() <= 1) continue;

                statement.setString(1, entry.getKey().toString().replace("-", ""));
                statement.setInt(2, entry.getValue());
                statement.addBatch();
            }
            connection.prepareStatement("TRUNCATE TABLE stackmob").execute();
            statement.executeBatch();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onDisable() {
        closeConnection();
    }

    private void makeConnection() throws SQLException {
        if(connection == null || connection.isClosed()) {
            String url = "jdbc:mysql://" + hostname + ":" + port + "/" + dbName + "?useSSL=false&rewriteBatchedStatements=true";
            connection = DriverManager.getConnection(url, username, password);
        }
    }

    private void closeConnection() {
        try {
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void convert() {
        FlatFile ff = new FlatFile(getStorageManager());
        if (ff.getFile().exists()) {
            getStackMob().getLogger().info("Converting FLATFILE cache to MySQL...");
            ff.loadStorage();
            saveStorage(getStorageManager().getAmountCache());
            ff.getFile().delete();
        }
    }

}
