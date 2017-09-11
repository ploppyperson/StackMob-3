package uk.antiperson.stackmob.events.entity;

import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.scheduler.BukkitRunnable;
import uk.antiperson.stackmob.tools.extras.GlobalValues;
import uk.antiperson.stackmob.StackMob;

public class Spawn implements Listener {

    private StackMob sm;
    public Spawn(StackMob sm){
        this.sm = sm;
    }

    @EventHandler
    public void onCreatureSpawn(CreatureSpawnEvent e){
        final Entity newEntity = e.getEntity();
        final CreatureSpawnEvent.SpawnReason sr = e.getSpawnReason();

        // EntityTools before running task
        if(newEntity instanceof ArmorStand){
            return;
        }
        if(sm.config.getCustomConfig().getStringList("no-stack-reasons")
                .contains(sr.toString())){
            return;
        }
        if(sm.config.getCustomConfig().getStringList("no-stack-types")
                .contains(newEntity.getType().toString())){
            return;
        }
        if(sm.config.getCustomConfig().getStringList("no-stack-worlds")
                .contains(newEntity.getWorld().getName())){
            return;
        }

        // BukkitRunnable to delay this, so the needed metadata can be set before attempting to merge.
        new BukkitRunnable(){
           @Override
           public void run(){
               // EntityTools before attempting to merge with other entities
               if(newEntity.hasMetadata(GlobalValues.noSpawnStack) && newEntity.getMetadata(GlobalValues.noSpawnStack).get(0).asBoolean()){
                   newEntity.removeMetadata(GlobalValues.noSpawnStack, sm);
                   return;
               }
               if(sm.pluginSupport.isMiniPet(newEntity)){
                   return;
               }

               // Check for nearby entities, and merge if compatible.
               double xLoc = sm.config.getCustomConfig().getDouble("check-area.x");
               double yLoc = sm.config.getCustomConfig().getDouble("check-area.y");
               double zLoc = sm.config.getCustomConfig().getDouble("check-area.z");

               for(Entity nearby : newEntity.getNearbyEntities(xLoc, yLoc, zLoc)){
                   // EntityTools on both entities
                   if(newEntity.getType() != nearby.getType()){
                       continue;
                   }
                   if(!nearby.hasMetadata(GlobalValues.metaTag)){
                       continue;
                   }
                   if(sm.config.getCustomConfig().isInt("custom." + nearby.getType() + ".stack-max")){
                       if(nearby.getMetadata(GlobalValues.metaTag).get(0).asInt() + 1 > sm.config.getCustomConfig().getInt("custom." + nearby.getType() + ".stack-max")){
                           continue;
                       }
                   }else {
                       if (nearby.getMetadata(GlobalValues.metaTag).get(0).asInt() + 1 > sm.config.getCustomConfig().getInt("stack-max")) {
                           continue;
                       }
                   }
                   if(sm.checks.notMatching(newEntity, nearby)){
                       continue;
                   }

                   // Continue to stack, if match is found.
                   newEntity.remove();
                   int oldSize = nearby.getMetadata(GlobalValues.metaTag).get(0).asInt();
                   nearby.setMetadata(GlobalValues.metaTag, new FixedMetadataValue(sm, oldSize + 1));


                   return;
               }

               // No match was found
               newEntity.setMetadata(GlobalValues.metaTag, new FixedMetadataValue(sm, 1));

               // Set mcMMO stuff
               sm.pluginSupport.setMcmmoMetadata(newEntity);
           }
       }.runTaskLater(sm, 1);
    }

}
