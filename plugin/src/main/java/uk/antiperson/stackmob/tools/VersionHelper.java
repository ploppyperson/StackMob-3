package uk.antiperson.stackmob.tools;

import io.github.classgraph.ClassGraph;
import io.github.classgraph.ClassInfo;
import io.github.classgraph.ScanResult;
import org.bukkit.Bukkit;

import java.util.HashSet;
import java.util.function.Predicate;
import java.util.regex.Pattern;

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

    public static HashSet<Class<?>> scanClasses(String packageName, Class<?> inter) {
        return scanClasses(packageName, aClass -> inter.isAssignableFrom(aClass));
    }

    private static HashSet<Class<?>> scanClasses(String packageName, Predicate<Class<?>> predicate) {
        HashSet<Class<?>> classes = new HashSet<>();
        try (ScanResult scanResult = new ClassGraph().enableAllInfo().whitelistPackages(packageName).scan()) {
            for (ClassInfo routeClassInfo : scanResult.getAllClasses()) {
                Class<?> clazz = routeClassInfo.loadClass();
                if (!isAllowed(routeClassInfo.getPackageInfo().getName())) {
                    continue;
                }
                if (predicate.test(clazz)) {
                    classes.add(clazz);
                }
            }
        }
        return classes;
    }

    private static boolean isAllowed(String classPath) {
        String[] b = classPath.split(Pattern.quote("."));
        String a = b[b.length - 1];
        return a.equals("common") || a.equalsIgnoreCase(VersionHelper.getVersion().toString());
    }
}
