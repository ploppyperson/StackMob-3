package uk.antiperson.stackmob.tools;

import uk.antiperson.stackmob.StackMob;
import uk.antiperson.stackmob.api.tools.IUpdateChecker;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;

public class UpdateChecker implements IUpdateChecker {

    private StackMob sm;
    public UpdateChecker(StackMob sm){
        this.sm = sm;
    }

    @Override
    public String getLatestVersion(){
        try{
            URL updateUrl = new URL("https://api.spigotmc.org/legacy/update.php?resource=29999");
            HttpURLConnection connect = (HttpURLConnection) updateUrl.openConnection();
            connect.setRequestMethod("GET");
            return new BufferedReader(new InputStreamReader(connect.getInputStream())).readLine();
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public String updateString(){
        String latestVersion = getLatestVersion();
        if(latestVersion == null){
            return "Unable to obtain latest version information. Is your server offline?";
        }
        if(!latestVersion.replace("v", "").equals(sm.getDescription().getVersion())){
            return "A new version (" + latestVersion + ") is currently available! Type '/sm update' to download, then restart your server.";
        }
         return "There is no new version at this time.";
    }

    @Override
    public String update(){
        File currentFile = new File(sm.getClass().getProtectionDomain().getCodeSource().getLocation().getPath());
        try{
            URL fileUrl = new URL("https://api.spiget.org/v2/resources/29999/download");
            try (InputStream in = fileUrl.openStream()) {
                Files.copy(in, currentFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
            }
            return "Downloaded latest version successfully!";
        }catch (Exception e){
            e.printStackTrace();
            return "Failed to download latest version. Check the console for more details.\n" +
                    "You can download the update manually at " + sm.getDescription().getWebsite();
        }
    }
}
