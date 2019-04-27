package uk.antiperson.stackmob.bukkit;

import uk.antiperson.stackmob.tools.VersionHelper;

public class BukkitHelper {

    public static BukkitCompat getBukkitCompat() {
        try {
            final Class<?> clazz = Class.forName("uk.antiperson.stackmob.bukkit." + VersionHelper.getVersion().toString().toLowerCase() + ".BukkitHandler");
            // Check if we have a NMSHandler class at that location.
            if (BukkitCompat.class.isAssignableFrom(clazz)) {
                return  (BukkitCompat) clazz.getConstructor().newInstance();
            }
        } catch (final Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
