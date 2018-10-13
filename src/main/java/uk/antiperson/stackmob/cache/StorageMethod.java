package uk.antiperson.stackmob.cache;

import java.util.UUID;

public interface StorageMethod {

    void saveStorage();

    void loadStorage();

    void setValue(UUID uuid, int size);

    int getValue(UUID uuid);

}
