package uk.antiperson.stackmob.utils;

import lombok.experimental.UtilityClass;

@UtilityClass
public class StringUtils {

    public static String toTitleCase(String string) {
        String[] elements = string.split(" ");
        StringBuilder result = new StringBuilder();

        for (String element : elements) {
            result.append(Character.toUpperCase(element.charAt(0)))
                    .append(element.substring(1)).append(" ");
        }
        return result.toString().trim();
    }

}
