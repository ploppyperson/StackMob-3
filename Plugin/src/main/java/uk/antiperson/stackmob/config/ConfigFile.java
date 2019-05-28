package uk.antiperson.stackmob.config;

import uk.antiperson.stackmob.StackMobPlugin;

public class ConfigFile extends ConfigLoader {

    public ConfigFile(StackMobPlugin sm){
        super(sm, "config");
    }
}
