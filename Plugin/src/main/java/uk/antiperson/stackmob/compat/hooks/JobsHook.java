package uk.antiperson.stackmob.compat.hooks;

import com.gamingmesh.jobs.Jobs;
import org.bukkit.entity.Entity;
import org.bukkit.metadata.FixedMetadataValue;
import uk.antiperson.stackmob.StackMobPlugin;
import uk.antiperson.stackmob.api.compat.IHookManager;
import uk.antiperson.stackmob.api.compat.CloneTrait;
import uk.antiperson.stackmob.api.compat.PluginCompat;
import uk.antiperson.stackmob.compat.PluginHook;

public class JobsHook extends PluginHook implements CloneTrait {

    public JobsHook(IHookManager hm, StackMobPlugin sm){
        super(hm, sm, PluginCompat.JOBS);
    }

    @Override
    public void enable() {
        if(getStackMob().getCustomConfig().getBoolean("jobs-reborn.enabled")){
            getHookManager().registerHook(PluginCompat.JOBS, this);
        }
    }

    @Override
    public void setTrait(Entity entity) {
        if(getStackMob().getCustomConfig().getStringList("jobs-reborn.blacklist")
                .contains(entity.getType().toString())){
            return;
        }
        String metadata = Jobs.getPlayerManager().getMobSpawnerMetadata();
        entity.setMetadata(metadata, new FixedMetadataValue(getPlugin(), true));
    }


}
