package uk.antiperson.stackmob.api.cache;

import java.util.Map;
import java.util.UUID;

public interface StorageMethod {

    void saveStorage(Map<UUID, Integer> values);

    void loadStorage();

}
