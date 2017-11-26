package uk.antiperson.stackmob.services;

import lombok.AllArgsConstructor;
import org.bukkit.entity.*;
import uk.antiperson.stackmob.GlobalValues;
import uk.antiperson.stackmob.config.Config;
import uk.antiperson.stackmob.utils.BukkitVersion;

import java.util.HashSet;
import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;

@AllArgsConstructor
@SuppressWarnings("deprecation")
public class EntityService {

    private Config config;
    private SupportService supportService;
    private BukkitService bukkitService;

    // Compares the differences between two entities newe and nearby are ALWAYS the same entity type!
    public boolean notMatching(Entity entity, Entity nearby) {
        // Just checking if the entity is dead or not, just to be sure.
        if (nearby.isDead()) {
            return true;
        }

        if (entity.hasMetadata(GlobalValues.NO_STACK_ALL) && entity.getMetadata(GlobalValues.NO_STACK_ALL).get(0).asBoolean()) {
            return true;
        }

        if (nearby.hasMetadata(GlobalValues.NO_STACK_ALL) && nearby.getMetadata(GlobalValues.NO_STACK_ALL).get(0).asBoolean()) {
            return true;
        }

        // Checks on the nearby entity
        if (nearby instanceof Tameable) {
            if (config.get().getBoolean("check.tamed")) {
                if (((Tameable) nearby).isTamed()) {
                    return true;
                }
            }
        }
        if (((LivingEntity) nearby).isLeashed()) {
            if (config.get().getBoolean("check.leashed")) {
                return true;
            }
        }
        // Checks on both entities
        if (entity instanceof Villager) {
            if (config.get().getBoolean("compare.villager-profession")) {
                if (((Villager) entity).getProfession() != ((Villager) nearby).getProfession()) {
                    return true;
                }
            }
        }
        if (entity instanceof Sheep) {
            if (config.get().getBoolean("compare.sheep-wool-sheared")) {
                if (((Sheep) entity).isSheared() != ((Sheep) nearby).isSheared()) {
                    return true;
                }
            }
            if (config.get().getBoolean("compare.sheep-wool-color")) {
                if (((Sheep) entity).getColor() != ((Sheep) nearby).getColor()) {
                    return true;
                }
            }
        }
        if (entity instanceof Slime) {
            if (config.get().getBoolean("compare.slime-size")) {
                if (((Slime) entity).getSize() != ((Slime) nearby).getSize()) {
                    return true;
                }
            }
        }
        if (entity instanceof Ageable) {
            if (config.get().getBoolean("compare.entity-age")) {
                if (((Ageable) entity).isAdult() != ((Ageable) nearby).isAdult()) {
                    return true;
                }
            }
        }
        if (entity instanceof Zombie) {
            if (config.get().getBoolean("compare.zombie-is-villager") && BukkitVersion.getVersion() == BukkitVersion.V1_8) {
                if (((Zombie) entity).isVillager() != ((Zombie) nearby).isVillager()) {
                    return true;
                }
            }
            if (config.get().getBoolean("compare.zombie-villager-profession") && !BukkitVersion.isAtLeast(BukkitVersion.V1_11)
                    && BukkitVersion.getVersion() != BukkitVersion.V1_8) {
                if (((Zombie) entity).getVillagerProfession() != ((Zombie) nearby).getVillagerProfession()) {
                    return true;
                }
            }
            if (config.get().getBoolean("compare.entity-age")) {
                if (((Zombie) entity).isBaby() != ((Zombie) nearby).isBaby()) {
                    return true;
                }
            }
        }
        if (entity instanceof Skeleton) {
            if (config.get().getBoolean("compare.skeleton-type") && !BukkitVersion.isAtLeast(BukkitVersion.V1_11)) {
                if (((Skeleton) entity).getSkeletonType() != ((Skeleton) nearby).getSkeletonType()) {
                    return true;
                }
            }
        }
        if (entity instanceof Animals) {
            if (config.get().getBoolean("compare.can-breed")) {
                if (((Animals) entity).canBreed() != ((Animals) nearby).canBreed()) {
                    return true;
                }
            }
        }
        if (entity instanceof Horse) {
            if (config.get().getBoolean("compare.horse-color")) {
                if (((Horse) entity).getColor() != ((Horse) nearby).getColor()) {
                    return true;
                }
            }
        }
        if (BukkitVersion.isAtLeast(BukkitVersion.V1_11)) {
            if (entity instanceof Llama) {
                if (config.get().getBoolean("compare.llama-color")) {
                    if (((Llama) entity).getColor() != ((Llama) nearby).getColor()) {
                        return true;
                    }
                }
            }
            if (BukkitVersion.isAtLeast(BukkitVersion.V1_12)) {
                if (entity instanceof Parrot) {
                    if (config.get().getBoolean("compare.parrot-color")) {
                        if (((Parrot) entity).getVariant() != ((Parrot) nearby).getVariant()) {
                            return true;
                        }
                    }
                }
            }
        }

        return supportService.notMatchMythicMobs(entity, nearby);
    }

