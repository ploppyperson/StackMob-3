package uk.antiperson.stackmob.utils;

import org.bukkit.Bukkit;

public enum BukkitVersion {
    V1_8,
    V1_9,
    V1_10,
    V1_11,
    V1_12,
    UNSUPPORTED;

    private final static BukkitVersion currentVersion;

    static {
        String rawVersion = Bukkit.getVersion();
        if (rawVersion.contains("1.8")) {
            currentVersion = V1_8;
        } else if (rawVersion.contains("1.9")) {
            currentVersion = V1_9;
        } else if (rawVersion.contains("1.10")) {
            currentVersion = V1_10;
        } else if (rawVersion.contains("1.11")) {
            currentVersion = V1_11;
        } else if (rawVersion.contains("1.12")) {
            currentVersion = V1_12;
        } else {
            currentVersion = UNSUPPORTED;
        }
    }

    public static BukkitVersion getVersion() {
        return currentVersion;
    }

    public static boolean isAtLeast(BukkitVersion version) {
        return version.ordinal() <= currentVersion.ordinal();
    }

}
