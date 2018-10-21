package uk.antiperson.stackmob.entity;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.*;
import org.bukkit.inventory.ItemStack;
import org.bukkit.metadata.FixedMetadataValue;

import uk.antiperson.stackmob.StackMob;
import uk.antiperson.stackmob.compat.PluginCompat;
import uk.antiperson.stackmob.compat.hooks.MythicMobsHook;
import uk.antiperson.stackmob.tools.GeneralTools;
import uk.antiperson.stackmob.tools.extras.GlobalValues;

import java.util.HashSet;
import java.util.UUID;

/**
 * Created by nathat on 24/07/17.
 *
 * TODO: Seperate methods.
 */
public class EntityTools {

    private StackMob sm;
    public EntityTools(StackMob sm){
        this.sm = sm;
    }

    // Compares the differences between two entities
    // firstEntity and nearby are ALWAYS the same entity type!
    public boolean notMatching(Entity firstEntity, Entity nearby) {
        // Just checking if the entity is dead or not, just to be sure.
        if(nearby.isDead()){
            return true;
        }

        if(GeneralTools.hasValidMetadata(firstEntity, GlobalValues.NO_STACK_ALL) &&
                firstEntity.getMetadata(GlobalValues.NO_STACK_ALL).get(0).asBoolean()){
            return true;
        }

        if(GeneralTools.hasValidMetadata(nearby, GlobalValues.NO_STACK_ALL) &&
                nearby.getMetadata(GlobalValues.NO_STACK_ALL).get(0).asBoolean()){
            return true;
        }

        if(sm.getHookManager().onEntityComparison(firstEntity, nearby)){
            return true;
        }
        return sm.getTraitManager().checkTraits(firstEntity, nearby);
    }

    public void onceStacked(Entity entity){
        if(((LivingEntity)entity).isLeashed()){
            ItemStack leash = new ItemStack(Material.LEAD, 1);
            entity.getWorld().dropItemNaturally(entity.getLocation(), leash);
        }
    }

    @Deprecated
    public Entity duplicate(Entity original, boolean slightMovement){
        Entity dupe;
        MythicMobsHook mobsHook = (MythicMobsHook) sm.hookManager.getHook(PluginCompat.MYTHICMOBS);
        if(mobsHook != null && mobsHook.isMythicMob(original)){
            dupe = mobsHook.spawnMythicMob(original);
        }else if(slightMovement){
            dupe = original.getWorld().spawnEntity(original.getLocation().add(0,0.1,0), original.getType());
        }else{
            dupe = original.getWorld().spawnEntity(original.getLocation(), original.getType());
        }
        return cloneTraits(original, dupe);
    }

    public Entity duplicate(Entity original){
        Entity dupe;
        Location dupeLoc;
        MythicMobsHook mobsHook = (MythicMobsHook) sm.hookManager.getHook(PluginCompat.MYTHICMOBS);
        if(mobsHook != null && mobsHook.isMythicMob(original)){
            dupe = mobsHook.spawnMythicMob(original);
        }else if (original instanceof Zombie || original instanceof Skeleton){
        	// will spawn the dupe in the middle of the block the original died in, prevents mobs from glitching through walls due to "safe spawn errors"
        	dupeLoc = new Location(original.getWorld(), original.getLocation().getBlockX()+0.5, original.getLocation().getY(), original.getLocation().getBlockZ()+0.5);
            dupe = original.getWorld().spawnEntity(dupeLoc, original.getType());
        }
        else{
        	dupe = original.getWorld().spawnEntity(original.getLocation(), original.getType());
        }
        return cloneTraits(original, dupe);
    }

    // Copies all of the attributes of one entity and gives them to another.
    // TODO: fire ticks.
    public Entity cloneTraits(Entity original, Entity dupe){
        sm.getTraitManager().applyTraits(original, dupe);

        // other plugin stuff
        sm.hookManager.onEntityClone(dupe);

        // noAi
        setAi((LivingEntity) dupe);

        return dupe;
    }

    public boolean notTaskSuitable(Entity e){
        if(e.isDead()){
            return true;
        }
        return e.hasMetadata(GlobalValues.NO_STACK_ALL) && e.getMetadata(GlobalValues.NO_STACK_ALL).get(0).asBoolean();
    }

    public boolean notEnoughNearby(Entity original){
        double xLoc = sm.config.getCustomConfig().getDouble("check-area.x");
        double yLoc = sm.config.getCustomConfig().getDouble("check-area.y");
        double zLoc = sm.config.getCustomConfig().getDouble("check-area.z");
        if(sm.config.getCustomConfig().getInt("dont-stack-until") > 0){
            HashSet<UUID> entities = new HashSet<>();
            entities.add(original.getUniqueId());
            for(Entity nearby : original.getNearbyEntities(xLoc, yLoc, zLoc)){
                if(original.getType() == nearby.getType()) {
                    if (GeneralTools.hasNotEnoughNear(nearby)) {
                        if (notMatching(original, nearby)) {
                            continue;
                        }
                        entities.add(nearby.getUniqueId());
                    }
                }
            }
            if(entities.size() >= sm.config.getCustomConfig().getInt("dont-stack-until")){
                for(UUID uuid : entities){
                    Entity nearby = Bukkit.getEntity(uuid);
                    if(nearby == null){
                        entities.remove(uuid);
                        return true;
                    }else{
                        nearby.setMetadata(GlobalValues.METATAG, new FixedMetadataValue(sm, 1));
                    }
                }
            }else{
                return true;
            }
        }
        return false;
    }

    public void setAi(LivingEntity entity){
        if(sm.config.getCustomConfig().getBoolean("no-ai.enabled")){
            if(sm.config.getCustomConfig().getBoolean("no-ai.use-whitelist")){
                if(!(sm.config.getCustomConfig().getList("no-ai.type-whitelist").contains(entity.getType().toString()))){
                    return;
                }
            }
            entity.setAI(false);
        }
    }

    public boolean checkIfMaximumSize(Entity entity, int stackSize){
        int maxStackSize = sm.getCustomConfig().getInt("stack-max");
        if (sm.config.getCustomConfig().isInt("custom." + entity.getType() + ".stack-max")) {
            maxStackSize =  sm.getCustomConfig().getInt("custom." + entity.getType() + ".stack-max");
        }
        return maxStackSize == stackSize;
    }
}
