package uk.antiperson.stackmob.tools.cache;

import uk.antiperson.stackmob.StackMob;

import java.sql.*;
import java.util.Set;
import java.util.UUID;

// Template for future SQL caching.

public class SQLCache implements Cache {

    private String username;
    private String password;
    private String finalUrl;
    public SQLCache(StackMob sm){
        String serverUrl = sm.config.getCustomConfig().getString("caching.mysql.server-ip");
        int serverPort = sm.config.getCustomConfig().getInt("caching.mysql.server-port");
        username = sm.config.getCustomConfig().getString("caching.mysql.username");
        password = sm.config.getCustomConfig().getString("caching.mysql.password");
        finalUrl = "jdbc:mysql://" + serverUrl + ":" + serverPort + "/stackmob?autoReconnect=true&useSSL=false";
    }


    public int read(UUID uuid){
        try{
            return performCommandResults("SELECT * FROM CACHE WHERE UUID='" + uuid.toString() + "';").getInt(0);
        }catch (SQLException e){
            e.printStackTrace();
        }
        return 0;
    }

    public void write(UUID uuid, int value){
        remove(uuid);
        performCommand("INSERT INTO CACHE VALUES ('" + uuid.toString() + "', " + value + ");");
    }

    public boolean contains(UUID uuid){
        return false;
    }

    public void remove(UUID uuid){
        performCommand("DELETE FROM CACHE WHERE UUID='" + uuid.toString() + "';");
    }

    public void load(){
        performCommand("CREATE TABLE IF NOT EXISTS CACHE (UUID varchar(255), Size int);");
    }

    public void close(){

    }

    public Set<UUID> getKeys(){
        return null;
    }

    public CacheType getType() {
        return CacheType.SQL;
    }

    public void performCommand(String command){
        try{
            Connection con = DriverManager.getConnection(finalUrl, username, password);
            Statement statement = con.createStatement();
            statement.execute(command);
            con.close();
        }catch (SQLException e){
            e.printStackTrace();
        }
    }

    public ResultSet performCommandResults(String command) throws SQLException{
        Connection con = DriverManager.getConnection(finalUrl, username, password);
        Statement statement = con.createStatement();
        ResultSet rs = statement.executeQuery(command);
        con.close();
        return rs;
    }
}
