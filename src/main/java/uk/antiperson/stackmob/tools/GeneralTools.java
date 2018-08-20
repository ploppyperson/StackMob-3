package uk.antiperson.stackmob.tools;

import org.bukkit.entity.Entity;
import uk.antiperson.stackmob.tools.extras.GlobalValues;

public class GeneralTools {

    public static boolean hasInvaildMetadata(Entity entity){
        return hasInvaildMetadata(entity, GlobalValues.METATAG);
    }

    public static boolean hasInvaildMetadata(Entity entity, String metaTag){
        return !(entity.hasMetadata(metaTag)) || entity.getMetadata(metaTag).size() == 0;
    }
}
