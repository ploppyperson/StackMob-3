package uk.antiperson.stackmob.checks.trait.common;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Entity;
import uk.antiperson.stackmob.api.checks.ApplicableTrait;

public class FireTrait implements ApplicableTrait {

    private FileConfiguration config;
    public FireTrait(FileConfiguration config){
        this.config = config;
    }

    @Override
    public boolean checkTrait(Entity original, Entity nearby) {
        return false;
    }

    @Override
    public void applyTrait(Entity original, Entity spawned) {
        if(config.getStringList("fire-ticks.blacklist")
                .contains(original.getType().toString())){
            return;
        }
        spawned.setFireTicks(original.getFireTicks());
    }

    @Override
    public String getConfigPath() {
        return "fire-ticks.enabled";
    }
}
