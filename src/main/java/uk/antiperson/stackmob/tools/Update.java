package uk.antiperson.stackmob.tools;

import org.apache.commons.io.FileUtils;
import uk.antiperson.stackmob.StackMob;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class Update {

    private StackMob sm;
    public Update(StackMob sm){
        this.sm = sm;
    }

    public String getLatestVersion(){
        try{
            HttpURLConnection connect = (HttpURLConnection) new URL("https://api.spigotmc.org/legacy/update.php?resource=29999").openConnection();
            connect.setRequestProperty("User-Agent", "StackMob");
            connect.setRequestMethod("GET");
            return new BufferedReader(new InputStreamReader(connect.getInputStream())).readLine();
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }

    public String updateString(){
        String latestVersion = getLatestVersion();
        if(latestVersion == null){
            return "Unable to obtain latest version information.";
        }
        if(!latestVersion.replace("v", "").equals(sm.getDescription().getVersion())){
            return "A new version (" + latestVersion + ") is currently available!";
        }
         return "There is no new version at this time.";
    }

    public String update(){
        try{
            FileUtils.copyURLToFile(new URL("https://api.spiget.org/v2/resources/29999/download"),
                    new File(sm.getDataFolder().toString().replace("StackMob", "") + File.separator + "update", "StackMob.jar"));
            return "Downloaded latest version successfully!";
        }catch (Exception e){
            e.printStackTrace();
            return "Failed to download latest version.";
        }
    }
}
