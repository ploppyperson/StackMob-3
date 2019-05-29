package uk.antiperson.stackmob.entity;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.*;
import org.bukkit.inventory.ItemStack;

import uk.antiperson.stackmob.api.StackMob;
import uk.antiperson.stackmob.api.compat.PluginCompat;
import uk.antiperson.stackmob.api.entity.IEntityTools;
import uk.antiperson.stackmob.compat.hooks.MythicMobsHook;

/**
 * Created by nathat on 24/07/17.
 *
 * Strictly stuff to do with entities, but not stacking.
 */
public class EntityTools implements IEntityTools {

    private StackMob sm;
    public EntityTools(StackMob sm){
        this.sm = sm;
    }

    // Compares the differences between two entities
    // firstEntity and nearby are ALWAYS the same entity type!
    @Override
    public boolean notMatching(Entity firstEntity, Entity nearby) {
        // Just checking if the entity is dead or not, just to be sure.
        if(sm.getHookManager().onEntityComparison(firstEntity, nearby)){
            return true;
        }
        return sm.getTraitManager().checkTraits(firstEntity, nearby);
    }

    @Override
    public void onceStacked(Entity entity){
        if(((LivingEntity)entity).isLeashed()){
            ItemStack leash = new ItemStack(Material.LEAD, 1);
            entity.getWorld().dropItemNaturally(entity.getLocation(), leash);
        }
        sm.getLogic().cleanup(entity);
    }

    @Override
    public Entity duplicate(Entity original) {
        Entity duplicate = spawnDuplicateEntity(getSpawnLocation(original), original);
        sm.getTraitManager().applyTraits(original, duplicate);
        setTraits((LivingEntity) duplicate);
        return duplicate;
    }

    private Location getSpawnLocation(Entity original){
        Location dupeLoc = original.getLocation();
        if (original instanceof Zombie || original instanceof Skeleton) {
            // Make location in the middle of the block, prevents wall glitching due to "safe spawn errors"
            dupeLoc.setX(dupeLoc.getBlockX() + 0.5);
            dupeLoc.setZ(dupeLoc.getBlockZ() + 0.5);
        }
        return dupeLoc;
    }

    @Override
    public void setTraits(LivingEntity entity){
        // other plugin stuff
        sm.getHookManager().onEntityClone(entity);
        // noAi
        setAi(entity);
    }

    private Entity spawnDuplicateEntity(Location location, Entity original){
        MythicMobsHook mmh = (MythicMobsHook) sm.getHookManager().getHook(PluginCompat.MYTHICMOBS);
        if(mmh != null && mmh.isMythicMob(original)){
            Entity entity = mmh.spawnMythicMob(location, original);
            if(entity != null){
                return entity;
            }
        }
        return original.getWorld().spawnEntity(location, original.getType());
    }

    @Override
    public void setAi(LivingEntity entity){
        if(sm.getCustomConfig().getBoolean("no-ai.enabled")){
            if(sm.getCustomConfig().getBoolean("no-ai.use-whitelist")){
                if(!(sm.getCustomConfig().getStringList("no-ai.type-whitelist").contains(entity.getType().toString()))){
                    return;
                }
            }
            entity.setAI(false);
        }
    }


}
