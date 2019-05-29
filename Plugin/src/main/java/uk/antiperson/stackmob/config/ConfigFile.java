package uk.antiperson.stackmob.config;

import uk.antiperson.stackmob.StackMob;

public class ConfigFile extends ConfigLoader {

    public ConfigFile(StackMob sm){
        super(sm, "config");
    }
}
