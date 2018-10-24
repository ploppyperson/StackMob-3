package uk.antiperson.stackmob.entity;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.*;
import org.bukkit.inventory.ItemStack;

import uk.antiperson.stackmob.StackMob;
import uk.antiperson.stackmob.compat.PluginCompat;
import uk.antiperson.stackmob.compat.hooks.MythicMobsHook;

/**
 * Created by nathat on 24/07/17.
 *
 * Strictly stuff to do with entities, but not stacking.
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

    public Entity duplicate(Entity original) {
        Location dupeLoc = original.getLocation();
        if (original instanceof Zombie || original instanceof Skeleton) {
            // Make location in the middle of the block, prevents wall glitching due to "safe spawn errors"
            dupeLoc.setX(dupeLoc.getBlockX() + 0.5);
            dupeLoc.setZ(dupeLoc.getBlockZ() + 0.5);
        }
        return cloneTraits(original, spawnDuplicateEntity(dupeLoc, original));
    }

    // Copies all of the attributes of one entity and gives them to another.
    // TODO: fire ticks.
    public Entity cloneTraits(Entity original, Entity dupe){
        sm.getTraitManager().applyTraits(original, dupe);
        setTraits((LivingEntity) dupe);
        return dupe;
    }

    public void setTraits(LivingEntity entity){
        // other plugin stuff
        sm.getHookManager().onEntityClone(entity);
        // noAi
        setAi(entity);
    }

    public Entity spawnDuplicateEntity(Location location, Entity original){
        MythicMobsHook mmh = (MythicMobsHook) sm.getHookManager().getHook(PluginCompat.MYTHICMOBS);
        if(mmh != null && mmh.isMythicMob(original)){
            return mmh.spawnMythicMob(location, original);
        }
        return original.getWorld().spawnEntity(location, original.getType());
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
}
