package uk.antiperson.stackmob.compat.hooks;

import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldguard.protection.ApplicableRegionSet;
import com.sk89q.worldguard.protection.flags.StateFlag;
import com.sk89q.worldguard.protection.flags.registry.FlagConflictException;
import com.sk89q.worldguard.protection.flags.registry.FlagRegistry;
import com.sk89q.worldguard.protection.managers.RegionManager;
import com.sk89q.worldguard.protection.regions.RegionContainer;
import org.bukkit.entity.Entity;
import uk.antiperson.stackmob.StackMob;

public class WorldGuard {

    private StackMob sm;
    private static final StateFlag ENTITY_FLAG = new StateFlag("entity-stacking", true);
    public WorldGuard(StackMob sm){
        this.sm = sm;
    }


    public void registerFlag(){
        FlagRegistry registry = com.sk89q.worldguard.WorldGuard.getInstance().getFlagRegistry();
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
            RegionContainer rc = com.sk89q.worldguard.WorldGuard.getInstance().getPlatform().getRegionContainer();
            RegionManager regionManager = rc.get(BukkitAdapter.adapt(entity.getWorld()));
            ApplicableRegionSet ars = regionManager.getApplicableRegions(BukkitAdapter.asBlockVector(entity.getLocation()));
            return !(ars.testState(null, ENTITY_FLAG));
        }catch (NullPointerException e){
            return false;
        }
    }

}
