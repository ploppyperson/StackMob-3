package uk.antiperson.stackmob.api.tools;

import org.bukkit.Bukkit;
import uk.antiperson.stackmob.api.IStackMob;
import uk.antiperson.stackmob.api.bcompat.Compat;

import java.lang.reflect.InvocationTargetException;

public class VersionHelper {

    public static BukkitVersion getVersion() {
        if (Bukkit.getVersion().contains("1.13")) {
            return BukkitVersion.V1_13;
        } else if (Bukkit.getVersion().contains("1.14")) {
            return BukkitVersion.V1_14;
        }
        return BukkitVersion.UNSUPPORTED;
    }

    public static boolean isVersionNewerThan(BukkitVersion version) {
        return version.getId() >= getVersion().getId();
    }

    public static Compat getBukkitCompat(IStackMob sm) {
        try {
            Class clazz = Class.forName("uk.antiperson.stackmob.bcompat." + getVersion().toString().toLowerCase() + ".BukkitCompat");
            return (Compat) clazz.getConstructor(IStackMob.class).newInstance(sm);
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | NoSuchMethodException  | InvocationTargetException e) {
            e.printStackTrace();
            return null;
        }
    }
}
