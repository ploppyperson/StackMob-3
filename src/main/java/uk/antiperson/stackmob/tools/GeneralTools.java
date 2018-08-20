package uk.antiperson.stackmob.tools;

import org.bukkit.entity.Entity;
import uk.antiperson.stackmob.tools.extras.GlobalValues;

public class GeneralTools {

    public static boolean hasInvaildMetadata(Entity entity){
        return !(entity.hasMetadata(GlobalValues.METATAG) && entity.getMetadata(GlobalValues.METATAG).size() > 0);
    }

    public static boolean hasInvaildMetadata(Entity entity, String metaTag){
        return !(entity.hasMetadata(metaTag) && entity.getMetadata(metaTag).size() > 0);
    }
}
