package uk.antiperson.stackmob.bukkit.v1_14;

import org.bukkit.Material;
import org.bukkit.entity.Cat;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Horse;
import org.bukkit.entity.Wolf;
import org.bukkit.inventory.ItemStack;
import uk.antiperson.stackmob.bukkit.BukkitCompat;

public class BukkitHandler implements BukkitCompat {

    public boolean checkFood(ItemStack is, Entity entity) {
        Material type = is.getType();
        switch (entity.getType()) {
            case COW:
            case SHEEP:
            case MUSHROOM_COW:
                return type == Material.WHEAT;
            case PIG:
                return (type == Material.CARROT || type == Material.BEETROOT || type == Material.POTATO);
            case CHICKEN:
                return type == Material.WHEAT_SEEDS
                        || type == Material.MELON_SEEDS
                        || type == Material.BEETROOT_SEEDS
                        || type == Material.PUMPKIN_SEEDS;
            case HORSE:
                return (type == Material.GOLDEN_APPLE || type == Material.GOLDEN_CARROT) && ((Horse)entity).isTamed();
            case WOLF:
                return (type == Material.BEEF
                        || type == Material.CHICKEN
                        || type == Material.COD
                        || type == Material.MUTTON
                        || type == Material.PORKCHOP
                        || type == Material.RABBIT
                        || type == Material.SALMON
                        || type == Material.COOKED_BEEF
                        || type == Material.COOKED_CHICKEN
                        || type == Material.COOKED_COD
                        || type == Material.COOKED_MUTTON
                        || type == Material.COOKED_PORKCHOP
                        || type == Material.COOKED_RABBIT
                        || type == Material.COOKED_SALMON)
                        && ((Wolf) entity).isTamed();
            case OCELOT:
                return (type == Material.SALMON
                        || type == Material.COD
                        || type == Material.PUFFERFISH
                        || type == Material.TROPICAL_FISH);
            case RABBIT:
                return type == Material.CARROT || type == Material.GOLDEN_CARROT || type == Material.DANDELION;
            case LLAMA:
                return type == Material.HAY_BLOCK;
            case TURTLE:
                return type == Material.SEAGRASS;
            case PANDA:
                return type == Material.BAMBOO;
            case FOX:
                return type == Material.SWEET_BERRIES;
            case CAT:
                return (type == Material.COD || type == Material.SALMON) && ((Cat) entity).isTamed();
        }
        return false;
    }
}
