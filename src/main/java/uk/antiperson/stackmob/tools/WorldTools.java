package uk.antiperson.stackmob.tools;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Chunk;
import org.bukkit.World;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import uk.antiperson.stackmob.StackMob;

import java.io.File;
import java.io.FilenameFilter;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class WorldTools {

    private StackMob sm;
    private String lastColour;
    public WorldTools(StackMob sm){
        this.sm = sm;
        String configFormat = sm.config.getCustomConfig().getString("tag.format");
        lastColour = ChatColor.getLastColors(ChatColor.translateAlternateColorCodes('&', configFormat));
    }

    public void fixAllEntities() {
        for(World world : Bukkit.getWorlds()) {

            final Pattern regionPattern = Pattern.compile("r\\.([0-9-]+)\\.([0-9-]+)\\.mca");

            File worldDir = new File(Bukkit.getWorldContainer(), world.getName());
            File regionDir = new File(worldDir, "region");

            File[] regionFiles = regionDir.listFiles(new FilenameFilter() {
                @Override
                public boolean accept(File dir, String name) {
                    return regionPattern.matcher(name).matches();
                }
            });

            sm.getLogger().info("Found " + (regionFiles.length * 1024) + " chunk candidates in " + regionFiles.length + " files to check for loading ...");

            for (File f : regionFiles) {
                // extract coordinates from filename
                Matcher matcher = regionPattern.matcher(f.getName());
                if (!matcher.matches()) {
                    sm.getLogger().warning("FilenameFilter accepted unmatched filename: " + f.getName());
                    continue;
                }

                int mcaX = Integer.parseInt(matcher.group(1));
                int mcaZ = Integer.parseInt(matcher.group(2));

                int loadedCount = 0;

                for (int cx = 0; cx < 32; cx++) {
                    for (int cz = 0; cz < 32; cz++) {
                        // local chunk coordinates need to be transformed into global ones
                        int x = (mcaX << 5) + cx;
                        int z = (mcaZ << 5) + cz;
                        if (world.loadChunk(x, z, false)) {
                            fixEntities(world.getChunkAt(x, z));
                            loadedCount++;
                        }

                    }
                }


                sm.getLogger().info("Actually loaded " + loadedCount + " chunks from " + f.getName() + ".");
            }
        }
    }

    public void fixEntities(Chunk chunk){
        for(Entity entity : chunk.getEntities()){
            if(entity instanceof LivingEntity && !(entity instanceof ArmorStand)){
                if(entity.getCustomName() != null && ChatColor.getLastColors(entity.getCustomName()).equals(lastColour)){
                    int stackSize = Integer.valueOf(ChatColor.stripColor(entity.getCustomName().replaceAll("\\D+","")));
                    sm.cache.getCache().write(entity.getUniqueId(), stackSize);
                }
            }
        }
        chunk.unload();
    }
}
