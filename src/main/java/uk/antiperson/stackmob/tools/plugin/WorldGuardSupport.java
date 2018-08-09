package uk.antiperson.stackmob.tools.plugin;

import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldguard.WorldGuard;
import com.sk89q.worldguard.protection.ApplicableRegionSet;
import com.sk89q.worldguard.protection.flags.StateFlag;
import com.sk89q.worldguard.protection.flags.registry.FlagConflictException;
import com.sk89q.worldguard.protection.flags.registry.FlagRegistry;
import com.sk89q.worldguard.protection.regions.RegionContainer;
import org.bukkit.Location;
import uk.antiperson.stackmob.StackMob;

public class WorldGuardSupport {

    private static final StateFlag ENTITY_FLAG = new StateFlag("entity-stacking", true);
    private StackMob sm;
    public WorldGuardSupport(StackMob sm){
        this.sm = sm;
    }

    public void registerFlag(){
        FlagRegistry registry = WorldGuard.getInstance().getFlagRegistry();
        try{
            registry.register(ENTITY_FLAG);
        }catch (FlagConflictException e){
            sm.getLogger().warning("A conflict occurred while trying to register the WorldGuard flag.");
            e.printStackTrace();
        }
    }

    public boolean checkCanStack(Location location){
        try {
            RegionContainer rc = WorldGuard.getInstance().getPlatform().getRegionContainer();
            ApplicableRegionSet ars = rc.get(BukkitAdapter.adapt(location.getWorld())).getApplicableRegions(BukkitAdapter.adapt(location).toVector());
            return ars.testState(null, ENTITY_FLAG);
        }catch (NullPointerException e){
            return false;
        }
    }

}
