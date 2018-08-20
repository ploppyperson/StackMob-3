package uk.antiperson.stackmob.listeners.entity;

import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.scheduler.BukkitRunnable;
import uk.antiperson.stackmob.StackMob;
import uk.antiperson.stackmob.tools.GeneralTools;
import uk.antiperson.stackmob.tools.extras.GlobalValues;

public class SpawnEvent implements Listener {

    private StackMob sm;
    public SpawnEvent(StackMob sm){
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
               if(newEntity.hasMetadata(GlobalValues.NO_SPAWN_STACK) && newEntity.getMetadata(GlobalValues.NO_SPAWN_STACK).get(0).asBoolean()){
                   newEntity.removeMetadata(GlobalValues.NO_SPAWN_STACK, sm);
                   return;
               }
               if(sm.pluginSupport.isMiniPet(newEntity)){
                   return;
               }

               // Check for nearby entities, and merge if compatible.
               double xLoc = sm.config.getCustomConfig().getDouble("check-area.x");
               double yLoc = sm.config.getCustomConfig().getDouble("check-area.y");
               double zLoc = sm.config.getCustomConfig().getDouble("check-area.z");
               //boolean wasMatchButTooBig = false;

               for(Entity nearby : newEntity.getNearbyEntities(xLoc, yLoc, zLoc)){
                   // EntityTools on both entities
                   if(newEntity.getType() != nearby.getType()){
                       continue;
                   }
                   if(!(GeneralTools.hasInvaildMetadata(nearby))) {
                       if (sm.tools.notMatching(newEntity, nearby)) {
                           continue;
                       }

                       int newSize = nearby.getMetadata(GlobalValues.METATAG).get(0).asInt() + 1;
                       int maxSize = sm.config.getCustomConfig().getInt("stack-max");
                       if (sm.config.getCustomConfig().isInt("custom." + nearby.getType() + ".stack-max")) {
                           maxSize =  sm.config.getCustomConfig().getInt("custom." + nearby.getType() + ".stack-max");
                       }
                       if (newSize > maxSize) {
                           //wasMatchButTooBig = true;
                           continue;
                       }


                       // Continue to stack, if match is found
                       nearby.setMetadata(GlobalValues.METATAG, new FixedMetadataValue(sm, newSize));
                       newEntity.remove();
                       return;
                   }
               }

               // A match was not found, so we will set the appropriate metadata.
               if(sm.config.getCustomConfig().getInt("dont-stack-until") > 0 /*&& !wasMatchButTooBig*/){
                   if(sm.tools.notEnoughNearby(newEntity)){
                       newEntity.setMetadata(GlobalValues.NOT_ENOUGH_NEAR, new FixedMetadataValue(sm, true));
                   }
               }else{
                   // No match was found
                   newEntity.setMetadata(GlobalValues.METATAG, new FixedMetadataValue(sm, 1));
               }


               // Set mcMMO stuff
               sm.pluginSupport.setMcmmoMetadata(newEntity);
           }
       }.runTaskLater(sm, 1);
    }

}
