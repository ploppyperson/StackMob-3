package uk.antiperson.stackmob.api.tools;

import java.util.UUID;
import java.util.regex.Pattern;

public final class UuidUtil {

    private static final Pattern UUID_PATTERN = Pattern.compile("(\\w{8})-?(\\w{4})-?(\\w{4})-?(\\w{4})-?(\\w{12})");

    public static UUID fromString(String raw) {
        return UUID.fromString(UUID_PATTERN.matcher(raw).replaceAll("$1-$2-$3-$4-$5"));
    }

}