package uk.antiperson.stackmob.tools;

import org.bukkit.entity.Entity;
import uk.antiperson.stackmob.tools.extras.GlobalValues;

public class GeneralTools {

    public static boolean hasValidStackData(Entity entity){
        return hasValidMetadata(entity, GlobalValues.METATAG) &&
                entity.getMetadata(GlobalValues.METATAG).get(0).asInt() >= 1;
    }

    public static boolean hasNotEnoughNear(Entity entity){
        return hasValidMetadata(entity, GlobalValues.METATAG) &&
                entity.getMetadata(GlobalValues.METATAG).get(0).asInt() == -1;
    }

    public static boolean hasValidMetadata(Entity entity){
        return hasValidMetadata(entity, GlobalValues.METATAG);
    }

    public static boolean hasValidMetadata(Entity entity, String metaTag){
        return entity.hasMetadata(metaTag) &&
                entity.getMetadata(metaTag).size() != 0;
    }


}
