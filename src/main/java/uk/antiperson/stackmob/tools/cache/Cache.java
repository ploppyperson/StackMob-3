package uk.antiperson.stackmob.tools.cache;

import java.util.Set;
import java.util.UUID;

public interface Cache {

    void write(UUID key, int value);
    int read(UUID key);
    void load();
    void close();
    void convert();
    void remove(UUID key);
    boolean contains(UUID key);
    Set<UUID> getKeys();
}
