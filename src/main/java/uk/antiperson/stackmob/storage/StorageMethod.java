package uk.antiperson.stackmob.storage;

import java.util.UUID;

public interface StorageMethod {

    void saveCache();

    void loadCache();

    void setValue(UUID uuid, int size);

    int getValue(UUID uuid);

}
