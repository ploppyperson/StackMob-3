package uk.antiperson.stackmob.tools;

import org.bukkit.entity.Entity;
import uk.antiperson.stackmob.tools.extras.GlobalValues;

public class GeneralTools {

    public static boolean hasInvalidMetadata(Entity entity){
        return hasInvalidMetadata(entity, GlobalValues.METATAG);
    }

    public static boolean hasInvalidMetadata(Entity entity, String metaTag){
        return !(entity.hasMetadata(metaTag)) || entity.getMetadata(metaTag).size() == 0;
    }
}
