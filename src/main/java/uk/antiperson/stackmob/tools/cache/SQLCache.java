package uk.antiperson.stackmob.tools.cache;

import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import uk.antiperson.stackmob.StackMob;
import uk.antiperson.stackmob.tools.extras.GlobalValues;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

// Template for future SQL caching.

public class SQLCache implements Cache{

    private String username;
    private String password;
    private String firstUrl;
    private Connection con;
    private StackMob sm;
    public SQLCache(StackMob sm){
        String serverUrl = sm.config.getCustomConfig().getString("caching.mysql.server-ip");
        int serverPort = sm.config.getCustomConfig().getInt("caching.mysql.server-port");
        username = sm.config.getCustomConfig().getString("caching.mysql.username");
        password = sm.config.getCustomConfig().getString("caching.mysql.password");
        firstUrl = "jdbc:mysql://" + serverUrl + ":" + serverPort + "/?autoReconnect=true&useSSL=false";
        this.sm = sm;
    }


    public int read(UUID uuid){
        try{
            ResultSet rs = con.createStatement().executeQuery("SELECT Size FROM CACHE WHERE UUID='" + uuid.toString() + "';");
            rs.next();
            return rs.getInt(1);
        }catch (SQLException e){
            e.printStackTrace();
        }
        return 0;
    }

    public void write(UUID uuid, int value){
        remove(uuid);
        try {
            con.createStatement().execute("INSERT INTO CACHE VALUES ('" + uuid.toString() + "', " + value + ");");
        }catch (SQLException e){
            e.printStackTrace();
        }
    }

    public boolean contains(UUID uuid){
        try {
            return con.createStatement().executeQuery("SELECT UUID FROM CACHE WHERE UUID='" + uuid.toString()+ "';").next();
        }catch (SQLException e){
            e.printStackTrace();
        }
        return false;
    }

    public void remove(UUID uuid){
        try {
            con.createStatement().execute("DELETE FROM CACHE WHERE UUID='" + uuid.toString() + "';");
        }catch (SQLException e){
            e.printStackTrace();
        }
    }

    public void load(){
        try{
            inialize();
            con.createStatement().execute("CREATE DATABASE IF NOT EXISTS " + getDatabaseName() + ";");
            calalog();
            con.createStatement().execute("CREATE TABLE IF NOT EXISTS CACHE (UUID varchar(255), Size int);");
        }catch (SQLException e){
            e.printStackTrace();
        }
    }

    public void close(){
        try {
            for(World world : Bukkit.getWorlds()){
                for(Entity entity : world.getEntities()){
                    if(entity.hasMetadata(GlobalValues.METATAG) &&
                            entity.getMetadata(GlobalValues.METATAG).size() > 0 &&
                            entity.getMetadata(GlobalValues.METATAG).get(0).asInt() > 1){
                        write(entity.getUniqueId(), entity.getMetadata(GlobalValues.METATAG).get(0).asInt());
                    }else if(entity.hasMetadata(GlobalValues.NOT_ENOUGH_NEAR) &&
                            entity.getMetadata(GlobalValues.NOT_ENOUGH_NEAR).size() > 0 &&
                            entity.getMetadata(GlobalValues.NOT_ENOUGH_NEAR).get(0).asBoolean()){
                        write(entity.getUniqueId(), -69);
                    }
                }
            }

            con.close();
        }catch (SQLException e){
            e.printStackTrace();
        }
    }

    public Set<UUID> getKeys(){
        HashSet<UUID> keys = new HashSet<>();
        try{
            ResultSet rs = con.createStatement().executeQuery("SELECT UUID FROM CACHE;");
            while (rs.next()){
                keys.add(UUID.fromString(rs.getString(1)));
            }
        }catch (SQLException e){
            e.printStackTrace();
        }

        return keys;
    }

    public void inialize() throws SQLException{
        con = DriverManager.getConnection(firstUrl, username, password);
    }

    public void convert(){
        FlatCache flat = new FlatCache(sm);
        if(flat.file.exists()){
            sm.getLogger().info("Converting YAML cache file to SQL database...");
            flat.load();
            for(UUID uuid : flat.getKeys()){
                write(uuid, flat.read(uuid));
            }
        }
    }

    public void drop(){
        try {
            con.createStatement().execute("DROP TABLE CACHE;");
        }catch (SQLException e){
            e.printStackTrace();
        }
    }

    public void calalog() throws SQLException{
        con.setCatalog(getDatabaseName());
    }

    public boolean hasSqlBeenUsedBefore(){
        try {
            inialize();
            calalog();
            con.createStatement().execute("SELECT * FROM CACHE");
            return true;
        }catch (SQLException e){
            return false;
        }
    }

    public void closeWithoutSaving(){
        try {
            con.close();
        }catch (SQLException e){
            e.printStackTrace();
        }
    }

    public String getDatabaseName(){
        if(sm.config.getCustomConfig().isString("caching.mysql.database-name")){
            return  sm.config.getCustomConfig().getString("caching.mysql.database-name");
        }else{
            return "STACKMOB";
        }
    }
}
