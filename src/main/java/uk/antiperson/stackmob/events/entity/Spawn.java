package uk.antiperson.stackmob.events.entity;

import org.bukkit.Bukkit;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.scheduler.BukkitRunnable;
import uk.antiperson.stackmob.StackMob;
import uk.antiperson.stackmob.tools.extras.GlobalValues;

import java.util.HashSet;
import java.util.UUID;

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
               boolean noMatch = true;

               for(Entity nearby : newEntity.getNearbyEntities(xLoc, yLoc, zLoc)){
                   // EntityTools on both entities
                   if(newEntity.getType() != nearby.getType()){
                       continue;
                   }
                   if(!nearby.hasMetadata(GlobalValues.METATAG)){
                       continue;
                   }
                   if(sm.checks.notMatching(newEntity, nearby)){
                       continue;
                   }else{
                       noMatch = false;
                   }
                   if(sm.config.getCustomConfig().isInt("custom." + nearby.getType() + ".stack-max")){
                       if(nearby.getMetadata(GlobalValues.METATAG).get(0).asInt() + 1 > sm.config.getCustomConfig().getInt("custom." + nearby.getType() + ".stack-max")){
                           continue;
                       }
                   }else {
                       if (nearby.getMetadata(GlobalValues.METATAG).get(0).asInt() + 1 > sm.config.getCustomConfig().getInt("stack-max")) {
                           continue;
                       }
                   }

                   // Continue to stack, if match is found
                   newEntity.remove();
                   int oldSize = nearby.getMetadata(GlobalValues.METATAG).get(0).asInt();
                   nearby.setMetadata(GlobalValues.METATAG, new FixedMetadataValue(sm, oldSize + 1));

                   return;
               }

               if(sm.config.getCustomConfig().getInt("dont-stack-until") > 0 && noMatch){
                   HashSet<UUID> entities = new HashSet<>();
                   entities.add(newEntity.getUniqueId());
                   for(Entity nearby : newEntity.getNearbyEntities(xLoc, yLoc, zLoc)){
                       if(newEntity.getType() == nearby.getType()) {
                           if (nearby.hasMetadata(GlobalValues.NOT_ENOUGH_NEAR) && nearby.getMetadata(GlobalValues.NOT_ENOUGH_NEAR).get(0).asBoolean()) {
                               if (sm.checks.notMatching(newEntity, nearby)) {
                                   continue;
                               }
                               entities.add(nearby.getUniqueId());
                           }
                       }
                   }

                   if(entities.size() == sm.config.getCustomConfig().getInt("dont-stack-until")){
                       for(UUID uuid : entities){
                           // TODO: this might be dodgy, check later
                           Bukkit.getEntity(uuid).setMetadata(GlobalValues.NOT_ENOUGH_NEAR, new FixedMetadataValue(sm, false));
                           Bukkit.getEntity(uuid).setMetadata(GlobalValues.METATAG, new FixedMetadataValue(sm, 1));
                       }
                   }else{
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
