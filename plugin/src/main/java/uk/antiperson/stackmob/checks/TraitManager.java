package uk.antiperson.stackmob.checks;

import io.github.classgraph.*;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Entity;
import uk.antiperson.stackmob.StackMob;
import uk.antiperson.stackmob.api.checks.ApplicableTrait;
import uk.antiperson.stackmob.api.checks.ComparableTrait;
import uk.antiperson.stackmob.api.checks.SingleTrait;
import uk.antiperson.stackmob.tools.BukkitVersion;
import uk.antiperson.stackmob.tools.VersionHelper;

import java.lang.reflect.InvocationTargetException;
import java.util.HashSet;
import java.util.Set;
import java.util.regex.Pattern;

public class TraitManager {

    private Set<ComparableTrait> comparableTraits = new HashSet<>();
    private StackMob sm;
    public TraitManager(StackMob sm){
        this.sm = sm;
    }

    public void registerTraits(){
        String pkg = "uk.antiperson.stackmob.checks.trait";
        try (ScanResult scanResult =
                     new ClassGraph()
                             .verbose()                   // Log to stderr
                             .enableAllInfo()             // Scan classes, methods, fields, annotations
                             .whitelistPackages(pkg)      // Scan com.xyz and subpackages (omit to scan all packages)
                             .scan()) {                   // Start the scan
            for (ClassInfo routeClassInfo : scanResult.getAllClasses()) {
                Class<?> clazz = routeClassInfo.loadClass();
                try {
                    if (isAllowed(clazz.getName())) {
                        if (ComparableTrait.class.isAssignableFrom(clazz)) {
                            ComparableTrait comparableTrait;
                            try {
                                comparableTrait = (ComparableTrait) clazz.getConstructor().newInstance();
                            } catch (NoSuchMethodException e) {
                                // this is way too hacky for my liking but it works so idk
                                comparableTrait = (ComparableTrait) clazz.getConstructor(FileConfiguration.class).newInstance(getStackMob().getCustomConfig());
                            }
                            if (getStackMob().getCustomConfig().getBoolean(comparableTrait.getConfigPath())) {
                                registerTrait(comparableTrait);
                            }
                        }
                    }
                } catch (NoSuchMethodException | IllegalAccessException | InstantiationException | InvocationTargetException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public boolean checkTraits(Entity original, Entity nearby){
        for(ComparableTrait comparableTrait : comparableTraits){
            if(comparableTrait.checkTrait(original, nearby)){
                return true;
            }
        }
        return false;
    }

    public boolean checkTraits(Entity original){
        for(ComparableTrait comparableTrait : comparableTraits){
            if(comparableTrait instanceof SingleTrait){
                SingleTrait singleTrait = (SingleTrait) comparableTrait;
                return singleTrait.checkTrait(original);
            }
        }
        return false;
    }

    public void applyTraits(Entity orginal,  Entity spawned){
        for(ComparableTrait comparableTrait : comparableTraits){
            if(comparableTrait instanceof ApplicableTrait){
                ApplicableTrait applicableTrait = (ApplicableTrait) comparableTrait;
                applicableTrait.applyTrait(orginal, spawned);
            }
        }
    }

    public void registerTrait(ComparableTrait trait){
        comparableTraits.add(trait);
    }

    public StackMob getStackMob() {
        return sm;
    }

    private boolean isAllowed(String classPath) {
        String a = classPath.split(Pattern.quote("."))[5];
        return a.equals("common") || VersionHelper.isVersionNewerThan(BukkitVersion.valueOf(a));
    }
}