    // Copies all of the attributes of one entity and gives them to another.
    // TODO: fire ticks, no ai if you don't want faction server owners screaming at you.
    public Entity duplicate(Entity original) {
        Entity dupe;

        if (supportService.isMythicMob(original)) {
            dupe = supportService.spawnMythicMob(original);
        } else {
            dupe = original.getWorld().spawnEntity(original.getLocation(), original.getType());
        }

        if (dupe instanceof Tameable) {
            if (!config.get().getBoolean("check.tamed")) {
                ((Tameable) dupe).setTamed(((Tameable) original).isTamed());
                ((Tameable) dupe).setOwner(((Tameable) original).getOwner());
            }
        }
        if (dupe instanceof Villager) {
            if (config.get().getBoolean("compare.villager-profession")) {
                ((Villager) dupe).setProfession(((Villager) original).getProfession());
            }
        }

        if (dupe instanceof Sheep) {
            if (config.get().getBoolean("compare.sheep-wool-sheared")) {
                ((Sheep) dupe).setSheared(((Sheep) original).isSheared());
            }
            if (config.get().getBoolean("compare.sheep-wool-color")) {
                ((Sheep) dupe).setColor(((Sheep) original).getColor());
            }
        }

        if (dupe instanceof Slime) {
            if (config.get().getBoolean("compare.slime-size")) {
                ((Slime) dupe).setSize(((Slime) original).getSize());
            }
        }

        if (dupe instanceof Ageable) {
            if (config.get().getBoolean("compare.entity-age")) {
                if (((Ageable) original).isAdult()) {
                    ((Ageable) dupe).setAdult();
                } else {
                    ((Ageable) dupe).setBaby();
                }
            }
        }
        if (dupe instanceof Zombie) {
            if (config.get().getBoolean("compare.zombie-is-villager") && BukkitVersion.getVersion() == BukkitVersion.V1_8) {
                ((Zombie) dupe).setVillager(((Zombie) original).isVillager());
            }
            if (config.get().getBoolean("compare.zombie-villager-profession") && !BukkitVersion.isAtLeast(BukkitVersion.V1_11)
                    && BukkitVersion.getVersion() != BukkitVersion.V1_8) {
                ((Zombie) dupe).setVillagerProfession(((Zombie) original).getVillagerProfession());
            }
            if (config.get().getBoolean("compare.entity-age")) {
                ((Zombie) dupe).setBaby(((Zombie) original).isBaby());
            }
        }

        if (dupe instanceof Skeleton) {
            if (config.get().getBoolean("compare.skeleton-type") && !BukkitVersion.isAtLeast(BukkitVersion.V1_11)) {
                ((Skeleton) dupe).setSkeletonType(((Skeleton) original).getSkeletonType());
            }
        }

        if (dupe instanceof Animals) {
            if (config.get().getBoolean("compare.can-breed")) {
                ((Animals) dupe).setBreed(((Animals) original).canBreed());
            }
        }

        if (BukkitVersion.isAtLeast(BukkitVersion.V1_11)) {
            if (dupe instanceof Llama) {
                if (config.get().getBoolean("compare.llama-color")) {
                    ((Llama) dupe).setColor(((Llama) original).getColor());
                }
            }
            if (BukkitVersion.isAtLeast(BukkitVersion.V1_12)) {
                if (dupe instanceof Parrot) {
                    if (config.get().getBoolean("compare.parrot-color")) {
                        ((Parrot) dupe).setVariant(((Parrot) original).getVariant());
                    }
                }
            }
        }

        // mcMMO stuff
        supportService.setMcMMOMetadata(dupe);

        return dupe;
    }

