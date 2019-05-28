package uk.antiperson.stackmob.api.compat;

public enum PluginCompat {
    WORLDGUARD("WorldGuard"),
    PROCOTOLLIB("ProtocolLib"),
    MYTHICMOBS("MythicMobs"),
    MCMMO("mcMMO"),
    JOBS("Jobs"),
    CITIZENS("Citizens"),
    MINIATUREPETS("MiniaturePets"),
    CUSTOMDROPS("CustomDrops");

    private final String name;
    PluginCompat(String name){
        this.name = name;
    }

    public static PluginCompat getPluginHooks(String name) {
        for(PluginCompat hooks : values()){
            if(hooks.name.equals(name)){
                return hooks;
            }
        }
        throw new IllegalArgumentException("Invalid plugin hook name " + name);
    }

    public String getName() {
        return name;
    }
}
