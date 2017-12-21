package uk.antiperson.stackmob.tools.plugin;

import com.sk89q.worldguard.bukkit.WorldGuardPlugin;
import com.sk89q.worldguard.protection.ApplicableRegionSet;
import com.sk89q.worldguard.protection.flags.StateFlag;
import com.sk89q.worldguard.protection.flags.registry.FlagConflictException;
import com.sk89q.worldguard.protection.flags.registry.FlagRegistry;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import uk.antiperson.stackmob.StackMob;

public class WorldGuardSupport {

    private WorldGuardPlugin wgp = (WorldGuardPlugin) Bukkit.getServer().getPluginManager().getPlugin("WorldGuard");
    private static final StateFlag ENTITY_FLAG = new StateFlag("entity-stacking", true);
    private StackMob sm;
    public WorldGuardSupport(StackMob sm){
        this.sm = sm;
    }

    public void registerFlag(){
        FlagRegistry registry = wgp.getFlagRegistry();
        try{
            registry.register(ENTITY_FLAG);
        }catch (FlagConflictException e){
            sm.getLogger().warning("A conflict occurred while trying to register the WorldGuard flag.");
            e.printStackTrace();
        }
    }

    public boolean checkCanStack(Location location){
        ApplicableRegionSet ars = wgp.getRegionManager(location.getWorld()).getApplicableRegions(location);
        return ars.testState(null, ENTITY_FLAG);
    }

    public WorldGuardPlugin getWgp() {
        return wgp;
    }
}
