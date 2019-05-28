package uk.antiperson.stackmob.compat.hooks;

import net.aminecraftdev.customdrops.CustomDropsAPI;
import org.bukkit.entity.Entity;
import org.bukkit.inventory.ItemStack;
import uk.antiperson.stackmob.StackMobPlugin;
import uk.antiperson.stackmob.api.compat.HookManager;
import uk.antiperson.stackmob.api.compat.PluginCompat;
import uk.antiperson.stackmob.compat.PluginHook;

import java.util.List;

public class CustomDropsHook extends PluginHook {

    public CustomDropsHook(HookManager hm, StackMobPlugin sm){
        super(hm, sm, PluginCompat.CUSTOMDROPS);
    }

    @Override
    public void enable() {
        if(getStackMob().getCustomConfig().getBoolean("custom-drops.enabled")){
            getHookManager().registerHook(PluginCompat.CUSTOMDROPS, this);
        }
    }

    public List<ItemStack> getDrops(Entity entity){
        return CustomDropsAPI.getCustomDrops(entity.getType());
    }

    public boolean hasCustomDrops(Entity entity){
        return !CustomDropsAPI.getNaturalDrops(entity.getType());
    }
}
