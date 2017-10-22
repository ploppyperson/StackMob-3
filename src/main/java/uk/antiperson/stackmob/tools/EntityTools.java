package uk.antiperson.stackmob.tools;

import io.lumine.xikage.mythicmobs.mobs.MobManager;
import org.bukkit.entity.*;
import org.bukkit.metadata.FixedMetadataValue;
import uk.antiperson.stackmob.StackMob;
import uk.antiperson.stackmob.tools.extras.GlobalValues;

import java.util.concurrent.ThreadLocalRandom;

/**
 * Created by nathat on 24/07/17.
 */
public class EntityTools {

    private StackMob sm;
    public EntityTools(StackMob sm){
        this.sm = sm;
    }

    // Compares the differences between two entities
    // newe and nearby are ALWAYS the same entity type!
    public boolean notMatching(Entity newe, Entity nearby) {
        // Just checking if the entity is dead or not, just to be sure.
        if(nearby.isDead()){
            return true;
        }

        if(newe.hasMetadata(GlobalValues.NO_STACK_ALL) && newe.getMetadata(GlobalValues.NO_STACK_ALL).get(0).asBoolean()){
            return true;
        }

        if(nearby.hasMetadata(GlobalValues.NO_STACK_ALL) && nearby.getMetadata(GlobalValues.NO_STACK_ALL).get(0).asBoolean()){
            return true;
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
        if (newe instanceof Villager) {
            if (sm.config.getCustomConfig().getBoolean("compare.villager-profession")) {
                if (((Villager) newe).getProfession() != ((Villager) nearby).getProfession()) {
                    return true;
                }
            }
        }
        if (newe instanceof Sheep) {
            if (sm.config.getCustomConfig().getBoolean("compare.sheep-wool-sheared")) {
                if (((Sheep) newe).isSheared() != ((Sheep) nearby).isSheared()) {
                    return true;
                }
            }
            if (sm.config.getCustomConfig().getBoolean("compare.sheep-wool-color")) {
                if (((Sheep) newe).getColor() != ((Sheep) nearby).getColor()) {
                    return true;
                }
            }
        }
        if (newe instanceof Slime) {
            if (sm.config.getCustomConfig().getBoolean("compare.slime-size")) {
                if (((Slime) newe).getSize() != ((Slime) nearby).getSize()) {
                    return true;
                }
            }
        }
        if (newe instanceof Ageable) {
            if (sm.config.getCustomConfig().getBoolean("compare.entity-age")) {
                if (((Ageable) newe).isAdult() != ((Ageable) nearby).isAdult()) {
                    return true;
                }
            }
        }
        if (newe instanceof Zombie) {
            if (sm.config.getCustomConfig().getBoolean("compare.zombie-is-villager") && sm.getVersionId() == 1) {
                if (((Zombie) newe).isVillager() != ((Zombie) nearby).isVillager()) {
                    return true;
                }
            }
            if (sm.config.getCustomConfig().getBoolean("compare.zombie-villager-profession") && sm.getVersionId() <= 3  && sm.getVersionId() > 1) {
                if (((Zombie) newe).getVillagerProfession() != ((Zombie) nearby).getVillagerProfession()) {
                    return true;
                }
            }
            if (sm.config.getCustomConfig().getBoolean("compare.entity-age")) {
                if (((Zombie) newe).isBaby() != ((Zombie) nearby).isBaby()) {
                    return true;
                }
            }
        }
        if (newe instanceof Skeleton) {
            if (sm.config.getCustomConfig().getBoolean("compare.skeleton-type") && sm.getVersionId() <= 3) {
                if (((Skeleton) newe).getSkeletonType() != ((Skeleton) nearby).getSkeletonType()) {
                    return true;
                }
            }
        }
        if(newe instanceof Animals){
            if (sm.config.getCustomConfig().getBoolean("compare.can-breed")){
                if (((Animals) newe).canBreed() != ((Animals) nearby).canBreed()) {
                    return true;
                }
            }
        }
        if(newe instanceof Horse){
            if(sm.config.getCustomConfig().getBoolean("compare.horse-color")){
                if(((Horse)newe).getColor() != ((Horse) nearby).getColor()){
                    return true;
                }
            }
        }
        if(sm.getVersionId() >= 4){
            if(newe instanceof Llama){
                if (sm.config.getCustomConfig().getBoolean("compare.llama-color")){
                    if (((Llama) newe).getColor() != ((Llama) nearby).getColor()) {
                        return true;
                    }
                }
            }
            if(sm.getVersionId() >= 5) {
                if (newe instanceof Parrot) {
                    if (sm.config.getCustomConfig().getBoolean("compare.parrot-color")) {
                        if (((Parrot) newe).getVariant() != ((Parrot) nearby).getVariant()) {
                            return true;
                        }
                    }
                }
            }
        }

        if(sm.pluginSupport.getMythicSupport() != null){
            MobManager mm = sm.pluginSupport.getMythicSupport().getMythicMobs();
            if(mm.isActiveMob(nearby.getUniqueId()) && mm.isActiveMob(newe.getUniqueId())){
                if(sm.config.getCustomConfig().getStringList("mythicmobs.blacklist")
                        .contains(mm.getMythicMobInstance(nearby).getType().getInternalName())){
                    return true;
                }
                if(sm.config.getCustomConfig().getStringList("mythicmobs.blacklist")
                        .contains(mm.getMythicMobInstance(newe).getType().getInternalName())){
                    return true;
                }
                if(!mm.getMythicMobInstance(nearby).getType().getInternalName().equals(mm.getMythicMobInstance(newe).getType().getInternalName())){
                    return true;
                }
            }
        }


        return false;
    }

    // Copies all of the attributes of one entity and gives them to another.
    // TODO: fire ticks, no ai if you don't want faction server owners screaming at you.
    public Entity duplicate(Entity original){
        Entity dupe;

        if(sm.pluginSupport.getMythicSupport() != null && sm.pluginSupport.getMythicSupport().isMythicMob(original)){
            dupe = sm.pluginSupport.getMythicSupport().spawnMythicMob(original);
        }else{
            dupe = original.getWorld().spawnEntity(original.getLocation(), original.getType());
        }

        if (dupe instanceof Tameable) {
            if (!sm.config.getCustomConfig().getBoolean("check.tamed")) {
                ((Tameable)dupe).setTamed(((Tameable)original).isTamed());
                ((Tameable)dupe).setOwner(((Tameable)original).getOwner());
            }
        }
        if (dupe instanceof Villager) {
            if (sm.config.getCustomConfig().getBoolean("compare.villager-profession")) {
                ((Villager) dupe).setProfession(((Villager) original).getProfession());
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
            if (sm.config.getCustomConfig().getBoolean("compare.zombie-is-villager") && sm.getVersionId() == 1) {
                ((Zombie) dupe).setVillager(((Zombie) original).isVillager());
            }
            if (sm.config.getCustomConfig().getBoolean("compare.zombie-villager-profession") && sm.getVersionId() <= 3 && sm.getVersionId() > 1) {
                ((Zombie) dupe).setVillagerProfession(((Zombie) original).getVillagerProfession());
            }
            if (sm.config.getCustomConfig().getBoolean("compare.entity-age")) {
                ((Zombie) dupe).setBaby(((Zombie) original).isBaby());
            }
        }

        if (dupe instanceof Skeleton) {
            if (sm.config.getCustomConfig().getBoolean("compare.skeleton-type") && sm.getVersionId() <= 3) {
                ((Skeleton) dupe).setSkeletonType(((Skeleton) original).getSkeletonType());
            }
        }

        if(dupe instanceof Animals){
            if (sm.config.getCustomConfig().getBoolean("compare.can-breed")){
                ((Animals) dupe).setBreed(((Animals) original).canBreed());
            }
        }

        if(sm.getVersionId() >= 4){
            if(dupe instanceof Llama){
                if (sm.config.getCustomConfig().getBoolean("compare.llama-color")){
                    ((Llama) dupe).setColor(((Llama) original).getColor());
                }
            }
            if(sm.getVersionId() >= 5) {
                if (dupe instanceof Parrot) {
                    if (sm.config.getCustomConfig().getBoolean("compare.parrot-color")) {
                        ((Parrot) dupe).setVariant(((Parrot) original).getVariant());
                    }
                }
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
        int maxSize = sm.config.getCustomConfig().getInt("stack-max");
        if(sm.config.getCustomConfig().isInt("custom." + e.getType() + ".stack-max")){
            maxSize = sm.config.getCustomConfig().getInt("custom." + e.getType() + ".stack-max");
        }
        if(e.hasMetadata(GlobalValues.METATAG)){
            if(e.getMetadata(GlobalValues.METATAG).get(0).asInt() == maxSize){
                return true;
            }
        }else if(e.hasMetadata(GlobalValues.NOT_ENOUGH_NEAR)){
            if(!e.getMetadata(GlobalValues.NOT_ENOUGH_NEAR).get(0).asBoolean()){
                return true;
            }
        }else{
            return true;
        }
        return false;
    }

    public void spawnMoreSlime(Slime bigSlime, int amountDead){
        int newSlimeSize = (int) Math.floor(bigSlime.getSize() / 2);
        for(int i = 0; i < amountDead; i++){
            Slime smallSlime = (Slime) bigSlime.getWorld().spawnEntity(bigSlime.getLocation(), EntityType.SLIME);
            smallSlime.setSize(newSlimeSize);
            smallSlime.setMetadata(GlobalValues.NO_SPAWN_STACK, new FixedMetadataValue(sm, true));
            smallSlime.setMetadata(GlobalValues.METATAG, new FixedMetadataValue(sm, ThreadLocalRandom.current().nextInt(2,4)));
        }
    }



}