    public boolean notTaskSuitable(Entity entity) {
        if (entity.isDead()) {
            return true;
        }
        if (entity.hasMetadata(GlobalValues.NO_TASK_STACK) && entity.getMetadata(GlobalValues.NO_TASK_STACK).get(0).asBoolean()) {
            return true;
        }
        if (entity.hasMetadata(GlobalValues.NO_STACK_ALL) && entity.getMetadata(GlobalValues.NO_STACK_ALL).get(0).asBoolean()) {
            return true;
        }
        int maxSize = config.get().getInt("stack-max");
        if (config.get().isInt("custom." + entity.getType() + ".stack-max")) {
            maxSize = config.get().getInt("custom." + entity.getType() + ".stack-max");
        }
        if (entity.hasMetadata(GlobalValues.METATAG)) {
            if (entity.getMetadata(GlobalValues.METATAG).size() == 0 || entity.getMetadata(GlobalValues.METATAG).get(0).asInt() == maxSize) {
                return true;
            }
        } else if (entity.hasMetadata(GlobalValues.NOT_ENOUGH_NEAR)) {
            if (entity.getMetadata(GlobalValues.NOT_ENOUGH_NEAR).size() == 0 || !entity.getMetadata(GlobalValues.NOT_ENOUGH_NEAR).get(0).asBoolean()) {
                return true;
            }
        } else {
            return true;
        }
        return false;
    }

    public void spawnMoreSlime(Slime bigSlime, int amountDead) {
        int newSlimeSize = (int) Math.floor(bigSlime.getSize() / 2);
        for (int i = 0; i < amountDead; i++) {
            Slime smallSlime = (Slime) bigSlime.getWorld().spawnEntity(bigSlime.getLocation(), EntityType.SLIME);
            smallSlime.setSize(newSlimeSize);
            bukkitService.setMetadata(smallSlime, GlobalValues.NO_SPAWN_STACK, true);
            bukkitService.setMetadata(smallSlime, GlobalValues.METATAG, ThreadLocalRandom.current().nextInt(2, 4));
        }
    }

    public boolean notEnoughNearby(Entity original) {
        double xLoc = config.get().getDouble("check-area.x");
        double yLoc = config.get().getDouble("check-area.y");
        double zLoc = config.get().getDouble("check-area.z");
        if (config.get().getInt("dont-stack-until") > 0) {
            HashSet<UUID> entities = new HashSet<>();
            entities.add(original.getUniqueId());
            for (Entity nearby : original.getNearbyEntities(xLoc, yLoc, zLoc)) {
                if (original.getType() == nearby.getType()) {
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
            if (entities.size() >= config.get().getInt("dont-stack-until")) {
                for (UUID uuid : entities) {
                    Entity entity = bukkitService.getEntity(uuid);
                    if (entity == null) {
                        continue;
                    }
                    bukkitService.setMetadata(entity, GlobalValues.NOT_ENOUGH_NEAR, false);
                    bukkitService.setMetadata(entity, GlobalValues.METATAG, 1);
                }
            } else {
                return true;
            }
        }
        return false;
    }

}
