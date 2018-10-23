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
        setTraits((LivingEntity) dupe);
        return dupe;
    }

    public void setTraits(LivingEntity entity){
        // other plugin stuff
        sm.getHookManager().onEntityClone(entity);
        // noAi
        setAi(entity);
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
