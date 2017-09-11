package uk.antiperson.stackmob.tools.extras;

import io.lumine.xikage.mythicmobs.mobs.ActiveMob;
import io.lumine.xikage.mythicmobs.mobs.MobManager;
import org.bukkit.entity.Entity;
import uk.antiperson.stackmob.StackMob;

/**
 * Created by nathat on 05/08/17.
 */
public class MythicSupport {

    private StackMob sm;
    public MythicSupport(StackMob sm){
        this.sm = sm;
    }

    public Entity spawnMythicMob(Entity ea) {
        ActiveMob am = getMythicMobs().getMythicMobInstance(ea);
        return getMythicMobs().spawnMob(am.getType().getInternalName(), ea.getLocation()).getLivingEntity();
    }

    public boolean isMythicMob(Entity e){
        return getMythicMobs().isActiveMob(e.getUniqueId());
    }


    public MobManager getMythicMobs(){
        io.lumine.xikage.mythicmobs.MythicMobs mm = (io.lumine.xikage.mythicmobs.MythicMobs) sm.getServer().getPluginManager().getPlugin("MythicMobs");
        return mm.getMobManager();
    }
}
