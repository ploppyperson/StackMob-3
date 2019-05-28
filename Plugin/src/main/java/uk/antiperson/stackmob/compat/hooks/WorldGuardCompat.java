package uk.antiperson.stackmob.compat.hooks;

import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldguard.WorldGuard;
import com.sk89q.worldguard.protection.flags.StateFlag;
import com.sk89q.worldguard.protection.flags.registry.FlagConflictException;
import com.sk89q.worldguard.protection.flags.registry.FlagRegistry;
import com.sk89q.worldguard.protection.regions.RegionQuery;
import org.bukkit.entity.Entity;
import uk.antiperson.stackmob.StackMobPlugin;

public class WorldGuardCompat {

    private StackMobPlugin sm;
    private static final StateFlag ENTITY_FLAG = new StateFlag("entity-stacking", true);
    public WorldGuardCompat(StackMobPlugin sm){
        this.sm = sm;
    }


    public void registerFlag(){
        FlagRegistry registry = WorldGuard.getInstance().getFlagRegistry();
        try {
            registry.register(ENTITY_FLAG);
            sm.getLogger().info("Registered WorldGuard region flag.");
        } catch (FlagConflictException e) {
            sm.getLogger().warning("A conflict occurred while trying to register the WorldGuard flag.");
            e.printStackTrace();
        }
    }

    public boolean test(Entity entity){
        try {
            RegionQuery rq = WorldGuard.getInstance().getPlatform().getRegionContainer().createQuery();
            return !(rq.testState(BukkitAdapter.adapt(entity.getLocation()), null, ENTITY_FLAG));
        }catch (NullPointerException e){
            return false;
        }
    }

}
