package uk.antiperson.stackmob.tools;

import io.lumine.xikage.mythicmobs.mobs.MobManager;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.*;
import org.bukkit.inventory.ItemStack;
import org.bukkit.metadata.FixedMetadataValue;

import uk.antiperson.stackmob.StackMob;
import uk.antiperson.stackmob.tools.extras.GlobalValues;

import java.util.HashSet;
import java.util.UUID;

/**
 * Created by nathat on 24/07/17.
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

        if(firstEntity.hasMetadata(GlobalValues.NO_STACK_ALL) && firstEntity.getMetadata(GlobalValues.NO_STACK_ALL).get(0).asBoolean()){
            return true;
        }

        if(nearby.hasMetadata(GlobalValues.NO_STACK_ALL) && nearby.getMetadata(GlobalValues.NO_STACK_ALL).get(0).asBoolean()){
            return true;
        }

        if(sm.pluginSupport.isWorldGuardEnabled() && sm.config.getCustomConfig().getBoolean("worldguard-support")){
            if(!sm.pluginSupport.getWorldGuard().checkCanStack(firstEntity.getLocation())){
                return true;
            }
        }

        // Checks on the nearby entity
        if (nearby instanceof Tameable) {
            if (sm.config.getCustomConfig().getBoolean("check.tamed")) {
                if (((Tameable) nearby).isTamed()) {
                    return true;
                }
            }
        }
        if (((LivingEntity) nearby).isLeashed()) {
            if (sm.config.getCustomConfig().getBoolean("check.leashed")) {
                return true;
            }

        }
        // Checks on both entities
        if (firstEntity instanceof Villager) {
            if (sm.config.getCustomConfig().getBoolean("compare.villager-profession")) {
                if (((Villager) firstEntity).getProfession() != ((Villager) nearby).getProfession()) {
                    return true;
                }
                if(((Villager) firstEntity).getCareer() != ((Villager) nearby).getCareer()){
                    return true;
                }
            }
        }
        if (firstEntity instanceof Sheep) {
            if (sm.config.getCustomConfig().getBoolean("compare.sheep-wool-sheared")) {
                if (((Sheep) firstEntity).isSheared() != ((Sheep) nearby).isSheared()) {
                    return true;
                }
            }
            if (sm.config.getCustomConfig().getBoolean("compare.sheep-wool-color")) {
                if (((Sheep) firstEntity).getColor() != ((Sheep) nearby).getColor()) {
                    return true;
                }
            }
        }
        if (firstEntity instanceof Slime) {
            if (sm.config.getCustomConfig().getBoolean("compare.slime-size")) {
                if (((Slime) firstEntity).getSize() != ((Slime) nearby).getSize()) {
                    return true;
                }
            }
        }
        if (firstEntity instanceof Ageable) {
            if (sm.config.getCustomConfig().getBoolean("compare.entity-age")) {
                if (((Ageable) firstEntity).isAdult() != ((Ageable) nearby).isAdult()) {
                    return true;
                }
            }
        }
        if (firstEntity instanceof Zombie) {
            if (sm.config.getCustomConfig().getBoolean("compare.entity-age")) {
                if (((Zombie) firstEntity).isBaby() != ((Zombie) nearby).isBaby()) {
                    return true;
                }
            }
        }
        if(firstEntity instanceof Animals){
            if (sm.config.getCustomConfig().getBoolean("compare.can-breed")){
                if (((Animals) firstEntity).canBreed() != ((Animals) nearby).canBreed()) {
                    return true;
                }
            }
        }
        if(firstEntity instanceof Horse){
            if(sm.config.getCustomConfig().getBoolean("compare.horse-color")){
                if(((Horse)firstEntity).getColor() != ((Horse) nearby).getColor()){
                    return true;
                }
            }
        }
        if(firstEntity instanceof Llama){
            if (sm.config.getCustomConfig().getBoolean("compare.llama-color")){
                if (((Llama) firstEntity).getColor() != ((Llama) nearby).getColor()) {
                    return true;
                }
            }
        }
        if (firstEntity instanceof Parrot) {
            if (sm.config.getCustomConfig().getBoolean("compare.parrot-color")) {
                if (((Parrot) firstEntity).getVariant() != ((Parrot) nearby).getVariant()) {
                    return true;
                }
            }
        }

        if(sm.pluginSupport.getMythicSupport() != null){
            MobManager mm = sm.pluginSupport.getMythicSupport().getMythicMobs();
            if(mm.isActiveMob(nearby.getUniqueId()) && mm.isActiveMob(firstEntity.getUniqueId())){
                if(sm.config.getCustomConfig().getStringList("mythicmobs.blacklist")
                        .contains(mm.getMythicMobInstance(nearby).getType().getInternalName())){
                    return true;
                }
                if(sm.config.getCustomConfig().getStringList("mythicmobs.blacklist")
                        .contains(mm.getMythicMobInstance(firstEntity).getType().getInternalName())){
                    return true;
                }
                return !mm.getMythicMobInstance(nearby).getType().equals(mm.getMythicMobInstance(firstEntity).getType());
            }
        }


        return false;
    }

    public void onceStacked(Entity firstEntity, Entity nearby){
        if(((LivingEntity)firstEntity).isLeashed()){
            ItemStack leash = new ItemStack(Material.LEAD, 1);
            firstEntity.getWorld().dropItemNaturally(firstEntity.getLocation(), leash);
        }else if(((LivingEntity)nearby).isLeashed()){
            ItemStack leash = new ItemStack(Material.LEAD, 1);
            nearby.getWorld().dropItemNaturally(nearby.getLocation(), leash);
        }
    }

    public Entity duplicate(Entity original, boolean slightMovement){
        Entity dupe;
        if(sm.pluginSupport.getMythicSupport() != null && sm.pluginSupport.getMythicSupport().isMythicMob(original)){
            dupe = sm.pluginSupport.getMythicSupport().spawnMythicMob(original);
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
        if(sm.pluginSupport.getMythicSupport() != null && sm.pluginSupport.getMythicSupport().isMythicMob(original)){
            dupe = sm.pluginSupport.getMythicSupport().spawnMythicMob(original);
        }else if (original instanceof Zombie){
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
    // TODO: fire ticks, no ai if you don't want faction server owners screaming at you.
    public Entity cloneTraits(Entity original, Entity dupe){
        if (dupe instanceof Tameable) {
            if (!sm.config.getCustomConfig().getBoolean("check.tamed")) {
                ((Tameable)dupe).setTamed(((Tameable)original).isTamed());
                ((Tameable)dupe).setOwner(((Tameable)original).getOwner());
            }
        }
        if (dupe instanceof Villager) {
            if (sm.config.getCustomConfig().getBoolean("compare.villager-profession")) {
                ((Villager) dupe).setProfession(((Villager) original).getProfession());
                ((Villager) dupe).setCareer(((Villager) original).getCareer());
            }
        }

        if (dupe instanceof Sheep) {
            if (sm.config.getCustomConfig().getBoolean("compare.sheep-wool-sheared")) {
                ((Sheep)dupe).setSheared(((Sheep)original).isSheared());
            }
            if (sm.config.getCustomConfig().getBoolean("compare.sheep-wool-color")) {
                ((Sheep)dupe).setColor(((Sheep)original).getColor());
            }
        }

        if (dupe instanceof Slime) {
            if (sm.config.getCustomConfig().getBoolean("compare.slime-size")) {
                ((Slime)dupe).setSize(((Slime) original).getSize());
            }
        }

        if (dupe instanceof Ageable) {
            if (sm.config.getCustomConfig().getBoolean("compare.entity-age")) {
               if(((Ageable) original).isAdult()){
                   ((Ageable) dupe).setAdult();
               }else{
                   ((Ageable) dupe).setBaby();
               }
            }
        }
        if (dupe instanceof Zombie) {
            if (sm.config.getCustomConfig().getBoolean("compare.entity-age")) {
                ((Zombie) dupe).setBaby(((Zombie) original).isBaby());
            }
        }

        if(dupe instanceof Animals){
            if (sm.config.getCustomConfig().getBoolean("compare.can-breed")){
                ((Animals) dupe).setBreed(((Animals) original).canBreed());
            }
        }
        if(dupe instanceof Llama){
            if (sm.config.getCustomConfig().getBoolean("compare.llama-color")){
                ((Llama) dupe).setColor(((Llama) original).getColor());
            }
        }
        if (dupe instanceof Parrot) {
            if (sm.config.getCustomConfig().getBoolean("compare.parrot-color")) {
                ((Parrot) dupe).setVariant(((Parrot) original).getVariant());
            }
        }

        // mcMMO stuff
        sm.pluginSupport.setMcmmoMetadata(dupe);

        return dupe;
    }

    public boolean notTaskSuitable(Entity e){
        if(e.isDead()){
            return true;
        }
        if(e.hasMetadata(GlobalValues.NO_TASK_STACK) && e.getMetadata(GlobalValues.NO_TASK_STACK).get(0).asBoolean()){
            return true;
        }
        if(e.hasMetadata(GlobalValues.NO_STACK_ALL) && e.getMetadata(GlobalValues.NO_STACK_ALL).get(0).asBoolean()){
            return true;
        }

        return !(e.hasMetadata(GlobalValues.NOT_ENOUGH_NEAR) || e.hasMetadata(GlobalValues.METATAG));

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
                    if (nearby.hasMetadata(GlobalValues.NOT_ENOUGH_NEAR)
                            && nearby.getMetadata(GlobalValues.NOT_ENOUGH_NEAR).size() > 0
                            && nearby.getMetadata(GlobalValues.NOT_ENOUGH_NEAR).get(0).asBoolean()) {
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
                        nearby.setMetadata(GlobalValues.NOT_ENOUGH_NEAR, new FixedMetadataValue(sm, false));
                        nearby.setMetadata(GlobalValues.METATAG, new FixedMetadataValue(sm, 1));
                    }
                }
            }else{
                return true;
            }
        }
        return false;
    }

}
