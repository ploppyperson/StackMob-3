package uk.antiperson.stackmob.tools.plugin;

import net.aminecraftdev.customdrops.CustomDropsAPI;
import org.bukkit.entity.Entity;
import org.bukkit.inventory.ItemStack;

import java.util.List;

/**
 * @author Charles Cullen
 * @version 1.0.0
 * @since 23-Feb-19
 */
public class CustomDropsSupport {

    public List<ItemStack> getDrops(Entity entity) {
        return CustomDropsAPI.getCustomDrops(entity.getType());
    }

}
