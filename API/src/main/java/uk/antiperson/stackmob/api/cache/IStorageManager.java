package uk.antiperson.stackmob.api.cache;

import uk.antiperson.stackmob.api.StackMob;

import java.util.Map;
import java.util.UUID;

public interface IStorageManager {
    void onServerEnable();

    void onServerDisable();

    Map<UUID, Integer> getCombinedMap();

    void saveStorage();

    StackMob getStackMob();

    StackStorage getStackStorage();

    Map<UUID, Integer> getAmountCache();
}
