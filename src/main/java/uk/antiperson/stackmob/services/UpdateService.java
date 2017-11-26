package uk.antiperson.stackmob.services;

import lombok.AllArgsConstructor;
import org.apache.commons.io.FileUtils;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.concurrent.CompletableFuture;

@AllArgsConstructor
public class UpdateService {

    private Plugin plugin;
    private int resourceId;

    private CompletableFuture<String> getLatestVersion() {
        final CompletableFuture<String> future = new CompletableFuture<>();
        new BukkitRunnable() {

            @Override
            public void run() {
                try {
                    HttpURLConnection connect = (HttpURLConnection) new URL("https://api.spigotmc.org/legacy/update.php?resource=" + resourceId).openConnection();
                    connect.setRequestProperty("User-Agent", "StackMob");
                    connect.setRequestMethod("GET");
                    String result = new BufferedReader(new InputStreamReader(connect.getInputStream())).readLine();
                    future.complete(result);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }.runTaskAsynchronously(plugin);
        return future;
    }

    public CompletableFuture<String> checkUpdate() {
        final CompletableFuture<String> future = new CompletableFuture<>();
        getLatestVersion().thenAccept(latestVersion -> {
            if (latestVersion == null) {
                future.complete("Unable to obtain latest version information.");
            } else if (!latestVersion.replace("v", "").equals(plugin.getDescription().getVersion())) {
                future.complete("A new version (" + latestVersion + ") is currently available!");
            } else {
                future.complete("There is no new version at this time.");
            }
        });
        return future;
    }

    public CompletableFuture<String> update() {
        final CompletableFuture<String> future = new CompletableFuture<>();
        new BukkitRunnable() {

            @Override
            public void run() {
                try {
                    FileUtils.copyURLToFile(new URL("https://api.spiget.org/v2/resources/" + resourceId + "/download"),
                            new File(plugin.getServer().getUpdateFolder(), "StackMob.jar"));
                    future.complete("Downloaded latest version successfully!");
                } catch (Exception e) {
                    e.printStackTrace();
                    future.complete("Failed to download latest version.");
                }
            }
        }.runTaskAsynchronously(plugin);
        return future;
    }
}
